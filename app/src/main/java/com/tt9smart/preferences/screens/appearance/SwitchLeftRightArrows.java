package com.tt9smart.preferences.screens.appearance;

import android.content.Context;
import android.util.AttributeSet;

import com.tt9smart.R;

public class SwitchLeftRightArrows extends SwitchWhenLargeTouchscreenLayout {
	public final static String NAME = "pref_arrow_keys_visible";
	public final static boolean DEFAULT = true;

	public SwitchLeftRightArrows(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }
	public SwitchLeftRightArrows(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
	public SwitchLeftRightArrows(Context context, AttributeSet attrs) { super(context, attrs); }
	public SwitchLeftRightArrows(Context context) { super(context); }

	@Override protected String getName() { return NAME; }
	@Override protected boolean getDefault() { return DEFAULT; }
	@Override protected int getTitleResId() { return R.string.pref_arrows_left_right; }
	@Override protected int getSummaryResId() { return R.string.pref_arrows_left_right_summary; }
}
