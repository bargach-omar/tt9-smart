package com.tt9smart.ime;

class WordPredictionUtils {
	/**
	 * Extracts the word being typed at the end of the committed text before the cursor.
	 * Used to determine how many characters to delete when a word prediction is tapped.
	 *
	 * NOTE: must be called AFTER clearing composing text, otherwise getTextBeforeCursor()
	 * includes the composing letter in textBefore, causing the prefix length to be off by one
	 * and deleting one extra character (the character before the current word).
	 */
	static String extractCurrentWord(String textBefore) {
		if (textBefore == null || textBefore.isEmpty()) return "";
		int end = textBefore.length();
		int start = end;
		while (start > 0 && Character.isLetter(textBefore.charAt(start - 1))) {
			start--;
		}
		return textBefore.substring(start, end);
	}
}
