package com.tt9smart.preferences.screens.appearance;

import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.preferences.settings.SettingsStore;

public record ItemStatusIcon(SwitchPreferenceCompat item, SettingsStore settings) {
	public static final String NAME = "pref_status_icon";

	public void populate() {
		if (item != null) {
			item.setChecked(settings.isStatusIconEnabled());
		}
	}
}
