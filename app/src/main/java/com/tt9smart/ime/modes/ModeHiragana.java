package com.tt9smart.ime.modes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.hacks.InputType;
import com.tt9smart.ime.helpers.TextField;
import com.tt9smart.ime.modes.predictions.KanaPredictions;
import com.tt9smart.languages.Language;
import com.tt9smart.preferences.settings.SettingsStore;

public class ModeHiragana extends ModeKanji {
	protected ModeHiragana(@NonNull SettingsStore settings, @NonNull Language lang, @Nullable InputType inputType, @Nullable TextField textField) {
		super(settings, lang, inputType, textField);
		NAME = "ひらがな";
	}

	@Override
	protected void initPredictions() {
		predictions = new KanaPredictions(settings, false);
		predictions.setWordsChangedHandler(this::onPredictions);
	}

	@Override
	public int getId() {
		return MODE_HIRAGANA;
	}
}
