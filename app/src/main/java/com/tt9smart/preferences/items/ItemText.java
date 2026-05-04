package com.tt9smart.preferences.items;

import androidx.preference.Preference;

import com.tt9smart.R;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.ui.UI;
import com.tt9smart.util.sys.Clipboard;
import com.tt9smart.util.sys.DeviceInfo;

public class ItemText extends ItemClickable {
	private final PreferencesActivity activity;
	public ItemText(PreferencesActivity activity, Preference preference) {
		super(preference);

		this.activity = activity;
	}

	@Override
	protected boolean onClick(Preference p) {
		if (activity == null || p.getSummary() == null) {
			return false;
		}

		Clipboard.copy(
			activity,
			activity.getString(R.string.app_name_short) + " / " + item.getTitle(),
			p.getSummary()
		);

		if (!DeviceInfo.AT_LEAST_ANDROID_13) {
			UI.toast(activity, "\"" + Clipboard.getLastPreview() + "\" copied.");
		}

		return true;
	}

	public ItemText populate(String text) {
		if (item != null) {
			item.setSummary(text);
		}

		return this;
	}
}
