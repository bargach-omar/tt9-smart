package com.tt9smart.preferences.screens.keypad;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import com.tt9smart.R;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.screens.BaseScreenFragment;

public class KeyPadScreen extends BaseScreenFragment {
	final public static String NAME = "KeyPad";

	public KeyPadScreen() { super(); }
	public KeyPadScreen(@Nullable PreferencesActivity activity) { super(activity); }

	@Override public String getName() { return NAME; }
	@Override protected int getTitle() { return R.string.pref_category_keypad; }
	@Override protected int getXml() { return R.xml.prefs_screen_keypad; }

	@Override
	protected void onCreate() {
		createPhysicalKeysSection();
		createVirtualKeysSection();
		resetFontSize(true);
	}

	private void createPhysicalKeysSection() {
		Preference debounceTime = findPreference(DropDownKeyPadDebounceTime.NAME);
		if (debounceTime instanceof DropDownKeyPadDebounceTime && activity != null) {
			((DropDownKeyPadDebounceTime) debounceTime).populate(activity.getSettings()).preview();
		}
	}

	protected void createVirtualKeysSection() {
		if (activity == null) {
			return;
		}

		// hide the entire category when the settings show no interest in it
		final boolean isVisible = activity.getSettings().isMainLayoutSmall();
		final PreferenceCategory category = findPreference("category_virtual_keys");
		if (category != null) {
			category.setVisible(isVisible);
		}
	}
}
