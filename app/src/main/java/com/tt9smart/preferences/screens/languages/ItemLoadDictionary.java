package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import java.util.ArrayList;

import com.tt9smart.R;
import com.tt9smart.db.words.DictionaryLoader;
import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.ui.UI;
import com.tt9smart.ui.notifications.DictionaryLoadingBar;


class ItemLoadDictionary extends ItemClickable {
	public final static String NAME = "dictionary_load";

	private final PreferencesActivity activity;
	private final Runnable onStart;
	private final Runnable onFinish;

	private final DictionaryLoadingBar progressBar;


	ItemLoadDictionary(Preference item, PreferencesActivity context, Runnable onStart, Runnable onFinish) {
		super(item);

		progressBar = DictionaryLoadingBar.getInstance(context);

		this.activity = context;
		this.onStart = onStart;
		this.onFinish = onFinish;
	}


	public void refreshStatus() {
		if (DictionaryLoader.isRunning()) {
			setBusy();
		} else {
			setReady();
		}
	}


	private void onLoadingStatusChange() {
		item.setSummary(progressBar.getTitle() + " " + progressBar.getMessage());

		if (progressBar.isCancelled()) {
			setReady();
			onFinish.run();
		} else if (progressBar.isFailed()) {
			setReady();
			onFinish.run();
			UI.toastFromAsync(activity, progressBar.getMessage());
		} else if (!progressBar.inProgress()) {
			setReady();
			onFinish.run();
			UI.toastFromAsync(activity, R.string.dictionary_loaded);
		}
	}


	@Override
	protected boolean onClick(Preference p) {
		ArrayList<Language> languages = LanguageCollection.getAll(activity.getSettings().getEnabledLanguageIds());

		setBusy();
		if (!DictionaryLoader.load(activity, languages)) {
			DictionaryLoader.abort();
			setReady();
			onFinish.run();
		}

		return true;
	}


	private void setBusy() {
		progressBar.setOnStatusChange(this::onLoadingStatusChange);
		onStart.run();
		item.setTitle(activity.getString(R.string.dictionary_cancel_load));
	}


	private void setReady() {
		progressBar.setOnStatusChange(null);
		item.setTitle(activity.getString(R.string.dictionary_load_title));
		item.setSummary(progressBar.isFailed() || progressBar.isCancelled() ? progressBar.getMessage() : "");
	}
}
