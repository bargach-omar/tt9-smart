package com.tt9smart.preferences.screens.debug;

import androidx.preference.Preference;

import com.tt9smart.db.customWords.LogcatExporter;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemExportAbstract;
import com.tt9smart.ui.notifications.DictionaryProgressNotification;

public class ItemExportLogcat extends ItemExportAbstract {
	public static final String NAME = "pref_export_logcat";

	public ItemExportLogcat(Preference item, PreferencesActivity activity) {
		super(item, activity, null, null);
	}

	@Override
	protected LogcatExporter getProcessor() {
		return LogcatExporter.getInstance();
	}

	@Override
	protected boolean onStartProcessing() {
		return getProcessor().setIncludeSystemLogs(activity.getSettings().getEnableSystemLogs()).run(activity);
	}

	@Override
	protected void onFinishProcessing(String outputFile) {
		activity.runOnUiThread(() -> {
			DictionaryProgressNotification.getInstance(activity).hide();
			setAndNotifyReady();

			if (outputFile == null) {
				item.setSummary("Export failed");
			} else {
				item.setSummary("Logs exported to: " + outputFile);
			}
		});
	}
}
