package com.tt9smart.colors;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.R;


public class ColorSchemeSystem extends AbstractColorScheme {
	public static final int ID = 0;

	public ColorSchemeSystem(@NonNull Context context) {
		super(context, R.style.TTheme, null);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public int getName() {
		return R.string.pref_dark_theme_auto;
	}

	@Override
	public boolean isSystem() {
		return true;
	}
}
