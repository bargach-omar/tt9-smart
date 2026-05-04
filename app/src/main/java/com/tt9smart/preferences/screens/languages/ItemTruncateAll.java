package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import com.tt9smart.R;
import com.tt9smart.db.words.DictionaryDeleter;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.ui.UI;


class ItemTruncateAll extends ItemClickable {
	public static final String NAME = "dictionary_truncate";

	protected final PreferencesActivity activity;
	protected final DictionaryDeleter deleter;
	private final Runnable onStart;
	private final Runnable onFinish;


	ItemTruncateAll(Preference item, PreferencesActivity activity, Runnable onStart, Runnable onFinish) {
		super(item);
		this.activity = activity;
		this.deleter = DictionaryDeleter.getInstance(activity);
		this.onStart = onStart;
		this.onFinish = onFinish;
	}


	@Override
	protected boolean onClick(Preference p) {
		onStart.run();
		setBusy();
		deleter.deleteLanguages(LanguageCollection.getAll(false));

		return true;
	}


	void refreshStatus() {
		if (deleter.isRunning()) {
			setBusy();
		} else {
			enable();
		}
	}


	protected void setBusy() {
		deleter.setOnFinish(this::onFinishDeleting);
		item.setSummary(R.string.dictionary_truncating);
		disable();
	}


	public void enable() {
		super.enable();
		item.setSummary("");
	}


	protected void onFinishDeleting() {
		activity.runOnUiThread(() -> {
			onFinish.run();
			enable();
			UI.toastFromAsync(activity, R.string.dictionary_truncated);
		});
	}
}
