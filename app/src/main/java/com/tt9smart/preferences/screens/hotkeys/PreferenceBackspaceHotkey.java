package com.tt9smart.preferences.screens.hotkeys;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.tt9smart.commands.Command;
import com.tt9smart.preferences.settings.SettingsStore;

public class PreferenceBackspaceHotkey extends PreferenceHotkey {
	public PreferenceBackspaceHotkey(@NonNull Context context, @NonNull SettingsStore settings, @NonNull Command command) {
		super(context, settings, command);
	}

	@Override
	protected boolean onAssign(DialogInterface dialog, int keyCode) {
		// backspace works both when pressed short and long,
		// so separate "hold" and "not hold" options for it make no sense
		return super.onAssign(dialog, Math.abs(keyCode));
	}
}
