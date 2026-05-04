package com.tt9smart.preferences.screens.appearance;

import com.tt9smart.R;
import com.tt9smart.util.sys.HardwareInfo;

public class SwitchDoubleTapResize extends SwitchWhenLargeTouchscreenLayout {
	public final static String NAME = "pref_double_tap_resize";
	public final static boolean DEFAULT = HardwareInfo.IS_EMULATOR;

	public SwitchDoubleTapResize(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }
	public SwitchDoubleTapResize(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
	public SwitchDoubleTapResize(android.content.Context context, android.util.AttributeSet attrs) { super(context, attrs); }
	public SwitchDoubleTapResize(android.content.Context context) { super(context); }

	@Override protected String getName() { return NAME; }
	@Override protected boolean getDefault() { return DEFAULT; }
	@Override protected int getTitleResId() { return R.string.pref_double_tap_resize; }
	@Override protected int getSummaryResId() { return R.string.pref_double_tap_resize_summary;}
}
