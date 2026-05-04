package com.tt9smart.preferences.screens.appearance;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.preferences.settings.SettingsStore;

public class ItemSuggestionSmoothScroll extends ItemClickable {
	public static final String NAME = "pref_suggestion_smooth_scroll";
	private final SettingsStore settings;

	public ItemSuggestionSmoothScroll(Preference item, SettingsStore settings) {
		super(item);
		this.settings = settings;
	}

	@Override protected boolean onClick(Preference p) { return true; }

	public ItemSuggestionSmoothScroll populate() {
		if (item != null) {
			((SwitchPreferenceCompat) item).setChecked(settings.getSuggestionSmoothScroll());
		}

		return this;
	}
}
