package com.tt9smart.util.colors;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.tt9smart.util.sys.SystemSettings;

public class ErrorSystemColor extends SystemColor {
	public ErrorSystemColor(@NonNull Context context) {
		final boolean isDark = SystemSettings.isNightModeOn(context);
		final int colorResource = isDark ? android.R.color.holo_red_dark : android.R.color.holo_red_light;
		color = ContextCompat.getColor(context, colorResource);
	}
}
