package com.tt9smart.preferences.screens.debug;

import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;

class ItemDemoMode extends ItemClickable {
	public static final String NAME = "pref_demo_mode";

	private final PreferencesActivity activity;

	ItemDemoMode(Preference item, PreferencesActivity activity) {
		super(item);
		this.activity = activity;
	}

	@Override
	protected boolean onClick(Preference p) {
		activity.getSettings().setDemoMode(((SwitchPreferenceCompat) p).isChecked());
		activity.getOnBackPressedDispatcher().onBackPressed();
		return true;
	}

	ItemDemoMode populate() {
		if (item != null) {
			((SwitchPreferenceCompat) item).setChecked(activity.getSettings().getDemoMode());
		}

		return this;
	}
}
