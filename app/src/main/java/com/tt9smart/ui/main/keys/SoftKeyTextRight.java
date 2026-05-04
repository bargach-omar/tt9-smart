package com.tt9smart.ui.main.keys;

import android.content.Context;
import android.util.AttributeSet;

import com.tt9smart.commands.CmdRedo;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.util.chars.Characters;

public class SoftKeyTextRight extends SoftKeyText {
	public SoftKeyTextRight(Context context) { super(context); }
	public SoftKeyTextRight(Context context, AttributeSet attrs) { super(context, attrs); }
	public SoftKeyTextRight(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

	protected String getKeyChar() {
		if (tt9 == null) return "";
		if (tt9.isInputTypePhone()) return "#";
		if (tt9.isInputModeNumeric()) return ".";
		if (tt9.isTextEditingActive()) return CmdRedo.iconTxt;

		return Characters.getChar(tt9.getLanguage(), SettingsStore.SOFT_KEY_TEXT_RIGHT_DEFAULT);
	}

	@Override
	protected boolean handleRelease() {
		return tt9 != null && tt9.isTextEditingActive() ? new CmdRedo().run(tt9) : super.handleRelease();
	}

	@Override
	protected String getTitle() {
		return isTextEditingOn() ? "" : super.getTitle();
	}

	@Override
	protected int getCentralIcon() {
		return isTextEditingOn() && !shouldHide() ? new CmdRedo().getIcon() : -1;
	}
}
