package com.tt9smart.preferences.screens.appearance;

import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.preferences.items.ItemSwitch;
import com.tt9smart.preferences.settings.SettingsStore;

public class ItemAlternativeSuggestionScrolling extends ItemSwitch {
	public static final String NAME = "pref_alternative_suggestion_scrolling";

	private final SettingsStore settings;

	public ItemAlternativeSuggestionScrolling(SwitchPreferenceCompat item, SettingsStore settings) {
		super(item);
		this.settings = settings;
	}

	@Override
	protected boolean getDefaultValue() {
		return settings.getSuggestionScrollingDelay() > 0;
	}
}
