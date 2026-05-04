package com.tt9smart.ui.main;

import android.content.res.Resources;
import android.graphics.Paint;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;

class MainLayoutSmartBar extends MainLayoutTray {
	MainLayoutSmartBar(TraditionalT9 tt9) {
		super(tt9, R.layout.main_smartbar);
	}


	@Override
	int getHeight(boolean forceRecalculate) {
		if (height <= 0 || forceRecalculate) {
			Resources resources = tt9.getResources();
			float density = resources.getDisplayMetrics().density;

			// Actual rendered text height (descent - ascent) for the font at the scaled size.
			// getStatusBarHeight() uses textSize * 1.45 which overestimates; the FrameLayout
			// holding the status bar sizes to the font line height, not an arbitrary multiplier.
			float textPx = resources.getDimension(R.dimen.status_bar_text_size)
				* tt9.getSettings().getSuggestionFontScale();
			Paint paint = new Paint();
			paint.setTextSize(textPx);
			Paint.FontMetricsInt fm = paint.getFontMetricsInt();
			int lineHeight = fm.bottom - fm.top; // matches TextView measured height (includes leading)

			int separatorH = Math.round(density); // 1dp top separator in main_smartbar.xml
			height = lineHeight + separatorH;

			if (view != null) {
				android.view.View predictionsBar = view.findViewById(R.id.word_predictions_bar);
				if (predictionsBar != null && predictionsBar.getVisibility() == android.view.View.VISIBLE) {
					// predictions bar: same line height + 4dp top + 4dp bottom padding
					int padPx = Math.round(4 * density);
					height += lineHeight + 2 * padPx;
				}
			}
		}

		return height;
	}
}
