package com.tt9smart.preferences.screens.appearance;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.preferences.custom.EnhancedDropDownPreference;
import com.tt9smart.preferences.settings.SettingsStore;

public class DropDownSuggestionFontSize extends EnhancedDropDownPreference implements ItemLayoutChangeReactive{
	public static final String NAME = "pref_suggestion_font_size";

	public DropDownSuggestionFontSize(@NonNull Context context) { super(context); }
	public DropDownSuggestionFontSize(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); }
	public DropDownSuggestionFontSize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	public DropDownSuggestionFontSize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }

	@Override
	public EnhancedDropDownPreference populate(@NonNull SettingsStore settings) {
		commitPercentRange(70, 150, 5);
		setValue(String.valueOf(settings.getSuggestionFontSizePercent()));
		onLayoutChange(settings.getMainViewLayout());

		return this;
	}

	@Override
	protected String getName() {
		return NAME;
	}

	public void onLayoutChange(int mainViewLayout) {
		setVisible(mainViewLayout != SettingsStore.LAYOUT_STEALTH);
		setIconSpaceReserved(false);
	}
}
