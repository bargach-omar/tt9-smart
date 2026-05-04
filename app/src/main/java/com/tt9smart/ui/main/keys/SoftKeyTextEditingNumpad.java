package com.tt9smart.ui.main.keys;

import android.content.Context;
import android.util.AttributeSet;

import com.tt9smart.commands.CommandCollection;
import com.tt9smart.util.chars.Characters;

public class SoftKeyTextEditingNumpad extends SoftKeyNumberNumpad {
	public SoftKeyTextEditingNumpad(Context context) { super(context); }
	public SoftKeyTextEditingNumpad(Context context, AttributeSet attrs) { super(context, attrs); }
	public SoftKeyTextEditingNumpad(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

	protected boolean isFnPanelOn() {
		return isTextEditingActive();
	}

	protected void handleHold() {
		if (isTextEditingActive()) {
			preventRepeat();
		} else {
			super.handleHold();
		}
	}

	@Override
	protected String getTitle() {
		if (isTextEditingActive()) {
			return (getNumber() == 0) ? Characters.SPACE : "";
		}
		return super.getTitle();
	}

	@Override
	protected String getHoldText() {
		return isTextEditingActive() ? "" : super.getHoldText();
	}

	@Override
	protected int getCentralIcon() {
		if (!isTextEditingActive()) {
			return super.getCentralIcon();
		}

		return CommandCollection.getBySoftKey(CommandCollection.COLLECTION_TEXT_EDITING, getId()).getIcon();
	}

	private boolean isTextEditingActive() {
		return tt9 != null && tt9.isTextEditingActive();
	}
}
