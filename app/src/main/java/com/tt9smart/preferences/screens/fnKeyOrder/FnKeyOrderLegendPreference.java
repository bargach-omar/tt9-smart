package com.tt9smart.preferences.screens.fnKeyOrder;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.commands.CmdAddWord;
import com.tt9smart.commands.CmdBackspace;
import com.tt9smart.commands.CmdEditText;
import com.tt9smart.commands.CmdFilterSuggestions;
import com.tt9smart.commands.CmdNextInputMode;
import com.tt9smart.commands.CmdShift;
import com.tt9smart.commands.CmdShowSettings;
import com.tt9smart.commands.CmdVoiceInput;
import com.tt9smart.preferences.custom.PreferencePlainText;

public class FnKeyOrderLegendPreference extends PreferencePlainText {
	public FnKeyOrderLegendPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); populate(); }
	public FnKeyOrderLegendPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); populate(); }
	public FnKeyOrderLegendPreference(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); populate(); }
	public FnKeyOrderLegendPreference(@NonNull Context context) { super(context); populate(); }

	private void populate() {
		StringBuilder content = new StringBuilder(__(R.string.fn_key_order_legend))
			.append("\n1 = ").append(__(new CmdShowSettings().getName()))
			.append("\n2 = ").append(__(new CmdAddWord().getName()))
			.append("\n3 = ").append(__(new CmdShift().getName()))
			.append("\n4 = ").append(__(new CmdNextInputMode().getName()))
			.append("\n5 = ").append(__(new CmdBackspace().getName()))
			.append("\n6 = ").append(__(new CmdFilterSuggestions().getName()))
			.append("\n7 = ").append(__(new CmdEditText().getName())).append(" / ").append(__(new CmdVoiceInput().getName()))
			.append("\n8 = OK\n\n")
			.append(__(R.string.fn_key_order_preview_tip));

		setSummary(content);
	}

	private String __(int resourceId) {
		return getContext().getString(resourceId);
	}
}
