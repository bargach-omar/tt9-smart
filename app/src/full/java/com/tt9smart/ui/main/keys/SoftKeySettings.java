package com.tt9smart.ui.main.keys;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.tt9smart.commands.CmdShowSettings;
import com.tt9smart.commands.Command;

public class SoftKeySettings extends BaseKeyboardResizeKey {
	private final CmdShowSettings command = new CmdShowSettings();

	public SoftKeySettings(Context context) { super(context); }
	public SoftKeySettings(Context context, AttributeSet attrs) { super(context, attrs); }
	public SoftKeySettings(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

	@NonNull
	protected Command getCommand() {
		return command;
	}

	@Override
	protected boolean handleRelease() {
		if (super.handleRelease()) {
			return true;
		}

		return getCommand().run(tt9);
	}

	@Override
	protected int getCentralIcon() {
		final int icon = super.getCentralIcon();
		return icon != -1 ? icon : getCommand().getIcon();
	}

	@Override
	public void render() {
		resetIconCache();
		super.render();
	}
}
