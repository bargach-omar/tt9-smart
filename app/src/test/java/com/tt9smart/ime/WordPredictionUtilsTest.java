package com.tt9smart.ime;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the word prefix extraction used by SmartBar word predictions.
 *
 * Scenario: user types "bonj" in French SmartBar mode, then taps "bonjour" in the predictions bar.
 * acceptWordPrediction() must:
 *   1. Clear the composing letter ("j") FIRST
 *   2. Read the committed text before the cursor ("bon")
 *   3. Delete exactly those letters (3), not 4
 *   4. Insert "bonjour"
 *
 * The DB lookup (bonj → bonjour) and the actual text insertion both require a real Android device,
 * so they live in androidTest. These tests cover the pure-Java prefix logic that caused the bug
 * where deleting the composing letter AFTER reading the prefix produced a count that was too high,
 * deleting one extra character and erasing the whole word.
 */
public class WordPredictionUtilsTest {

	// --- extractCurrentWord ---

	@Test
	public void emptyInput_returnsEmpty() {
		assertEquals("", WordPredictionUtils.extractCurrentWord(""));
	}

	@Test
	public void nullInput_returnsEmpty() {
		assertEquals("", WordPredictionUtils.extractCurrentWord(null));
	}

	@Test
	public void committedBonj_returnsBonj() {
		// User pressed OK after "j" — "bonj" is fully committed, no composing.
		// Prefix must be "bonj" so we delete 4 chars before inserting "bonjour".
		assertEquals("bonj", WordPredictionUtils.extractCurrentWord("bonj"));
	}

	@Test
	public void committedBon_returnsBon() {
		// Composing "j" was cleared before this call.
		// Prefix is "bon" (3 chars) — deleting 3 then inserting "bonjour" gives "bonjour".
		assertEquals("bon", WordPredictionUtils.extractCurrentWord("bon"));
	}

	@Test
	public void committedBonjour_returnsBonjour() {
		assertEquals("bonjour", WordPredictionUtils.extractCurrentWord("bonjour"));
	}

	@Test
	public void endsWithSpace_returnsEmpty() {
		// Cursor is after a space — no word being typed, no prefix to delete.
		assertEquals("", WordPredictionUtils.extractCurrentWord("bonjour "));
	}

	@Test
	public void multipleWords_returnsLastWord() {
		// Only the word immediately before the cursor matters.
		assertEquals("monde", WordPredictionUtils.extractCurrentWord("hello monde"));
	}

	@Test
	public void nonLetterSeparator_returnsWordAfterSeparator() {
		assertEquals("bonjour", WordPredictionUtils.extractCurrentWord("1. bonjour"));
	}

	@Test
	public void singleLetter_returnsThatLetter() {
		// User just committed "b" — prefix is "b", delete 1, insert the prediction.
		assertEquals("b", WordPredictionUtils.extractCurrentWord("b"));
	}

	// --- regression: composing included in textBefore gives wrong count ---

	@Test
	public void regression_composingIncludedWouldGiveBonj_butWeShouldPassBon() {
		// Before the fix, getTextBeforeCursor() returned "bonj" (committed "bon" + composing "j").
		// extractCurrentWord("bonj") = 4, but only 3 chars were committed → deleted "bon" + char before it.
		// The fix: clear composing FIRST, then call this with "bon" → returns 3 → correct.
		assertEquals(3, WordPredictionUtils.extractCurrentWord("bon").length());
		assertEquals(4, WordPredictionUtils.extractCurrentWord("bonj").length());
		// Proof: if composing is cleared first, we pass "bon" and delete exactly 3 chars.
	}

	// --- cycling: getTextBeforeCursor() includes composing letter ---
	//
	// When the user types "bo" (committed) then presses key 6 (m/n/o),
	// getTextBeforeCursor() = committed + composing = "bom" / "bon" / "boo".
	// loadWordPredictions() is called AFTER setComposingText(), so it naturally
	// receives the full string including the cycling letter — no special handling needed.

	@Test
	public void cycling_firstLetter_prefixIncludesComposing() {
		// key 6 first press → composing="m", textBefore="bom"
		assertEquals("bom", WordPredictionUtils.extractCurrentWord("bom"));
	}

	@Test
	public void cycling_secondLetter_prefixChanges() {
		// key 6 second press → composing="n", textBefore="bon"
		assertEquals("bon", WordPredictionUtils.extractCurrentWord("bon"));
	}

	@Test
	public void cycling_thirdLetter_prefixChanges() {
		// key 6 third press → composing="o", textBefore="boo"
		assertEquals("boo", WordPredictionUtils.extractCurrentWord("boo"));
	}

	@Test
	public void cycling_eachPressGivesDifferentPrefix() {
		// Proof that the three cycling letters produce three distinct DB queries.
		String m = WordPredictionUtils.extractCurrentWord("bom");
		String n = WordPredictionUtils.extractCurrentWord("bon");
		String o = WordPredictionUtils.extractCurrentWord("boo");
		assertNotEquals(m, n);
		assertNotEquals(n, o);
		assertNotEquals(m, o);
	}

	// --- backspace: prefix shrinks character by character ---
	//
	// After each backspace, getTextBeforeCursor() is one character shorter.
	// loadWordPredictions() is called in onBackspace() so predictions update
	// immediately with the new shorter prefix.

	@Test
	public void backspace_fromFullWord_dropsLastLetter() {
		// typed "bonjour" fully committed, one backspace → textBefore="bonjou"
		assertEquals("bonjou", WordPredictionUtils.extractCurrentWord("bonjou"));
	}

	@Test
	public void backspace_eachStepShortensByOne() {
		// successive backspaces on "bon": "bon" → "bo" → "b" → ""
		assertEquals(3, WordPredictionUtils.extractCurrentWord("bon").length());
		assertEquals(2, WordPredictionUtils.extractCurrentWord("bo").length());
		assertEquals(1, WordPredictionUtils.extractCurrentWord("b").length());
		assertEquals(0, WordPredictionUtils.extractCurrentWord("").length());
	}

	@Test
	public void backspace_pastWordBoundary_returnsEmpty() {
		// "bonjour " (trailing space) → cursor is after space, no active word → no predictions
		assertEquals("", WordPredictionUtils.extractCurrentWord("bonjour "));
	}

	@Test
	public void backspace_removesSpace_resumesPreviousWord() {
		// user typed "bonjour " then backspaces the space → textBefore="bonjour"
		// predictions should reappear for "bonjour"
		assertEquals("bonjour", WordPredictionUtils.extractCurrentWord("bonjour"));
	}
}
