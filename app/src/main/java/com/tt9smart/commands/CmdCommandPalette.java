package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;

public class CmdCommandPalette implements Command {
	public static final String ID = "key_command_palette";
	public String getId() { return ID; }
	public int getIconText() { return R.string.virtual_key_command_palette; }
	public String getIconEmojiText() { return "☰"; }
	public int getIcon() { return R.drawable.ic_fn_command_palette; }
	public int getName() { return com.tt9smart.R.string.function_show_command_palette; }

	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null) {
			return false;
		}
		tt9.onKeyCommandPalette(false);
		return true;
	}
}
