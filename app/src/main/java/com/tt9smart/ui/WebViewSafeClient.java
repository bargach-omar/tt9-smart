package com.tt9smart.ui;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.tt9smart.R;
import com.tt9smart.util.Logger;
import com.tt9smart.util.sys.Clipboard;
import com.tt9smart.util.sys.DeviceInfo;

public class WebViewSafeClient extends WebViewClient {
	private final Activity activity;

	public WebViewSafeClient(@NonNull Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
		final String url = request.getUrl().toString();

		if (!url.startsWith("http")) {
			return super.shouldOverrideUrlLoading(view, request);
		}

		if (!DeviceInfo.AT_LEAST_ANDROID_10 || !shareLink(url)) {
			Clipboard.copy(activity, url);
			if (!DeviceInfo.AT_LEAST_ANDROID_13) {
				UI.toastShortSingle(activity, R.string.help_url_copied);
			}
		}

		return true;
	}

	private boolean shareLink(String url) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, url);

		try {
			activity.startActivity(Intent.createChooser(intent, "Share URL"));
			return true;
		} catch (Exception e) {
			Logger.d(getClass().getSimpleName(), "Failed sharing URL: '" + url + "'. " + e.getMessage());
			return false;
		}
	}
}
