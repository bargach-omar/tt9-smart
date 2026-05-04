package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.PreferencesActivity;

record ItemSelectLanguage(PreferencesActivity activity, Preference item) {
	public static final String NAME = "pref_languages";

	public ItemSelectLanguage populate() {
		previewSelection();
		return this;
	}


	private void previewSelection() {
		if (item == null) {
			return;
		}

		item.setSummary(
			LanguageCollection.toString(LanguageCollection.getAll(activity.getSettings().getEnabledLanguageIds(), true))
		);
	}
}
