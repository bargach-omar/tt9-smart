package com.tt9smart.colors;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.R;

public class ColorSchemeSystemDark extends AbstractColorScheme {
	public static final int ID = -1;

	public ColorSchemeSystemDark(@NonNull Context context) {
		super(context, R.style.TTheme, true);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getName() {
		return R.string.pref_color_scheme_device_dark;
	}

	@Override
	public boolean isSystem() {
		return true;
	}
}
