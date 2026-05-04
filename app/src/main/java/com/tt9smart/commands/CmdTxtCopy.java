package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;

public class CmdTxtCopy implements Command {
	public static final String ID = "key_txt_copy";
	@Override public String getId() { return ID; }
	@Override public int getIcon() { return com.tt9smart.R.drawable.ic_txt_copy; }
	@Override public int getName() { return 0; }
	@Override public int getHardKey() { return 8; }
	@Override public int getPaletteKey() { return com.tt9smart.R.id.soft_key_8; }
	@Override public boolean run(@Nullable TraditionalT9 tt9) { return tt9 != null && tt9.copy(); }
}
