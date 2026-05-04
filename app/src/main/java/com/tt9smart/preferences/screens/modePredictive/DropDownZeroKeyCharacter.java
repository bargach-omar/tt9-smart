package com.tt9smart.preferences.screens.modePredictive;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.preferences.custom.EnhancedDropDownPreference;
import com.tt9smart.preferences.settings.SettingsStore;

public class DropDownZeroKeyCharacter extends EnhancedDropDownPreference {
	public static final String NAME = "pref_double_zero_char";
	public static final String DEFAULT = ".";

	public DropDownZeroKeyCharacter(@NonNull Context context) { super(context); }
	public DropDownZeroKeyCharacter(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); }
	public DropDownZeroKeyCharacter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	public DropDownZeroKeyCharacter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }

	@Override
	public String getName() {
		return NAME;
	}

	public DropDownZeroKeyCharacter populate(@NonNull SettingsStore settings) {
		add(DEFAULT, DEFAULT);
		add(",", ",");
		add("\\n", R.string.char_newline); // SharedPreferences return a corrupted string when using the real "\n"... :(
		add("", R.string.char_space);
		commitOptions();
		setValue(settings.getDoubleZeroChar());
		setDefaultValue(DEFAULT);

		return this;
	}
}
