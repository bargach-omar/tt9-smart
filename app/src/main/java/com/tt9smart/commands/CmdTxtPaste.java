package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;

public class CmdTxtPaste implements Command {
	public static final String ID = "key_txt_paste";
	@Override public String getId() { return ID; }
	@Override public int getIcon() { return com.tt9smart.R.drawable.ic_txt_paste; }
	@Override public int getName() { return 0; }
	@Override public int getHardKey() { return 9; }
	@Override public int getPaletteKey() { return com.tt9smart.R.id.soft_key_9; }

	@Override
	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null) {
			return false;
		}

		tt9.paste();
		return true;
	}
}
