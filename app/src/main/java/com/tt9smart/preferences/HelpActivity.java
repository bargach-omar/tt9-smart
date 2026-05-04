package com.tt9smart.preferences;

import java.io.BufferedReader;
import java.io.IOException;

import com.tt9smart.ui.DocumentActivity;
import com.tt9smart.util.sys.SystemSettings;

public class HelpActivity extends DocumentActivity {
	@Override
	protected BufferedReader getDocumentReader() throws IOException {
		String systemLanguage = SystemSettings.getLocale().replaceFirst("_\\w+$", "");
		HelpFile file = new HelpFile(this, systemLanguage);
		file = file.exists() ? file : new HelpFile(this);
		return file.getReader();
	}
}
