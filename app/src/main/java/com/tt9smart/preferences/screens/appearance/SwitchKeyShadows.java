package com.tt9smart.preferences.screens.appearance;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.util.sys.DeviceInfo;

public class SwitchKeyShadows extends SwitchPreferenceCompat {
	public static final String NAME = "pref_key_shadows";
	public static final boolean DEFAULT = !DeviceInfo.AT_LEAST_ANDROID_12;

	public SwitchKeyShadows(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public SwitchKeyShadows(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SwitchKeyShadows(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwitchKeyShadows(@NonNull Context context) {
		super(context);
		init();
	}

	private void init() {
		setKey(NAME);
		setTitle(com.tt9smart.R.string.pref_key_shadows);
		setChecked(getPersistedBoolean(DEFAULT));
	}
}
