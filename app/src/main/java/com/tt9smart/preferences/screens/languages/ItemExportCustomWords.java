package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import com.tt9smart.R;
import com.tt9smart.db.customWords.CustomWordsExporter;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemExportAbstract;

class ItemExportCustomWords extends ItemExportAbstract {
	final public static String NAME = "dictionary_export_custom";

	ItemExportCustomWords(Preference item, PreferencesActivity activity, Runnable onStart, Runnable onFinish) {
		super(item, activity, onStart, onFinish);
	}

	@Override
	protected CustomWordsExporter getProcessor() {
		return CustomWordsExporter.getInstance();
	}

	protected boolean onStartProcessing() {
		return CustomWordsExporter.getInstance().run(activity);
	}

	public void enable() {
		super.enable();
		item.setSummary(activity.getString(
			R.string.dictionary_export_custom_words_summary,
			CustomWordsExporter.getInstance().getOutputDir()
		));
	}
}
