package com.tt9smart.preferences.screens.modePredictive;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.custom.EnhancedDropDownPreference;
import com.tt9smart.preferences.screens.BaseScreenFragment;

public class ModePredictiveScreen extends BaseScreenFragment {
	public static final String NAME = "ModePredictive";

	public ModePredictiveScreen() { super(); }
	public ModePredictiveScreen(@Nullable PreferencesActivity activity) { super(activity); }

	@Override public String getName() { return NAME; }
	@Override protected int getTitle() { return R.string.pref_category_predictive_mode; }
	@Override protected int getXml() { return R.xml.prefs_screen_mode_predictive; }

	@Override
	protected void onCreate() {
		EnhancedDropDownPreference[] dropdowns = {
			findPreference(DropDownOneKeyEmoji.NAME),
			findPreference(DropDownZeroKeyCharacter.NAME),
			findPreference(DropDownPredictiveAutoAcceptTime.NAME),
		};

		for (EnhancedDropDownPreference dropdown : dropdowns) {
			if (dropdown != null && activity != null) dropdown.populate(activity.getSettings()).preview();
		}

		resetFontSize(false);
	}
}
