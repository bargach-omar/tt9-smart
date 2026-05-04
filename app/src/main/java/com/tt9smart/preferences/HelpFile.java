package com.tt9smart.preferences;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.BuildConfig;
import com.tt9smart.util.AssetFile;

public class HelpFile extends AssetFile {
	public HelpFile(@NonNull Context context, String language) {
		super(context.getAssets(), BuildConfig.DOCS_DIR + "/help." + language + ".html");
	}

	public HelpFile(@NonNull Context context) {
		super(context.getAssets(), BuildConfig.DOCS_DIR + "/help.en.html");
	}
}
