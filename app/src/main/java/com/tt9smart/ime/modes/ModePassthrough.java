package com.tt9smart.ime.modes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.hacks.InputType;
import com.tt9smart.preferences.settings.SettingsStore;

// see: InputType.isSpecialNumeric()
class ModePassthrough extends InputMode {
	protected ModePassthrough(@Nullable SettingsStore settings, @Nullable InputType inputType) {
		super(settings, inputType);
		reset();
	}

	@Override public int getId() { return MODE_PASSTHROUGH; }
	@Override public int getSequenceLength() { return 0; }
	@Override @NonNull public String toString() { return "--"; }

	@Override public boolean onNumber(int n, boolean h, int r, @NonNull String[] s) { return false; }
	@Override public boolean shouldIgnoreText(String t) { return true; }
}
