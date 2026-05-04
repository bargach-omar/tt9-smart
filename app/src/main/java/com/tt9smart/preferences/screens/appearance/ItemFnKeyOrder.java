package com.tt9smart.preferences.screens.appearance;

import androidx.annotation.NonNull;
import androidx.preference.Preference;

import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.preferences.settings.SettingsStore;

public class ItemFnKeyOrder extends ItemClickable implements ItemLayoutChangeReactive {
	public static final String NAME = "screen_fn_key_order";

	@NonNull private final SettingsStore settings;

	public ItemFnKeyOrder(@NonNull SettingsStore settings, Preference item) {
		super(item);
		this.settings = settings;
		onLayoutChange(0);
	}

	@Override protected boolean onClick(Preference p) { return true; }

	public void onLayoutChange(int ignored) {
		if (item != null) {
			item.setVisible(settings.isFnKeyOrderEnabled());
			item.setIconSpaceReserved(false);
		}
	}
}
