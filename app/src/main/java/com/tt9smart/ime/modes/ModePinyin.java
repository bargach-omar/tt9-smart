package com.tt9smart.ime.modes;

import androidx.annotation.Nullable;

import com.tt9smart.hacks.InputType;
import com.tt9smart.ime.helpers.TextField;
import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageKind;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.util.chars.Characters;

public class ModePinyin extends ModeIdeograms {
	protected ModePinyin(SettingsStore settings, Language lang, InputType inputType, TextField textField) {
		super(settings, lang, inputType, textField);
	}


	@Override
	public boolean validateLanguage(@Nullable Language newLanguage) {
		return LanguageKind.isChinesePinyin(newLanguage);
	}


	@Override
	protected String getPreferredChar() {
		return Characters.getChar(language, settings.getDoubleZeroChar());
	}
}
