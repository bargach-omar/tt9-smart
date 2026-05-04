package com.tt9smart.commands;

import com.tt9smart.R;

public class CmdSuggestionPrevious implements Command {
	public static final String ID = "key_previous_suggestion";
	public String getId() { return ID; }
	public int getIcon() { return R.drawable.ic_dpad_left; }
	public int getName() { return R.string.function_previous_suggestion; }

	public boolean run(com.tt9smart.ime.TraditionalT9 tt9) {
		return tt9 != null && tt9.onKeyScrollSuggestion(false, true);
	}
}
