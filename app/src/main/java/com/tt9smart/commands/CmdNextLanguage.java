package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;

public class CmdNextLanguage implements Command {
	public static final String ID = "key_next_language";
	public String getId() { return ID; }
	public int getIcon() { return R.drawable.ic_fn_next_language; }
	public int getName() { return R.string.function_next_language; }

	public boolean run(@Nullable TraditionalT9 tt9) {
		return tt9 != null && tt9.onKeyNextLanguage(false);
	}
}
