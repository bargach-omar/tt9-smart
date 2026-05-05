package com.tt9smart.ui.main;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.preferences.settings.SettingsStore;
import com.tt9smart.ui.Vibration;
import com.tt9smart.util.sys.DeviceInfo;

public class ResizableMainView extends StaticMainView implements View.OnAttachStateChangeListener {
	private Vibration vibration;

	private int height;
	private float resizeStartY;
	private long lastResizeTime;

	private int heightSmall;
	private int heightTray;
	private int heightSmartBar;


	public ResizableMainView(TraditionalT9 tt9) {
		super(tt9);
		calculateSnapHeights();
	}


	private void calculateSnapHeights() {
		boolean forceRecalculate = DeviceInfo.AT_LEAST_ANDROID_15;

		heightSmall = new MainLayoutSmall(tt9).getHeight(forceRecalculate);
		heightTray = new MainLayoutTray(tt9).getHeight(forceRecalculate);
		heightSmartBar = new MainLayoutSmartBar(tt9).getHeight(forceRecalculate);
	}


	@Nullable
	@Override
	public View getView() {
		final View view = super.getView();
		if (view != null) {
			view.removeOnAttachStateChangeListener(this);
			view.addOnAttachStateChangeListener(this);
			vibration = new Vibration(tt9.getSettings(), view);
		}

		return view;
	}


	@Override
	public boolean create() {
		return super.create() && main != null;
	}


	@Override
	public void destroy() {
		if (main != null && main.getView() != null) {
			main.getView().removeOnAttachStateChangeListener(this);
		}
		super.destroy();
	}


	@Override public void onViewDetachedFromWindow(@NonNull View v) {}
	@Override public void onViewAttachedToWindow(@NonNull View v) {
		if (main != null) {
			main.setPadding();
			setHeight(height, heightSmall, heightSmartBar);
		}
	}


	public void onOrientationChanged() {
		showKeyboard();
		render();
	}


	public void onAlign(float deltaX) {
		boolean right = deltaX > 0;
		SettingsStore settings = tt9.getSettings();

		if (settings.getAlignment() == Gravity.START && right) {
			settings.setAlignment(Gravity.CENTER_HORIZONTAL);
		} else if (settings.getAlignment() == Gravity.END && !right) {
			settings.setAlignment(Gravity.CENTER_HORIZONTAL);
		} else if (settings.getAlignment() == Gravity.CENTER_HORIZONTAL && right) {
			settings.setAlignment(Gravity.END);
		} else if (settings.getAlignment() == Gravity.CENTER_HORIZONTAL && !right) {
			settings.setAlignment(Gravity.START);
		}

		render();
	}


	public void onResizeStart(float startY) {
		resizeStartY = startY;
	}


	public void onResize(float currentY) {
		int resizeDelta = (int) (resizeStartY - currentY);
		resizeStartY = currentY;

		if (resizeDelta < 0) {
			shrink(resizeDelta);
		} else if (resizeDelta > 0) {
			expand(resizeDelta);
		}
	}


	public void onResizeThrottled(float currentY) {
		long now = System.currentTimeMillis();
		if (now - lastResizeTime > SettingsStore.RESIZE_THROTTLING_TIME) {
			lastResizeTime = now;
			onResize(currentY);
		}
	}


	public void onSnap() {
		SettingsStore settings = tt9.getSettings();

		if (settings.isMainLayoutTray() || settings.isMainLayoutSmartBar()) {
			expand(1);
		} else if (settings.isMainLayoutSmall()) {
			expand(heightSmartBar);
		} else {
			shrink(-heightSmartBar);
		}
	}


	private void expand(int delta) {
		if (main == null) {
			return;
		}

		final SettingsStore settings = tt9.getSettings();

		if (settings.isMainLayoutTray() || settings.isMainLayoutSmartBar()) {
			settings.setMainViewLayout(SettingsStore.LAYOUT_SMALL);
			height = heightSmall;
			tt9.setCurrentView();
			main.requestPreventEdgeToEdge();
			vibration.vibrate();
		} else if (settings.isMainLayoutSmall()) {
			settings.setMainViewLayout(SettingsStore.LAYOUT_SMALL);
			height = (int) Math.max(Math.max(heightSmall * 0.6, heightSmall * 1.1), height + delta);
			tt9.setCurrentView();
			main.requestPreventEdgeToEdge();
			vibration.vibrate();
		} else {
			changeHeight(delta, heightSmall, heightSmall);
		}
	}


	private void shrink(int delta) {
		SettingsStore settings = tt9.getSettings();

		if (main == null || settings.isMainLayoutTray() || settings.isMainLayoutSmartBar()) {
			return;
		}

		if (settings.isMainLayoutSmall()) {
			settings.setMainViewLayout(SettingsStore.LAYOUT_TRAY);
			height = heightTray;
			tt9.setCurrentView();
			fitMain();
			main.requestPreventEdgeToEdge();
			vibration.vibrate();
		} else if (!changeHeight(delta, heightSmall, heightSmall)) {
			settings.setMainViewLayout(SettingsStore.LAYOUT_SMALL);
			height = heightSmall;
			tt9.setCurrentView();
			main.requestPreventEdgeToEdge();
			vibration.vibrate();
		}
	}


	private boolean changeHeight(int delta, int minHeight, int maxHeight) {
		int keyboardHeight = main != null ? main.getKeyboardHeight() : -1;
		if (keyboardHeight == 0) {
			return false;
		}

		return setHeight(keyboardHeight + delta, minHeight, maxHeight);
	}


	private boolean setHeight(int height, int minHeight, int maxHeight) {
		if (main == null || height < minHeight) {
			return false;
		}

		height = Math.min(height, maxHeight);
		if (main.setKeyboardHeight(height)) {
			this.height = height;
			return true;
		}

		return false;
	}

	protected void fitMain() {
		if (main == null || main instanceof MainLayoutStealth) {
			return;
		}

		calculateSnapHeights();
		int heightLow, heightHigh, heightMain = main.getHeight(true);

		if (main instanceof MainLayoutSmall) {
			heightLow = 0;
			heightHigh = Math.max(heightSmall, heightMain); // make room for the command palette
		} else if (main instanceof MainLayoutSmartBar) {
			heightLow = 0;
			heightHigh = Math.max(heightSmartBar, heightMain);
		} else {
			heightLow = 0;
			heightHigh = Math.max(heightTray, heightMain); // make room for the command palette
		}

		setHeight(heightMain, heightLow, heightHigh);
	}

	public void updateHeight() {
		fitMain();
	}


	@Override
	public void showCommandPalette() {
		super.showCommandPalette();
		fitMain();
	}

	@Override
	public void showKeyboard() {
		super.showKeyboard();
		fitMain();
	}

	@Override
	public void showTextEditingPalette() {
		super.showTextEditingPalette();
		fitMain();
	}

	@Override
	public void render() {
		super.render();
		fitMain();
	}
}
