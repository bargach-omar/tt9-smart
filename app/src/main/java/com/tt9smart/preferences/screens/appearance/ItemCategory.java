package com.tt9smart.preferences.screens.appearance;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

import com.tt9smart.preferences.settings.SettingsStore;

public record ItemCategory(@Nullable PreferenceCategory item) implements ItemLayoutChangeReactive {
	@Override
	public void onLayoutChange(int mainViewLayout) {
		if (item != null) {
			item.setVisible(mainViewLayout != SettingsStore.LAYOUT_STEALTH);
		}
	}
}
