package com.tt9smart.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.tt9smart.R;
import com.tt9smart.db.DataStore;
import com.tt9smart.db.words.LegacyDb;
import com.tt9smart.ime.helpers.InputModeValidator;
import com.tt9smart.languages.LanguageCollection;
import com.tt9smart.preferences.screens.BaseScreenFragment;
import com.tt9smart.preferences.screens.UsageStatsScreen;
import com.tt9smart.preferences.screens.appearance.AppearanceScreen;
import com.tt9smart.preferences.screens.debug.DebugScreen;
import com.tt9smart.preferences.screens.deleteWords.DeleteWordsScreen;
import com.tt9smart.preferences.screens.fnKeyOrder.FnKeyOrderScreen;
import com.tt9smart.preferences.screens.hotkeys.HotkeysScreen;
import com.tt9smart.preferences.screens.keypad.KeyPadScreen;
import com.tt9smart.preferences.screens.languageSelection.LanguageSelectionScreen;
import com.tt9smart.preferences.screens.languages.LanguagesScreen;
import com.tt9smart.preferences.screens.main.MainSettingsScreen;
import com.tt9smart.preferences.screens.modeAbc.ModeAbcScreen;
import com.tt9smart.preferences.screens.modePredictive.ModePredictiveScreen;
import com.tt9smart.preferences.screens.punctuation.PunctuationScreen;
import com.tt9smart.preferences.screens.setup.SetupScreen;
import com.tt9smart.ui.PremiumPreferencesActivity;
import com.tt9smart.util.Logger;
import com.tt9smart.util.sys.SystemSettings;

public class PreferencesActivity extends PremiumPreferencesActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		getSettings();
		applyTheme();
		Logger.setLevel(settings.getLogLevel());
		new Thread(this::initSecondaryTasks).start();

		super.onCreate(savedInstanceState);

		buildLayout();
		displayScreen(getPreviousScreen(savedInstanceState), false);
	}


	private void initSecondaryTasks() {
		LanguageCollection.init(this);
		try (LegacyDb db = new LegacyDb(this)) { db.clear(); }
		DataStore.init(getApplicationContext());

		InputModeValidator.validateEnabledLanguages(settings.getEnabledLanguageIds());
		validateFunctionKeys();
	}


	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		preventEdgeToEdge(findViewById(R.id.preferences_container));
	}


	@Override
	public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref) {
		BaseScreenFragment fragment = getScreen((getScreenName(pref)));
		fragment.setArguments(pref.getExtras());
		displayScreen(fragment, true);
		return true;
	}


	@Override
	protected void onResume() {
		super.onResume();

		if (!SystemSettings.isTT9Enabled(this)) {
			return;
		}

		Intent intent = getIntent();
		String screenName = intent != null ? intent.getStringExtra("screen") : null;
		screenName = screenName != null ? screenName : "";

		BaseScreenFragment screen = getScreen(screenName);

		if (screen.getName().equals(screenName)) {
			displayScreen(screen, false);
		}
	}


	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			getOnBackPressedDispatcher().onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void selectOption(int position, boolean click) {
		// for convenience, scroll to the bottom on 0-key click
		try {
			if (position == 0) {
				position = getOptionsCount.call();
				resetKeyRepeat(); // ... but do not activate the last option on double click
			}
		}
		catch (Exception ignore) {}

		super.selectOption(position, click);
	}


	/**
	 * getScreenName
	 * Determines the name of the screen for the given preference, as defined in the preference's "fragment" attribute.
	 * Expected format: "current.package.name.screens.SomeNameScreen"
	 */
	private String getScreenName(@NonNull Preference pref) {
		String screenClassName = pref.getFragment();
		return screenClassName != null ? screenClassName.replaceFirst("^.+?([^.]+)Screen$", "$1") : "";
	}


	/**
	 * getScreen
	 * Finds a screen fragment by name. If there is no fragment with such name, the main screen
	 * fragment will be returned.
	 */
	protected BaseScreenFragment getScreen(@Nullable String name) {
		BaseScreenFragment screen = super.getScreen(this, name);
		if (screen != null) {
			return screen;
		}

		name = name != null ? name : "";

		return switch (name) {
			case AppearanceScreen.NAME -> new AppearanceScreen(this);
			case DebugScreen.NAME -> new DebugScreen(this);
			case DeleteWordsScreen.NAME -> new DeleteWordsScreen(this);
			case FnKeyOrderScreen.NAME -> new FnKeyOrderScreen(this);
			case HotkeysScreen.NAME -> new HotkeysScreen(this);
			case KeyPadScreen.NAME -> new KeyPadScreen(this);
			case LanguagesScreen.NAME -> new LanguagesScreen(this);
			case LanguageSelectionScreen.NAME -> new LanguageSelectionScreen(this);
			case ModePredictiveScreen.NAME -> new ModePredictiveScreen(this);
			case ModeAbcScreen.NAME -> new ModeAbcScreen(this);
			case PunctuationScreen.NAME -> new PunctuationScreen(this);
			case SetupScreen.NAME -> new SetupScreen(this);
			case UsageStatsScreen.NAME -> new UsageStatsScreen(this);
			default -> new MainSettingsScreen(this);
		};
	}


	/**
	 * displayScreen
	 * Replaces the currently displayed screen fragment with a new one.
	 */
	private void displayScreen(BaseScreenFragment screen, boolean addToBackStack) {
		getOptionsCount = screen::getPreferenceCount;

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.replace(R.id.preferences_container, screen);
		if (addToBackStack) {
			transaction.addToBackStack(screen.getClass().getSimpleName());
		}

		transaction.commit();
	}


	public void displayScreen(@NonNull String screenName) {
		displayScreen(getScreen(screenName), true);
	}


	private void buildLayout() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true); // hide the "back" button, if visible
		}

		setContentView(R.layout.preferences_container);
	}


	@NonNull
	private BaseScreenFragment getPreviousScreen(Bundle savedInstanceState) {
		BaseScreenFragment screen = null;
		if (savedInstanceState != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			screen = (BaseScreenFragment) fragmentManager.findFragmentById(R.id.preferences_container);
		}

		if (screen == null) {
			return getScreen("default");
		}

		screen.restart(this);
		return screen;
	}


	public void setScreenTitle(int title) {
		// set the title
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}


	private void applyTheme() {
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
	}


	private void validateFunctionKeys() {
		if (settings.areHotkeysInitialized()) {
			settings.setDefaultKeys();
		}
	}
}
