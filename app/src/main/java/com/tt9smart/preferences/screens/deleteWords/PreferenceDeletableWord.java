package com.tt9smart.preferences.screens.deleteWords;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;

import com.tt9smart.R;
import com.tt9smart.db.DataStore;
import com.tt9smart.db.entities.CustomWord;
import com.tt9smart.languages.Language;
import com.tt9smart.preferences.custom.ScreenPreference;
import com.tt9smart.ui.PopupBuilder;
import com.tt9smart.ui.UI;
import com.tt9smart.util.Logger;

public class PreferenceDeletableWord extends ScreenPreference {
	private DeletableWordsList parent;
	private Language language;
	private String word;


	public PreferenceDeletableWord(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }
	public PreferenceDeletableWord(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
	public PreferenceDeletableWord(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); }
	public PreferenceDeletableWord(@NonNull Context context) { super(context); }


	@Override public int getDefaultLayout() { return R.layout.pref_deletable_word; }
	@Override public int getLargeLayout() { return R.layout.pref_deletable_word_large; }


	void setParent(DeletableWordsList parent) {
		if (parent != null) {
			this.parent = parent;
		}
	}


	void setWord(CustomWord word) {
		this.word = word.word;
		this.language = word.language;
		setTitle(
			Logger.isDebugLevel() ? word.word + " / " + word.sequence + " / " + word.language.getName() : word.word + " / " + word.language.getName()
		);
	}


	@Override
	protected void onClick() {
		super.onClick();

		Context context = getContext();
		new PopupBuilder(context)
			.setCancelable(true)
			.setTitle(context.getString(R.string.delete_words_deleted_confirm_deletion_title))
			.setMessage(context.getString(R.string.delete_words_deleted_confirm_deletion_question, word))
			.setNegativeButton(true, null)
			.setPositiveButton(context.getString(R.string.delete), this::onDeletionConfirmed)
			.show();
	}


	private void onDeletionConfirmed() {
		DataStore.deleteCustomWord(this::onWordDeleted, language, word);
	}


	private void deleteSelf() {
		if (parent != null) {
			parent.delete(this);
		} else if (getParent() instanceof PreferenceCategory) {
			getParent().removePreference(this);
		}

		UI.toastFromAsync(getContext(), getContext().getString(R.string.delete_words_deleted_x, word));
	}


	private void onWordDeleted() {
		((Activity) getContext()).runOnUiThread(this::deleteSelf);
	}
}
