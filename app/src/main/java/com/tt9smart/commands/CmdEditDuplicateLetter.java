package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.ime.modes.InputModeKind;
import com.tt9smart.ime.modes.ModeRecomposing;

public class CmdEditDuplicateLetter implements Command {
	public static final String ID = "cmd_edit_duplicate_letter";

	@Override public String getId() { return ID; }
	@Override public int getIcon() { return R.drawable.ic_fn_edit_duplicate_letter; }
	@Override public int getName() { return 0; }

	@Override
	public boolean isAvailable(@Nullable TraditionalT9 tt9) {
		return tt9 != null && InputModeKind.isRecomposing(tt9.getInputMode());
	}

	@Override
	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null || !isAvailable(tt9)) {
			return false;
		}

		((ModeRecomposing) tt9.getInputMode()).duplicateLetter();
		return true;
	}
}
