package com.tt9smart.preferences.screens.fnKeyOrder;

import androidx.preference.Preference;

import com.tt9smart.preferences.items.ItemClickable;
import com.tt9smart.preferences.settings.SettingsVirtualNumpad;

public class ItemResetFnKeyOrder extends ItemClickable {
	public final static String NAME = "pref_reset_fn_key_order";

	private final LeftFnOrderPreference left;
	private final RightFnOrderPreference right;

	public ItemResetFnKeyOrder(Preference item, LeftFnOrderPreference left, RightFnOrderPreference right) {
		super(item);
		this.left = left;
		this.right = right;
	}

	@Override
	protected boolean onClick(Preference p) {
		if (left == null || right == null) {
			return false;
		}

		left.setText(SettingsVirtualNumpad.DEFAULT_LFN_KEY_ORDER);
		right.setText(SettingsVirtualNumpad.DEFAULT_RFN_KEY_ORDER);
		left.onTextChange();

		return true;
	}
}
