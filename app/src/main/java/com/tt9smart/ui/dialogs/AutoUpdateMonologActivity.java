package com.tt9smart.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tt9smart.db.words.DictionaryLoader;
import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.util.Logger;

public class AutoUpdateMonologActivity extends AppCompatActivity {
	public static final String PARAMETER_LANGUAGE = "lang";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processIntent(getIntent());
		finish();
	}

	private void processIntent(@NonNull Intent intent) {
		LanguageCollection.init(this);
		int languageId = intent.getIntExtra(PARAMETER_LANGUAGE, -1);
		Language language = LanguageCollection.getLanguage(languageId);

		if (language == null) {
			Logger.e(getClass().getSimpleName(), "Auto-updating is not possible. Intent parameter '" + PARAMETER_LANGUAGE + "' is invalid: " + languageId);
		} else {
			DictionaryLoader.load(this, language);
		}
	}

	public static Intent generateShowIntent(Context context, int language) {
		Intent intent = new Intent(context, AutoUpdateMonologActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(PARAMETER_LANGUAGE, language);

		return intent;
	}
}
