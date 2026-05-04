package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;

public class CmdTxtSelectAll implements Command {
	public static final String ID = "key_txt_select_all";
	@Override public String getId() { return ID; }
	@Override public int getIcon() { return com.tt9smart.R.drawable.ic_txt_select_all; }
	@Override public int getName() { return 0; }
	@Override public int getHardKey() { return 5; }
	@Override public int getPaletteKey() { return com.tt9smart.R.id.soft_key_5; }

	@Override
	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null || tt9.getTextSelection() == null) {
			return false;
		}

		tt9.getTextSelection().selectAll();
		return true;
	}
}
