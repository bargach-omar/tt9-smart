package com.tt9smart.ime;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.ArrayList;

import com.tt9smart.R;
import com.tt9smart.db.DataStore;
import com.tt9smart.db.words.DictionaryLoader;
import com.tt9smart.ime.helpers.SuggestionOps;
import com.tt9smart.ime.modes.InputModeKind;
import com.tt9smart.ui.UI;
import com.tt9smart.util.Text;
import com.tt9smart.util.TextTools;
import com.tt9smart.util.chars.Characters;
import com.tt9smart.util.sys.Clipboard;

abstract public class SuggestionHandler extends TypingHandler {
	@Nullable private Handler suggestionHandler;


	@Override
	protected void setInputField(EditorInfo field) {
		super.setInputField(field);
		mindReader.setInputType(inputType);
	}


	private Handler getAsyncHandler() {
		if (suggestionHandler == null) {
			suggestionHandler = new Handler(Looper.getMainLooper());
		}

		return suggestionHandler;
	}


	@Override
	protected void onFinishTyping() {
		if (suggestionHandler != null) {
			suggestionHandler.removeCallbacksAndMessages(null);
			suggestionHandler = null;
		}

		clearWordPredictions();
		mindReader.clearContext();
		super.onFinishTyping();
	}


	private String[] onAcceptPreviousSuggestion() {
		final int lastWordLength = InputModeKind.isABC(mInputMode) ? 1 : mInputMode.getSequenceLength() - 1;
		String lastWord = suggestionOps.getCurrent(mLanguage, lastWordLength);
		if (Characters.PLACEHOLDER.equals(lastWord)) {
			lastWord = "";
		}

		suggestionOps.commitCurrent(false, true);
		mInputMode.onAcceptSuggestion(lastWord, true);
		final String[] surroundingText = autoCorrectSpace(
			lastWord,
			textField.getSurroundingStringForAutoAssistance(settings, mInputMode),
			false,
			mInputMode.getFirstKey()
		);
		mInputMode.determineNextWordTextCase(surroundingText[0], -1);
		mindReader.setContext(mInputMode, mLanguage, surroundingText, lastWord);

		return surroundingText;
	}


	@Override
	protected void onAcceptSuggestionsDelayed(String word) {
		onAcceptSuggestionManually(word, -1);
		forceShowWindow();
	}


	protected void onAcceptSuggestionManually(String word, int fromKey) {
		mInputMode.onAcceptSuggestion(word);
		if (Clipboard.contains(word)) {
			Clipboard.copy(this, word);
		}

		if (!word.isEmpty()) {
			String[] surroundingText = autoCorrectSpace(
				word,
				textField.getSurroundingStringForAutoAssistance(settings, mInputMode),
				true,
				fromKey
			);

			mInputMode.determineNextWordTextCase(surroundingText[0], -1);
			updateShiftState(surroundingText[0], false, false);
			resetKeyRepeat();
			guessNextWord(surroundingText, word);
			if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
				loadWordPredictions();
			}
		}

		if (!Characters.getSpace(mLanguage).equals(word)) {
			waitForSpaceTrimKey();
		}
	}


	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
		super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
		if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
			loadWordPredictions();
		}
	}


	@Override
	protected void scrollSuggestions(boolean backward) {
		super.scrollSuggestions(backward);
		if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
			loadWordPredictions();
		}
	}


	@Override
	public boolean onBackspace(int repeat) {
		boolean result = super.onBackspace(repeat);
		if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
			loadWordPredictions();
		}
		return result;
	}


	@NonNull
	@Override
	public SuggestionOps getSuggestionOps() {
		return suggestionOps;
	}


	/**
	 * Ask the InputMode to load suggestions for the current state. No action is taken if the dictionary
	 * is still loading. Note that onComplete is called even if the loading was skipped.
	 */
	@Override
	protected void getSuggestions(double loadingId, @Nullable String currentWord, @Nullable Runnable onComplete) {
		if (InputModeKind.isPredictive(mInputMode) && DictionaryLoader.isRunning()) {
			mInputMode.reset();
			UI.toastShortSingle(this, R.string.dictionary_loading_please_wait);
			if (onComplete != null) {
				onComplete.run();
			}
		} else {
			mInputMode
				.setOnSuggestionsUpdated(() -> handleSuggestionsAsync(loadingId, onComplete))
				.loadSuggestions(currentWord == null ? suggestionOps.getCurrent() : currentWord);
		}
	}


	@WorkerThread
	protected void handleSuggestionsAsync() {
		handleSuggestionsAsync(0, null);
	}


	@WorkerThread
	protected void handleSuggestionsAsync(double loadingId, @Nullable Runnable onComplete) {
		final Handler handler = getAsyncHandler();
		handler.removeCallbacksAndMessages(null);
		handler.post(() -> handleSuggestions(loadingId, onComplete));
	}


	@MainThread
	protected void handleSuggestions(double loadingId, @Nullable Runnable onComplete) {
		// Second pass, analyze the available suggestions and decide if combining them with the
		// last key press makes up a compound word like: (it)'s, (I)'ve, l'(oiseau), or it is
		// just the end of a sentence, like: "word." or "another?"
		String[] surroundingText = null;
		if (mInputMode.shouldAcceptPreviousSuggestion(suggestionOps.getCurrent())) {
			surroundingText = onAcceptPreviousSuggestion();
		}

		final ArrayList<String> suggestions = mInputMode.getSuggestions();
		suggestionOps.set(suggestions, mInputMode.getRecommendedSuggestionIdx(), mInputMode.containsGeneratedSuggestions());

		// either accept the first one automatically (when switching from punctuation to text
		// or vice versa), or schedule auto-accept in N seconds (in ABC mode)
		int autoAcceptTimeout = mInputMode.getAutoAcceptTimeout();
		if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
			suggestionOps.setBarVisible(!suggestions.isEmpty());
		}
		if (suggestionOps.scheduleDelayedAccept(autoAcceptTimeout)) {
			if (onComplete != null) {
				onComplete.run();
			}
			return;
		}

		// We have not accepted anything yet, which means the user is composing a word.
		// put the first suggestion in the text field, but cut it off to the length of the sequence
		// (the count of key presses), for a more intuitive experience.
		String trimmedWord;

		if (InputModeKind.isRecomposing(mInputMode)) {
			// highlight the current letter, when editing a word
			trimmedWord = mInputMode.getWordStem() + suggestionOps.getCurrent();
			appHacks.setComposingTextPartsWithHighlightedJoining(trimmedWord, mInputMode.getRecomposingSuffix());
		} else {
			// or highlight the stem, when filtering
			trimmedWord = suggestionOps.getCurrent(mLanguage, mInputMode.getSequenceLength());
			appHacks.setComposingTextWithHighlightedStem(trimmedWord, mInputMode.getWordStem(), mInputMode.isStemFilterFuzzy());
		}

		if (settings.isMainLayoutSmartBar() && InputModeKind.isABC(mInputMode)) {
			loadWordPredictions();
		}

		// append guesses from the MindReader
		if (loadingId != 0 && loadingId == mindReader.getLoadingId()) {
			handleOrWaitForGuesses();
		}

		onAfterSuggestionsHandled(onComplete, surroundingText, trimmedWord, suggestions.isEmpty());
	}


	private void onAfterSuggestionsHandled(@Nullable Runnable callback, @Nullable String[] surroundingText, @Nullable String trimmedWord, boolean noSuggestions) {
		final String shiftStateContext = surroundingText != null ? surroundingText[0] + trimmedWord : trimmedWord;
		if (noSuggestions) {
			updateShiftStateDebounced(shiftStateContext, true, false);
		} else {
			updateShiftStateDebounced(shiftStateContext, false, true);
		}

		forceShowWindow();

		if (callback != null) {
			callback.run();
		}
	}


	@Override
	protected void autoCompleteOnNumber(double loadingId, @NonNull String[] surroundingText, @Nullable String lastWord, int number) {
		if (mLanguage.hasLettersOnAllKeys() || mLanguage.isTranscribed()) {
			return;
		}

		if (mLanguage.hasSpaceBetweenWords()) {
			autoCompleteOnNumberRegularLanguage(loadingId, surroundingText, lastWord, number);
		} else {
			autoCompleteOnNumberNoSpaceLanguage(loadingId, surroundingText, number);
		}
	}


	private void autoCompleteOnNumberNoSpaceLanguage(double loadingId, @NonNull String[] surroundingText, int number) {
		if (mInputMode.getSequenceLength() == 1) {
			autoCompleteWord(loadingId, surroundingText, number);
		}
	}


	private void autoCompleteOnNumberRegularLanguage(double loadingId, @NonNull String[] surroundingText, @Nullable String lastWord, int number) {
		if (mInputMode.getSequenceLength() != 1 || new Text(mLanguage, surroundingText[1]).startsWithWord()) {
			mindReader.clearContext();
			return;
		}

		if (surroundingText[0].isEmpty() || Characters.getSpace(mLanguage).equals(lastWord) || TextTools.endsWithSpace(surroundingText[0])) {
			autoCompleteWord(loadingId, surroundingText, number);
		}
	}


	private void autoCompleteWord(double loadingId, @NonNull String[] surroundingText, int number) {
		mindReader
			.setCurrentGuessHandler(null)
			.setTextCase(mInputMode.getTextCaseRaw())
			.setLanguage(mLanguage)
			.complete(loadingId, mInputMode, surroundingText, number);
	}


	@Override
	protected void guessNextWord(@NonNull String[] surroundingText, @Nullable String lastWord) {
		mindReader
			.setTextCase(mInputMode.getTextCaseRaw())
			.setLanguage(mLanguage)
			.guess(mInputMode, surroundingText, lastWord, this::handleGuessesAsync);
	}


	@WorkerThread
	private void handleGuessesAsync() {
		final Handler handler = getAsyncHandler();
		handler.removeCallbacks(this::handleGuesses);
		handler.post(this::handleGuesses);
	}


	@MainThread
	private boolean handleGuesses() {
		final ArrayList<String> guesses = mindReader.getGuesses();
		if (guesses.isEmpty()) {
			return false;
		}

		suggestionOps.cancelDelayedAccept();
		appHacks.setComposingText(guesses.get(0));
		suggestionOps.addGuesses(guesses);

		return true;
	}


	@MainThread
	private void handleOrWaitForGuesses() {
		if (!handleGuesses()) {
			mindReader.setCurrentGuessHandler(this::handleGuessesAsync);
		}
	}


	@Override
	protected boolean shouldAcceptGuessesOnNumber(int number) {
		return number == 0 && !mLanguage.hasLettersOnAllKeys() && suggestionOps.containsOnlyGuesses();
	}


	private void loadWordPredictions() {
		if (mLanguage == null) return;
		// Use sync read so this call is ordered after any prior setComposingText (same binder thread).
		String prefix = extractCurrentWord(textField.getStringBeforeCursorSync(50));
		if (prefix.isEmpty()) {
			clearWordPredictions();
			return;
		}
		final String prefixLower = prefix.toLowerCase(mLanguage.getLocale());
		DataStore.getWordsByPrefix(
			predictions -> new Handler(Looper.getMainLooper()).post(() -> {
				View bar = getWordPredictionsBar();
				if (bar == null) return;
				if (!predictions.isEmpty()) {
					showWordPredictions(bar, predictions);
				} else {
					clearWordPredictions(bar);
				}
			}),
			mLanguage,
			prefixLower,
			3
		);
	}


	private void showWordPredictions(@NonNull View bar, @NonNull ArrayList<String> words) {
		String w0 = words.size() > 0 ? words.get(0) : "";
		String w1 = words.size() > 1 ? words.get(1) : "";
		String w2 = words.size() > 2 ? words.get(2) : "";
		TextView t0 = bar.findViewById(R.id.word_prediction_0);
		TextView t1 = bar.findViewById(R.id.word_prediction_1);
		TextView t2 = bar.findViewById(R.id.word_prediction_2);
		int textColor = settings.getKeyboardTextColor();
		if (t0 != null) { t0.setText(w0); t0.setTextColor(textColor); t0.setOnClickListener(w0.isEmpty() ? null : v -> acceptWordPrediction(w0)); }
		if (t1 != null) { t1.setText(w1); t1.setTextColor(textColor); t1.setOnClickListener(w1.isEmpty() ? null : v -> acceptWordPrediction(w1)); }
		if (t2 != null) { t2.setText(w2); t2.setTextColor(textColor); t2.setOnClickListener(w2.isEmpty() ? null : v -> acceptWordPrediction(w2)); }

		boolean wasGone = bar.getVisibility() != View.VISIBLE;
		bar.setVisibility(View.VISIBLE);
		if (wasGone) mainView.updateHeight();
	}


	protected void clearWordPredictions() {
		View bar = getWordPredictionsBar();
		if (bar != null) clearWordPredictions(bar);
	}


	private void clearWordPredictions(@NonNull View bar) {
		TextView t0 = bar.findViewById(R.id.word_prediction_0);
		TextView t1 = bar.findViewById(R.id.word_prediction_1);
		TextView t2 = bar.findViewById(R.id.word_prediction_2);
		if (t0 != null) { t0.setText(""); t0.setOnClickListener(null); }
		if (t1 != null) { t1.setText(""); t1.setOnClickListener(null); }
		if (t2 != null) { t2.setText(""); t2.setOnClickListener(null); }

		boolean wasVisible = bar.getVisibility() == View.VISIBLE;
		bar.setVisibility(View.GONE);
		if (wasVisible) mainView.updateHeight();
	}


	@Nullable
	private View getWordPredictionsBar() {
		View root = mainView != null ? mainView.getView() : null;
		return root != null ? root.findViewById(R.id.word_predictions_bar) : null;
	}


	protected void acceptWordPrediction(@NonNull String word) {
		clearWordPredictions();

		// Clear composing first so getSurroundingString doesn't include it in the prefix
		String composing = textField.getComposingText();
		if (!composing.isEmpty()) {
			appHacks.setComposingText("");
			textField.finishComposingText();
		}

		String[] surrounding = textField.getSurroundingStringForAutoAssistance(settings, mInputMode);
		String prefix = extractCurrentWord(surrounding[0]);

		if (!prefix.isEmpty()) {
			textField.deleteChars(mLanguage, prefix.length());
		}

		// Insert the word (mirroring suggestionOps.commitCurrent which runs before onAcceptSuggestionManually)
		appHacks.setComposingText(matchPrefixCase(mLanguage, prefix, word));
		textField.finishComposingText();

		onAcceptSuggestionManually(word, KeyEvent.KEYCODE_ENTER);
	}


	private static String matchPrefixCase(@Nullable com.tt9smart.languages.Language language, @NonNull String prefix, @NonNull String word) {
		if (prefix.isEmpty() || language == null) return word;
		java.util.Locale locale = language.getLocale();
		if (prefix.equals(prefix.toUpperCase(locale))) return word.toUpperCase(locale);
		if (Character.isUpperCase(prefix.charAt(0))) return Character.toUpperCase(word.charAt(0)) + word.substring(1);
		return word;
	}


	private String extractCurrentWord(String textBefore) {
		return WordPredictionUtils.extractCurrentWord(textBefore);
	}
}
