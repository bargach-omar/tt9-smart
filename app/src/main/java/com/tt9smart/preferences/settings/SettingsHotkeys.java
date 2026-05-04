package com.tt9smart.preferences.settings;

import android.content.Context;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

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
import com.tt9smart.commands.Command;
import com.tt9smart.commands.CommandCollection;
import com.tt9smart.ime.helpers.Hotkey;
import com.tt9smart.util.Logger;

public class SettingsHotkeys extends SettingsVirtualNumpad {
	private static final String HOTKEY_VERSION = "hotkeys_v6";


	SettingsHotkeys(Context context) { super(context); }


	public boolean areHotkeysInitialized() {
		return !prefs.getBoolean(HOTKEY_VERSION, false);
	}


	public void setDefaultKeys() {
		final Hotkey hotkey = new Hotkey(context.getResources());

		for (String key : hotkey.getAllKeys()) {
			String keycode = hotkey.getCode(key);
			getPrefsEditor().putString(key, keycode);

			// if no backspace hotkey is set in the resources, set a sane default
			if (CmdBackspace.ID.equals(key) && String.valueOf(KeyEvent.KEYCODE_UNKNOWN).equals(keycode)) {
				setDefaultBackspace();
			}
		}

		getPrefsEditor().putBoolean(HOTKEY_VERSION, true).apply();
	}


	/**
	 * When a standard "Backspace" hardware key is available, "Backspace" hotkey association is not necessary,
	 * so it will be left out blank, to allow the hardware key do its job.
	 * When the on-screen keyboard is on, "Back" is also not associated, because it will cause weird user
	 * experience. Instead, the on-screen "Backspace" key can be used.
	 */
	private void setDefaultBackspace() {
		if (
			KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_CLEAR)
			|| KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_DEL)
			|| isMainLayoutLarge()
		) {
			getPrefsEditor().putString(CmdBackspace.ID, String.valueOf(KeyEvent.KEYCODE_UNKNOWN));
		} else {
			getPrefsEditor().putString(CmdBackspace.ID, String.valueOf(KeyEvent.KEYCODE_BACK));
		}
	}


	public int getFunctionKey(String functionName) {
		return getStringifiedInt(functionName, KeyEvent.KEYCODE_UNKNOWN);
	}


	public void setFunctionKey(String functionName, int keyCode) {
		if (isValidFunction(functionName)) {
			Logger.d(LOG_TAG, "Setting hotkey for function: '" + functionName + "' to " + keyCode);
			getPrefsEditor().putString(functionName, String.valueOf(keyCode)).apply();
		} else {
			Logger.w(LOG_TAG,"Not setting a hotkey for invalid function: '" + functionName + "'");
		}
	}


	public int getKeyAddWord() {
		return getFunctionKey(CmdAddWord.ID);
	}
	public int getKeyBackspace() {
		return getFunctionKey(CmdBackspace.ID);
	}
	public int getKeyCommandPalette() {
		return getFunctionKey(CmdCommandPalette.ID);
	}
	public int getKeyEditText() {
		return getFunctionKey(CmdEditText.ID);
	}
	public int getKeyEditWord() {
		return getFunctionKey(CmdEditWord.ID);
	}
	public int getKeyFilterClear() {
		return getFunctionKey(CmdFilterClear.ID);
	}
	public int getKeyFilterSuggestions() {
		return getFunctionKey(CmdFilterSuggestions.ID);
	}
	public int getKeyPreviousSuggestion() {
		return getFunctionKey(CmdSuggestionPrevious.ID);
	}
	public int getKeyNextSuggestion() {
		return getFunctionKey(CmdSuggestionNext.ID);
	}
	public int getKeyNextInputMode() {
		return getFunctionKey(CmdNextInputMode.ID);
	}
	public int getKeyNextLanguage() {
		return getFunctionKey(CmdNextLanguage.ID);
	}
	public int getKeySelectKeyboard() {
		return getFunctionKey(CmdSelectKeyboard.ID);
	}
	public int getKeyShift() {
		return getFunctionKey(CmdShift.ID);
	}
	public int getKeySpaceKorean() {
		return getFunctionKey(CmdSpaceKorean.ID);
	}
	public int getKeyShowSettings() {
		return getFunctionKey(CmdShowSettings.ID);
	}
	public int getKeyUndo() {
		return getFunctionKey(CmdUndo.ID);
	}
	public int getKeyRedo() {
		return getFunctionKey(CmdRedo.ID);
	}
	public int getKeyVoiceInput() {
		return getFunctionKey(CmdVoiceInput.ID);
	}


	public String getFunction(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
			return null;
		}

		for (Command cmd : CommandCollection.getHotkeyCommands()) {
			if (keyCode == getFunctionKey(cmd.getId())) {
				return cmd.getId();
			}
		}

		return null;
	}


	private boolean isValidFunction(String functionName) {
		for (Command cmd : CommandCollection.getHotkeyCommands()) {
			if (cmd.getId().equals(functionName)) {
				return true;
			}
		}
		return false;
	}
}
