package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.ime.modes.InputModeKind;
import com.tt9smart.ime.modes.ModeRecomposing;

public class CmdEditAdjacentLetter implements Command {
	public static final String ID = "cmd_edit_next_letter";

	@Override public String getId() { return ID; }
	@Override public int getIcon() { return 0; }
	@Override public int getName() { return 0; }

	@Override
	public boolean isAvailable(@Nullable TraditionalT9 tt9) {
		return tt9 != null && InputModeKind.isRecomposing(tt9.getInputMode());
	}

	public boolean run(@Nullable TraditionalT9 tt9, boolean left) {
		if (tt9 == null || !isAvailable(tt9)) {
			return false;
		}

		((ModeRecomposing) tt9.getInputMode()).skipLetter(left);
		return true;
	}
}
