package com.tt9smart.preferences.screens.punctuation;

import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.tt9smart.R;
import com.tt9smart.languages.Language;
import com.tt9smart.preferences.items.ItemClickable;

public class ItemPunctuationOrderSave extends ItemClickable {
	public static final String NAME = "punctuation_order_save";
	private final Runnable clickHandler;

	public ItemPunctuationOrderSave(Preference item, Runnable clickHandler) {
		super(item);
		this.clickHandler = clickHandler;
	}

	ItemPunctuationOrderSave setLanguage(@Nullable Language language) {
		if (item != null && language != null) {
			item.setTitle(item.getContext().getString(R.string.punctuation_order_save, language.getName()));
		}
		return this;
	}

	@Override
	protected boolean onClick(Preference p) {
		if (clickHandler == null) {
			return false;
		}

		clickHandler.run();
		return true;
	}
}
