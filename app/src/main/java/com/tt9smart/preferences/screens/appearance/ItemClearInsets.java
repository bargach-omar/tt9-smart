package com.tt9smart.preferences.screens.appearance;

import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.R;
import com.tt9smart.preferences.items.ItemSwitch;
import com.tt9smart.preferences.settings.SettingsStore;

public class ItemClearInsets extends ItemSwitch {
	public static final String NAME = "pref_clear_insets";
	private final SettingsStore settings;

	public ItemClearInsets(SwitchPreferenceCompat item, SettingsStore settings) {
		super(item);
		this.settings = settings;
	}

	@Override
	protected boolean getDefaultValue() {
		return settings.clearInsets();
	}

	@Override
	public ItemSwitch populate() {
		if (item != null) {
			String appName = item.getContext().getString(R.string.app_name_short);
			item.setSummary(item.getContext().getString(R.string.pref_hack_always_on_top_summary, appName));
		}

		return super.populate();
	}
}
