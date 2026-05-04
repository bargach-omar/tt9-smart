package com.tt9smart.ui;

import androidx.annotation.Nullable;

import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.screens.BaseScreenFragment;

/**
 * Implemented in the "premium" source set. The open-source version
 * has no premium features, so this class has only minimal functionality.
 */
public class PremiumPreferencesActivity extends ActivityWithNavigation {
	protected BaseScreenFragment getScreen(PreferencesActivity prefsActivity, @Nullable String ignored) {
		return null;
	}
}
