package com.tt9smart.commands;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;

public class CmdSpaceKorean implements Command {
	public static final String ID = "key_space_korean";
	public String getId() { return ID; }
	public int getIcon() { return R.drawable.ic_fn_space; }
	public int getName() { return R.string.virtual_key_space_korean; }
	public boolean run(TraditionalT9 tt9) { return tt9 != null && tt9.onKeySpaceKorean(false); }
}
