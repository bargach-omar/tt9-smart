package com.tt9smart.preferences;

import com.tt9smart.db.customWords.LogcatExporter;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.ui.WebViewActivity;

public class LogsActivity extends WebViewActivity {
	@Override
	protected String getMimeType() {
		return "text/plain";
	}

	@Override
	protected String getText() {
		boolean includeSystemLogs = new SettingsStore(this).getSystemLogs();
		String logs = LogcatExporter.getLogs(includeSystemLogs).replace("\n", "\n\n");
		return logs.isEmpty() ? "No Logs" : logs;
	}
}
