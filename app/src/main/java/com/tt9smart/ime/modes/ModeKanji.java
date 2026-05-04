package com.tt9smart.ime.modes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.tt9smart.hacks.InputType;
import com.tt9smart.ime.helpers.TextField;
import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageKind;
import com.tt9smart.preferences.settings.SettingsStore;

public class ModeKanji extends ModePinyin {
	protected ModeKanji(@NonNull SettingsStore settings, @NonNull Language lang, @Nullable InputType inputType, @Nullable TextField textField) {
		super(settings, lang, inputType, textField);
		NAME = language.getName().replace(" / ローマ字", "");
	}


	@NonNull
	@Override
	public ArrayList<String> getSuggestions() {
		ArrayList<String> snapshot = suggestions;
		ArrayList<String> newSuggestions = new ArrayList<>(snapshot.size());
		for (String s : snapshot) {
			// "Ql" is the transcription of "—" in the database, as defined in Japanese.yml. However, this
			// has only technical meaning. When displaying the suggestions, we want to show "—" for better
			// readability.
			newSuggestions.add(s.replaceAll("Ql", "—"));
		}

		return newSuggestions;
	}


	@Override
	public boolean onReplaceSuggestion(@NonNull String word) {
		// revert to the transcription, so that filtering works correctly
		return super.onReplaceSuggestion(word.replaceAll("—", "Ql"));
	}


	@Override
	public boolean validateLanguage(@Nullable Language newLanguage) {
		return LanguageKind.isJapanese(newLanguage);
	}
}
