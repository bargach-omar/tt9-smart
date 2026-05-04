package com.tt9smart.util.colors;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class AccentSystemColor extends SystemColor{
	public AccentSystemColor(@NonNull Context context) {
		final TypedValue value = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.colorAccent, value, true);
		color = value.data;
	}
}
