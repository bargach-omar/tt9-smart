package com.tt9smart.preferences.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.colors.AbstractColorScheme;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.custom.KeyboardPreviewSwitchPreference;
import com.tt9smart.util.sys.DeviceInfo;

abstract public class ScreenWithPreviewKeyboardHeaderFragment extends BaseScreenFragment {
	private KeyboardPreviewSwitchPreference preview;

	public ScreenWithPreviewKeyboardHeaderFragment() { super(); }
	public ScreenWithPreviewKeyboardHeaderFragment(@Nullable PreferencesActivity activity) { super(activity); }

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// The preview switch is not accessible on devices without touch screen, so omit it.
		if (activity == null || DeviceInfo.noTouchScreen(activity)) {
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		View root = inflater.inflate(R.layout.prefs_screen_with_preview_header, container, false);

		preview = new KeyboardPreviewSwitchPreference(activity);
		preview.bindView(root.findViewById(R.id.static_preview_header));
		preview.setOnBeforePreviewCallback(this::onBeforePreview);

		View prefs = super.onCreateView(inflater, container, savedInstanceState);
		FrameLayout prefsContainer = root.findViewById(R.id.preferences_container);
		if (prefsContainer != null) {
			prefsContainer.addView(prefs);
		}

		return root;
	}


	@Override
	protected void onCreate() {
		if (activity != null) {
			activity.getSettings().reloadColorScheme(); // clear any invalid preview cache
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		if (activity != null) {
			activity.getSettings().reloadColorScheme(); // clear any invalid preview cache
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		stopPreview();
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		stopPreview();
	}


	protected void previewKeyboard() {
		if (preview != null) {
			onBeforePreview();
			preview.resume();
		}
	}


	protected void pausePreview() {
		if (preview != null) {
			preview.pause();
		}
	}


	protected void stopPreview() {
		if (preview != null) {
			preview.stop();
		}
		if (activity != null) {
			activity.getSettings().reloadColorScheme(); // clear any invalid preview cache
		}
	}


	@Nullable
	protected AbstractColorScheme getInitialPreviewScheme() {
		return null;
	}


	private void onBeforePreview() {
		AbstractColorScheme scheme = getInitialPreviewScheme();
		if (activity != null && scheme != null) {
			activity.getSettings().setPreviewScheme(scheme);
		}
	}
}
