package com.tt9smart.preferences.screens.hotkeys;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.commands.Command;
import com.tt9smart.ime.voice.VoiceInputOps;
import com.tt9smart.preferences.settings.SettingsStore;

public class PreferenceVoiceInputHotkey extends PreferenceHotkey {
	public PreferenceVoiceInputHotkey(@NonNull Context context, @NonNull SettingsStore settings, @NonNull Command command) {
		super(context, settings, command);
	}

	@Override
	public void populate() {
		boolean isAvailable = new VoiceInputOps(getContext(), null, null, null, null).isAvailable();
		setVisible(isAvailable);
		if (isAvailable) {
			super.populate();
		}
	}
}
