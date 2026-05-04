package com.tt9smart.util.colors;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class TextSystemColor extends SystemColor{
	public TextSystemColor(@NonNull Context context) {
		color = new TextView(context).getTextColors().getDefaultColor();
	}
}
