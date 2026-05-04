package com.tt9smart.preferences;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.BuildConfig;
import com.tt9smart.util.AssetFile;

public class PrivacyPolicyFile extends AssetFile {
	public PrivacyPolicyFile(@NonNull Context context) {
		super(context.getAssets(), BuildConfig.DOCS_DIR + "/privacy.html");
	}
}
