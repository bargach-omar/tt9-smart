package com.tt9smart.preferences.screens.modePredictive;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tt9smart.R;
import com.tt9smart.preferences.custom.EnhancedDropDownPreference;
import com.tt9smart.preferences.settings.SettingsStore;

public class DropDownOneKeyEmoji extends EnhancedDropDownPreference {
	public static final String NAME = "pref_one_key_emoji";

	public DropDownOneKeyEmoji(@NonNull Context context) { super(context); }
	public DropDownOneKeyEmoji(@NonNull Context context, @Nullable AttributeSet attrs) { super(context, attrs); }
	public DropDownOneKeyEmoji(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
	public DropDownOneKeyEmoji(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }

	@Override
	protected String getName() {
		return NAME;
	}

	@Override
	protected int getDisplayTitle() {
		return R.string.pref_one_key_emoji;
	}

	@Override
	public DropDownOneKeyEmoji populate(@NonNull SettingsStore settings) {
		for (OneKeyEmojiOptions.OPTIONS option : OneKeyEmojiOptions.getAll(settings)) {
			add(option.toString(), getOptionTitle(option));
		}
		commitOptions();
		setValue(settings.getOneKeyEmojiMode().toString());
		setDefaultValue(OneKeyEmojiOptions.DEFAULT);

		return this;
	}

	@NonNull
	private String getOptionTitle(OneKeyEmojiOptions.OPTIONS option) {
		Integer title = OneKeyEmojiOptions.OPTION_TITLES.get(option);
		return title == null ? option.toString() : getContext().getString(title);
	}
}
