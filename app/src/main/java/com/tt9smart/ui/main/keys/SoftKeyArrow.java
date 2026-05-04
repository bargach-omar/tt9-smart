package com.tt9smart.ui.main.keys;

import android.content.Context;
import android.util.AttributeSet;

import com.tt9smart.R;
import com.tt9smart.commands.CmdEditAdjacentLetter;
import com.tt9smart.commands.CmdMoveCursor;
import com.tt9smart.commands.CmdSuggestionNext;
import com.tt9smart.commands.CmdSuggestionPrevious;
import com.tt9smart.ui.Vibration;

public class SoftKeyArrow extends BaseSoftKeyCustomizable {
	private final CmdEditAdjacentLetter editNextLetter = new CmdEditAdjacentLetter();
	private final CmdMoveCursor moveCursor = new CmdMoveCursor();

	private boolean held;
	private boolean editedNext;

	public SoftKeyArrow(Context context) { super(context); }
	public SoftKeyArrow(Context context, AttributeSet attrs) { super(context, attrs); }
	public SoftKeyArrow(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

	@Override
	protected boolean handlePress() {
		editedNext = false;
		held = false;
		return super.handlePress();
	}

	@Override
	protected void handleHold() {
		if (editNextLetter.isAvailable(tt9)) {
			preventRepeat();
			editedNext = editNextLetter.run(tt9, getId() == R.id.soft_key_left_arrow);
		} else {
			held = true;
			moveCursor();
		}
	}

	@Override
	protected boolean handleRelease() {
		if (editedNext || held) {
			vibrate(Vibration.getReleaseVibration());
			return true;
		} else {
			return moveCursor();
		}
	}

	private boolean moveCursor() {
		if (!validateTT9Handler()) {
			return false;
		}

		int keyId = getId();
		if (keyId == R.id.soft_key_left_arrow) return onLeft();
		if (keyId == R.id.soft_key_right_arrow) return onRight();

		return false;
	}

	private boolean onLeft() {
		return new CmdSuggestionPrevious().run(tt9) || moveCursor.run(tt9, CmdMoveCursor.CURSOR_MOVE_LEFT);
	}

	private boolean onRight() {
		return new CmdSuggestionNext().run(tt9) || moveCursor.run(tt9, CmdMoveCursor.CURSOR_MOVE_RIGHT);
	}

	@Override
	public void render() {
		final int visibility = tt9 != null && tt9.getSettings().getArrowsLeftRight() ? VISIBLE : GONE;

		setVisibility(visibility);
		getOverlayWrapper();
		if (overlay != null) {
			overlay.setVisibility(visibility);
		}

		super.render();
	}
}
