package com.tt9smart.preferences.screens.main;

import androidx.preference.Preference;

import com.tt9smart.BuildConfig;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.preferences.screens.debug.DebugScreen;

class ItemVersionInfo extends ItemClickable {
	static final String NAME = "version_info";

	private final PreferencesActivity activity;

	ItemVersionInfo(Preference item, PreferencesActivity activity) {
		super(item);
		this.activity = activity;
	}

	@Override
	protected boolean onClick(Preference p) {
		if (!activity.getSettings().getDemoMode()) {
			activity.displayScreen(DebugScreen.NAME);
		}

		return true;
	}

	ItemVersionInfo populate() {
		if (item != null) {
			item.setSummary(BuildConfig.VERSION_FULL);
		}
		return this;
	}
}
