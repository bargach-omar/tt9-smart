package com.tt9smart.preferences.screens.setup;

import android.content.Intent;
import android.provider.Settings;

import androidx.preference.Preference;

import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;

class ItemSelectGlobalKeyboard extends ItemClickable {
	private final Intent clickIntent;
	private final PreferencesActivity activity;

	ItemSelectGlobalKeyboard(Preference item, PreferencesActivity prefs) {
		super(item);
		this.activity = prefs;

		clickIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
		clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		clickIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		clickIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	}

	@Override
	protected boolean onClick(Preference p) {
		activity.startActivity(clickIntent);
		return false;
	}
}
