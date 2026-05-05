package com.tt9smart.preferences.settings;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tt9smart.commands.NullCommand;

public class SettingsCustomKeyActions extends SettingsUI {
	public static final String CUSTOM_ACTION_KEY_1 = "_1";
	public static final String CUSTOM_ACTION_KEY_2 = "_2";
	public static final String CUSTOM_ACTION_KEY_3 = "_3";
	public static final String CUSTOM_ACTION_KEY_4 = "_4";
	public static final String CUSTOM_ACTION_KEY_5 = "_5";
	public static final String CUSTOM_ACTION_KEY_6 = "_6";
	public static final String CUSTOM_ACTION_KEY_7 = "_7";
	public static final String CUSTOM_ACTION_KEY_8 = "_8";
	public static final String CUSTOM_ACTION_KEY_9 = "_9";

	public float getMoveCursorWithSpaceThreshold() {
		return 0;
	}

	protected SettingsCustomKeyActions(Context context) {
		super(context);
	}

	public boolean getMoveCursorWithSpace() {
		return false;
	}

	@NonNull
	public String getSwipeRightCommand(String keySuffix) {
		return NullCommand.ID;
	}

	@NonNull
	public String getSwipeLeftCommand(String keySuffix) {
		return NullCommand.ID;
	}
}
