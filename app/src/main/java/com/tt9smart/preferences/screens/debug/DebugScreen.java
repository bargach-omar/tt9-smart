package com.tt9smart.preferences.screens.debug;

import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.preferences.PreferencesActivity;
import com.tt9smart.preferences.items.ItemText;
import com.tt9smart.preferences.screens.BaseScreenFragment;
import com.tt9smart.util.sys.DeviceInfo;

public class DebugScreen extends BaseScreenFragment {
	public static final String NAME = "Debug";

	private static final String DEVICE_INFO_CONTAINER = "pref_device_info";

	public DebugScreen() { super(); }
	public DebugScreen(@Nullable PreferencesActivity activity) { super(activity); }

	@Override public String getName() { return NAME; }
	@Override protected int getTitle() { return R.string.pref_category_debug_options; }
	@Override protected int getXml() { return R.xml.prefs_screen_debug; }

	@Override
	protected void onCreate() {
		(new ItemText(activity, findPreference(DEVICE_INFO_CONTAINER))).populate(new DeviceInfo().toString()).enableClickHandler();
		(new ItemExportLogcat(findPreference(ItemExportLogcat.NAME), activity)).enableClickHandler();
		(new ItemDemoMode(findPreference(ItemDemoMode.NAME), activity)).populate().enableClickHandler();

		resetFontSize(false);
	}
}
