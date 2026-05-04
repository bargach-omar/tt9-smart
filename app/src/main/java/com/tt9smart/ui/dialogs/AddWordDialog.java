package com.tt9smart.ui.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.db.DataStore;
import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.languages.Language;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.ui.UI;

public class AddWordDialog extends PopupDialog {
	@NonNull private final Language language;
	@NonNull private final SettingsStore settings;
	@Nullable private final String word;


	public AddWordDialog(@NonNull TraditionalT9 tt9, @NonNull Language language, @Nullable String word) {
		super(tt9, R.style.TTheme_AddWord);

		title = tt9.getResources().getString(R.string.add_word_title);
		OKLabel = tt9.getResources().getString(R.string.add_word_add);
		message = tt9.getString(R.string.add_word_confirm, word, language.getName());
		settings = tt9.getSettings();

		this.language = language;
		this.word = word;
	}

	private void onOK() {
		close();
		DataStore.put(
			(result) -> UI.toastLongFromAsync(context, result.toHumanFriendlyString(context)),
			language,
			word
		);
	}


	public void show() {
		if (word == null || word.isEmpty()) {
			UI.toastLong(context, R.string.add_word_no_selection);
			close();
			return;
		}

		if (settings.getAddWordsNoConfirmation()) {
			onOK();
			return;
		}

		if (!render(this::onOK, null, null)) {
			onOK();
		}
	}
}
