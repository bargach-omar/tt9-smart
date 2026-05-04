package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.PreferencesActivity;


class ItemTruncateUnselected extends ItemTruncateAll {
	public static final String NAME = "dictionary_truncate_unselected";


	ItemTruncateUnselected(Preference item, PreferencesActivity context, Runnable onStart, Runnable onFinish) {
		super(item, context, onStart, onFinish);
	}


	@Override
	protected boolean onClick(Preference p) {
		ArrayList<Language> unselectedLanguages = new ArrayList<>();
		Set<Integer> selectedLanguageIds = new HashSet<>(activity.getSettings().getEnabledLanguageIds());
		for (Language lang : LanguageCollection.getAll(false)) {
			if (!selectedLanguageIds.contains(lang.getId())) {
				unselectedLanguages.add(lang);
			}
		}

		setBusy();
		deleter.setOnFinish(this::onFinishDeleting);
		deleter.deleteLanguages(unselectedLanguages);

		return true;
	}
}
