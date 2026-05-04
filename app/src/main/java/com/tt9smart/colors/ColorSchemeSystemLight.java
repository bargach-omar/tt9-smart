package com.tt9smart.colors;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.R;

public class ColorSchemeSystemLight extends AbstractColorScheme {
	public static final int ID = -2;

	public ColorSchemeSystemLight(@NonNull Context context) {
		super(context, R.style.TTheme, false);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getName() {
		return R.string.pref_color_scheme_device_light;
	}

	@Override
	public boolean isSystem() {
		return true;
	}
}
