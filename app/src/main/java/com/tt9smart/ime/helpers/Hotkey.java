package com.tt9smart.ime.helpers;

import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.tt9smart.R;
import com.tt9smart.commands.CmdAddWord;
import com.tt9smart.commands.CmdBackspace;
import com.tt9smart.commands.CmdCommandPalette;
import com.tt9smart.commands.CmdEditText;
import com.tt9smart.commands.CmdEditWord;
import com.tt9smart.commands.CmdFilterClear;
import com.tt9smart.commands.CmdFilterSuggestions;
import com.tt9smart.commands.CmdNextInputMode;
import com.tt9smart.commands.CmdNextLanguage;
import com.tt9smart.commands.CmdRedo;
import com.tt9smart.commands.CmdSelectKeyboard;
import com.tt9smart.commands.CmdShift;
import com.tt9smart.commands.CmdShowSettings;
import com.tt9smart.commands.CmdSpaceKorean;
import com.tt9smart.commands.CmdSuggestionNext;
import com.tt9smart.commands.CmdSuggestionPrevious;
import com.tt9smart.commands.CmdUndo;
import com.tt9smart.commands.CmdVoiceInput;

public class Hotkey {
	private final Resources resources;
	private final HashMap<String, Integer> defaults;

	public Hotkey(@NonNull Resources resources) {
		this.resources = resources;

		defaults = new HashMap<>();
		defaults.put(CmdAddWord.ID, R.integer.hotkey_add_word);
		defaults.put(CmdBackspace.ID, R.integer.hotkey_backspace);
		defaults.put(CmdCommandPalette.ID, R.integer.hotkey_command_palette);
		defaults.put(CmdEditText.ID, R.integer.hotkey_edit_text);
		defaults.put(CmdEditWord.ID, R.integer.hotkey_edit_word);
		defaults.put(CmdFilterClear.ID, R.integer.hotkey_filter_clear);
		defaults.put(CmdFilterSuggestions.ID, R.integer.hotkey_filter_suggestions);
		defaults.put(CmdNextInputMode.ID, R.integer.hotkey_next_input_mode);
		defaults.put(CmdNextLanguage.ID, R.integer.hotkey_next_language);
		defaults.put(CmdSelectKeyboard.ID, R.integer.hotkey_select_keyboard);
		defaults.put(CmdShift.ID, R.integer.hotkey_shift);
		defaults.put(CmdShowSettings.ID, R.integer.hotkey_show_settings);
		defaults.put(CmdSpaceKorean.ID, R.integer.hotkey_space_korean);
		defaults.put(CmdSuggestionNext.ID, R.integer.hotkey_next_suggestion);
		defaults.put(CmdSuggestionPrevious.ID, R.integer.hotkey_previous_suggestion);
		defaults.put(CmdUndo.ID, R.integer.hotkey_undo);
		defaults.put(CmdRedo.ID, R.integer.hotkey_redo);
		defaults.put(CmdVoiceInput.ID, R.integer.hotkey_voice_input);
	}

	public Set<String> getAllKeys() {
		return new HashSet<>(defaults.keySet());
	}

	public String getCode(@Nullable String key) {
		int keyCode = getCodeByResId(defaults.getOrDefault(key, 0));

		if (CmdSpaceKorean.ID.equals(key) && keyCode == KeyEvent.KEYCODE_UNKNOWN) {
			keyCode = getCodeByResId(R.integer.hotkey_space_korean_fallback);
		}

		return String.valueOf(keyCode);
	}

	private int getCodeByResId(@Nullable Integer resId) {
		if (resId == null || resId == 0) {
			return KeyEvent.KEYCODE_UNKNOWN;
		}

		try {
			int keyCode = resources.getInteger(resId);
			return KeyCharacterMap.deviceHasKey(Math.abs(keyCode)) ? keyCode : KeyEvent.KEYCODE_UNKNOWN;
		} catch (Resources.NotFoundException e) {
			return KeyEvent.KEYCODE_UNKNOWN;
		}
	}
}
