package com.tt9smart.preferences.screens.punctuation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import java.util.function.Consumer;

import com.tt9smart.R;
import com.tt9smart.languages.Language;
import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.preferences.settings.SettingsStore;

class ItemRestoreDefaultPunctuation extends ItemClickable {
	public static final String NAME = "punctuation_order_reset_defaults";

	private Language language;
	private final Consumer<String> onClick;
	private final SettingsStore settings;

	ItemRestoreDefaultPunctuation(@NonNull SettingsStore settings, Preference item, Consumer<String> onClick) {
		super(item);
		this.onClick = onClick;
		this.settings = settings;
	}

	ItemRestoreDefaultPunctuation setLanguage(@Nullable Language language) {
		this.language = language;
		if (item != null && language != null) {
			item.setTitle(item.getContext().getString(R.string.punctuation_order_restore_default_for, language.getName()));
		}
		return this;
	}

	@Override
	protected boolean onClick(Preference p) {
		if (language == null) {
			return false;
		}

		settings.setDefaultCharOrder(language, true);

		if (onClick != null) {
			onClick.accept(String.valueOf(language.getId()));
		}

		return true;
	}
}
