package com.tt9smart.preferences.screens.main;

import androidx.annotation.Nullable;
import androidx.preference.Preference;

import java.util.ArrayList;
import java.util.Arrays;

import com.tt9smart.R;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.screens.BaseScreenFragment;
import com.tt9smart.util.sys.SystemSettings;

public class MainSettingsScreen extends BaseScreenFragment {
	final public static String NAME = "Main";

	public MainSettingsScreen() { super(); }
	public MainSettingsScreen(@Nullable PreferencesActivity activity) { super(activity); }

	@Override public String getName() { return NAME; }
	@Override protected int getTitle() { return R.string.app_settings;}
	@Override protected int getXml() { return R.xml.prefs; }


	@Override
	public void onCreate() {
		boolean isTT9On = SystemSettings.isTT9Enabled(activity);
		createSettingsSection(isTT9On);
		createAboutSection(isTT9On);
		resetFontSize(false);
	}


	@Override
	public void onResume() {
		super.onResume();

		boolean isTT9On = SystemSettings.isTT9Enabled(activity);
		createSettingsSection(isTT9On);
		createAboutSection(isTT9On);
		resetFontSize(false);
	}


	private void createAboutSection(boolean isTT9On) {
		(new ItemVersionInfo(findPreference(ItemVersionInfo.NAME), activity)).populate().enableClickHandler();
		ProfitPreference profPref = findPreference(ProfitPreference.NAME);
		if (profPref != null && activity != null) {
			profPref.populate(activity, isTT9On);
		}
	}


	private void createSettingsSection(boolean isTT9On) {
		if (activity == null) {
			return;
		}

		Preference gotoSetup = findPreference("screen_setup");
		if (gotoSetup != null) {
			gotoSetup.setSummary(isTT9On ? "" : activity.getString(R.string.setup_click_here_to_enable));
		}

		ArrayList<Preference> screens = new ArrayList<>(Arrays.asList(
			findPreference("screen_appearance"),
			findPreference("screen_keypad"),
			findPreference("screen_languages")
		));

		for (Preference goToScreen : screens) {
			if (goToScreen != null) {
				goToScreen.setEnabled(isTT9On || activity.getSettings().getDemoMode());
			}
		}
	}
}
