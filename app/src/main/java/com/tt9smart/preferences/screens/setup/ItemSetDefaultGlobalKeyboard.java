package com.tt9smart.preferences.screens.setup;

import androidx.preference.Preference;

import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.ui.UI;

class ItemSetDefaultGlobalKeyboard extends ItemClickable {
	private final PreferencesActivity activity;

	ItemSetDefaultGlobalKeyboard(Preference item, PreferencesActivity prefs) {
		super(item);
		this.activity = prefs;
	}

	@Override
	protected boolean onClick(Preference p) {
		UI.showChangeKeyboardDialog(activity);
		return false;
	}
}
