package com.tt9smart.ui.main;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.ui.main.keys.SoftKey;
import com.tt9smart.util.sys.DeviceInfo;

class MainLayoutTray extends MainLayoutExtraPanel {
	protected int height;
	protected boolean isCommandPaletteShown = false;
	protected boolean isTextEditingPaletteShown = false;


	MainLayoutTray(TraditionalT9 tt9) {
		this(tt9, R.layout.main_small);
	}

	protected MainLayoutTray(TraditionalT9 tt9, int xml) {
		super(tt9, xml);
	}


	int getHeight(boolean forceRecalculate) {
		if (height <= 0 || forceRecalculate) {
			Resources resources = tt9.getResources();
			int statusBarH = getStatusBarHeight(resources, tt9.getSettings());
			height = statusBarH + getPanelHeight(resources);
		}

		return height;
	}


	protected int getPanelHeight(@NonNull Resources resources) {
		if (isCommandPaletteShown() || isTextEditingPaletteShown()) {
			return resources.getDimensionPixelSize(R.dimen.main_small_command_palette_height);
		} else {
			return 0;
		}
	}


	protected int getStatusBarHeight(@NonNull Resources resources, @NonNull SettingsStore settings) {
		float textSize = resources.getDimension(R.dimen.status_bar_text_size);
		float padding = textSize * 0.45f;
		padding = padding < 1 ? 1 : padding;
		return Math.round((padding + textSize) * settings.getSuggestionFontScale());
	}


	protected void setSoftKeysVisibility() {
		if (view != null) {
			togglePanel(R.id.main_soft_keys, false);
		}
	}


	void showCommandPalette() {
		super.showCommandPalette();
		isCommandPaletteShown = true;
		isTextEditingPaletteShown = false;
		togglePanel(R.id.main_command_keys, true);
		getHeight(true);
		renderKeys(false);
	}


	void showKeyboard() {
		super.showKeyboard();
		togglePanel(R.id.main_command_keys, false);
		isCommandPaletteShown = false;
		isTextEditingPaletteShown = false;
		getHeight(true);
		renderKeys(false);
	}


	@Override
	void showTextEditingPalette() {
		super.showTextEditingPalette();
		isCommandPaletteShown = false;
		isTextEditingPaletteShown = true;
		togglePanel(R.id.main_command_keys, true);
		getHeight(true);
		renderKeys(false);
	}


	@Override
	boolean isCommandPaletteShown() {
		return isCommandPaletteShown;
	}


	@Override
	boolean isTextEditingPaletteShown() {
		return isTextEditingPaletteShown;
	}


	@NonNull
	@Override
	protected ArrayList<SoftKey> getKeys() {
		if (view != null && keys.isEmpty()) {
			keys.addAll(getKeysFromContainer(view.findViewById(R.id.main_command_keys)));
		}
		return keys;
	}


	@Override
	void render() {
		final boolean isPortrait = !DeviceInfo.isLandscapeOrientation(tt9);

		getView();
		setSoftKeysVisibility();
		setPadding();
		setWidth(tt9.getSettings().getWidthPercent(isPortrait), tt9.getSettings().getAlignment());
		setBackgroundBlending();
		enableClickHandlers();
		renderKeys(false);
	}
}
