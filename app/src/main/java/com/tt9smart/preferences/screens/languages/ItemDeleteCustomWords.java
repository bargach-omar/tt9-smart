package com.tt9smart.preferences.screens.languages;

import androidx.preference.Preference;

import com.tt9smart.preferences.items.ItemClickable;

class ItemDeleteCustomWords extends ItemClickable {
	final static String NAME = "screen_delete_words";
	ItemDeleteCustomWords(Preference item) { super(item); }
	@Override protected boolean onClick(Preference p) { return false; }
}
