package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import com.tt9smart.db.customWords.DictionaryExporter;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemExportAbstract;
import com.tt9smart.preferences.items.ItemProcessCustomWordsAbstract;
import com.tt9smart.util.Logger;

class ItemExportDictionary extends ItemExportAbstract {
	final public static String NAME = "dictionary_export";

	ItemExportDictionary(Preference item, PreferencesActivity activity, Runnable onStart, Runnable onFinish) {
		super(item, activity, onStart, onFinish);
	}

	@Override
	public ItemProcessCustomWordsAbstract refreshStatus() {
		if (item != null) {
			item.setVisible(Logger.isDebugLevel());
		}
		return super.refreshStatus();
	}

	@Override
	protected DictionaryExporter getProcessor() {
		return DictionaryExporter.getInstance();
	}

	protected boolean onStartProcessing() {
		return DictionaryExporter.getInstance()
			.setLanguages(LanguageCollection.getAll(activity.getSettings().getEnabledLanguageIds()))
			.run(activity);
	}

	public void enable() {
		super.enable();
		item.setSummary("");
	}
}
