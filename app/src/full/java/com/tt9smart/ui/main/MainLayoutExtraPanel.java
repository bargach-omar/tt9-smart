package com.tt9smart.ui.main;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;

abstract class MainLayoutExtraPanel extends BaseMainLayout {
	MainLayoutExtraPanel(TraditionalT9 tt9, int xml) {
		super(tt9, xml);
	}

	@Override
	void showKeyboard() {
		togglePanel(R.id.main_panel_extra, false);
	}

	@Override
	void showTextEditingPalette() {
		togglePanel(R.id.main_panel_extra, false);
	}

	@Override
	void showCommandPalette() {
		togglePanel(R.id.main_panel_extra, false);
	}
}
