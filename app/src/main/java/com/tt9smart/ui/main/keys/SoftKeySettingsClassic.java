package com.tt9smart.ui.main.keys;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.tt9smart.commands.CmdBack;
import com.tt9smart.commands.Command;

public class SoftKeySettingsClassic extends SoftKeyTutorial {
	private final CmdBack back = new CmdBack();

	public SoftKeySettingsClassic(Context context) { super(context); }
	public SoftKeySettingsClassic(Context context, AttributeSet attrs) { super(context, attrs); }
	public SoftKeySettingsClassic(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

	@NonNull
	@Override
	protected Command getCommand() {
		return tt9 != null && tt9.isFnPanelVisible() ? back : super.getCommand();
	}
}
