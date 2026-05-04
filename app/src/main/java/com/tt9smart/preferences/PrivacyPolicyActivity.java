package com.tt9smart.preferences;

import java.io.BufferedReader;
import java.io.IOException;

import com.tt9smart.ui.DocumentActivity;

public class PrivacyPolicyActivity extends DocumentActivity {
	@Override
	protected BufferedReader getDocumentReader() throws IOException {
		return new PrivacyPolicyFile(this).getReader();
	}
}
