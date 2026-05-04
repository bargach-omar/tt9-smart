package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.languages.LanguageKind;

public class CmdTxtSelectNextChar implements Command {
	public static final String ID = "key_txt_select_next_char";
	@Override public String getId() { return ID; }
	@Override public int getIcon() { return com.tt9smart.R.drawable.ic_dpad_right; }
	@Override public int getName() { return 0; }
	@Override public int getHardKey() { return 3; }
	@Override public int getPaletteKey() { return com.tt9smart.R.id.soft_key_3; }

	@Override
	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null || tt9.getTextSelection() == null) {
			return false;
		}

		tt9.getTextSelection().selectNextChar(LanguageKind.isRTL(tt9.getLanguage()));
		return true;
	}
}
