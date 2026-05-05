package com.tt9smart.preferences.screens.appearance;

import androidx.annotation.Nullable;
import androidx.preference.SwitchPreferenceCompat;

import com.tt9smart.preferences.settings.SettingsStore;

abstract public class SwitchWhenLargeTouchscreenLayout extends SwitchPreferenceCompat implements ItemLayoutChangeReactive {
	public SwitchWhenLargeTouchscreenLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); init(); }
	public SwitchWhenLargeTouchscreenLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(); }
	public SwitchWhenLargeTouchscreenLayout(android.content.Context context, android.util.AttributeSet attrs) { super(context, attrs); init(); }
	public SwitchWhenLargeTouchscreenLayout(android.content.Context context) { super(context); init(); }

	protected void init() {
		setKey(getName());
		setDefaultValue(getDefault());
		setTitle(getTitleResId());
		setSummary(getSummaryResId());
		setChecked(getPersistedBoolean(getDefault()));
	}

	public SwitchWhenLargeTouchscreenLayout populate(@Nullable SettingsStore settings) {
		if (settings != null) {
			onLayoutChange(settings.getMainViewLayout());
		}
		return this;
	}

	@Override
	public void onLayoutChange(int mainViewLayout) {
		setVisible(false);
	}


	abstract protected String getName();
	abstract protected boolean getDefault();
	abstract protected int getTitleResId();
	abstract protected int getSummaryResId();
}
