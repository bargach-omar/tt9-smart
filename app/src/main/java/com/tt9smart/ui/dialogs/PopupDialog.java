package com.tt9smart.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tt9smart.R;
import com.tt9smart.ime.TraditionalT9;
import com.tt9smart.ui.PopupBuilder;
import com.tt9smart.ui.main.MainView;
import com.tt9smart.util.ThemedContextBuilder;
import com.tt9smart.util.sys.DeviceInfo;

abstract public class PopupDialog implements DialogInterface.OnKeyListener {
	protected final ContextThemeWrapper context;
	private final MainView mainView;

	protected boolean cancelable;
	protected Dialog popup;
	protected String title;
	protected String message;
	protected String OKLabel;

	PopupDialog(@NonNull TraditionalT9 tt9, int theme) {
		this.context = new ThemedContextBuilder()
				.setConfiguration(tt9.getResources().getConfiguration())
				.setContext(tt9.getApplicationContext())
				// The main theme does not work on Android <= 11 and the _AddWord theme does not work on 12+.
				// Not sure why since they inherit from the same parent, but it is what it is.
				.setTheme(DeviceInfo.AT_LEAST_ANDROID_12 ? R.style.TTheme : theme)
				.build();

		cancelable = true;
		mainView = tt9.getMainView();
	}

	protected void close() {
		if (popup != null) {
			popup.dismiss();
			popup = null;
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	protected boolean render(Runnable onOK, Runnable onCancel, View customView) {
		PopupBuilder popupBuilder = new PopupBuilder(context);
		if (onOK != null) {
			popupBuilder.setPositiveButton(OKLabel, onOK);
		}
		if (customView != null) {
			popupBuilder.setView(customView);
		}

		popup = popupBuilder
			.setCancelable(cancelable)
			.setTitle(title)
			.setMessage(message)
			.setNegativeButton(true, onCancel)
			.setOnKeyListener(this)
			.showFromIme(mainView);

		return popup != null;
	}
}
