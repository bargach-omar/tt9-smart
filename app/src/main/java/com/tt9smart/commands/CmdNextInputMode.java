package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.ui.StatusIcon;

public class CmdNextInputMode implements Command {
	public static final String ID = "key_next_input_mode";
	public String getId() { return ID; }
	public int getIcon() { return StatusIcon.getCachedResourceId(); }
	public int getName() { return R.string.function_next_mode; }

	public void invalidateIcon(@Nullable TraditionalT9 tt9) {
		new StatusIcon(
			tt9 != null ? tt9.getInputMode() : null,
			tt9 != null ? tt9.getLanguage() : null,
			tt9 != null ? tt9.getDisplayTextCase() : 0
		);
	}

	public boolean run(@Nullable TraditionalT9 tt9) {
		return tt9 != null && tt9.onKeyNextInputMode(false);
	}
}
