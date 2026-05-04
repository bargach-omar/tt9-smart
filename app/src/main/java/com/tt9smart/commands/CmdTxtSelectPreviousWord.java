package com.tt9smart.commands;

import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.languages.LanguageKind;

public class CmdTxtSelectPreviousWord implements Command {
	public static final String ID = "key_txt_select_left_word";
	@Override public String getId() { return ID; }
	@Override public int getIcon() { return com.tt9smart.R.drawable.ic_txt_word_back; }
	@Override public int getName() { return 0; }
	@Override public int getHardKey() { return 4; }
	@Override public int getPaletteKey() { return com.tt9smart.R.id.soft_key_4; }

	@Override
	public boolean run(@Nullable TraditionalT9 tt9) {
		if (tt9 == null || tt9.getTextSelection() == null) {
			return false;
		}

		tt9.getTextSelection().selectNextWord(!LanguageKind.isRTL(tt9.getLanguage()));
		return true;
	}
}
