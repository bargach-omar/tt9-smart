package com.tt9smart.preferences.screens.appearance;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.preferences.custom.EnhancedDropDownPreference;
import com.tt9smart.preferences.settings.SettingsStore;

public class DropDownAlignment extends EnhancedDropDownPreference {
	public static final String NAME = "pref_numpad_alignment";

	public DropDownAlignment(@NonNull Context context) { super(context); }
	public DropDownAlignment(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); }
	public DropDownAlignment(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	public DropDownAlignment(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }

	@Override
	public EnhancedDropDownPreference populate(@NonNull SettingsStore settings) {
		add(Gravity.START, R.string.virtual_numpad_alignment_left);
		add(Gravity.CENTER_HORIZONTAL, R.string.virtual_numpad_alignment_center);
		add(Gravity.END, R.string.virtual_numpad_alignment_right);
		commitOptions();
		setValue(String.valueOf(settings.getAlignment()));

		return this;
	}

	@Override
	protected String getName() {
		return NAME;
	}
}
