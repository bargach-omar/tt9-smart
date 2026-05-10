package com.tt9smart.test;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Integration test: verifies that the delete-then-insert sequence used by
 * acceptWordPredictionWithPrefix() works correctly on a real Android InputConnection.
 *
 * Bug reproduced: typing "comm" then accepting "comment" resulted in "commcomment"
 * instead of "comment". The fix deletes the prefix ("comm") before inserting the
 * full prediction ("comment").
 *
 * This test isolates the InputConnection operations to confirm:
 *   1. deleteSurroundingText(n, 0) correctly removes the prefix
 *   2. setComposingText + finishComposingText inserts the prediction
 *   3. The result is the prediction alone, not prefix + prediction
 */
@RunWith(AndroidJUnit4.class)
public class WordPredictionAcceptanceTest {

    /**
     * Minimal Activity with an EditText — only needed to get a real InputConnection.
     */
    public static class TestActivity extends AppCompatActivity {
        public EditText editText;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            editText = new EditText(this);
            editText.setId(android.R.id.edit);
            LinearLayout layout = new LinearLayout(this);
            layout.addView(editText);
            setContentView(layout);
        }
    }


    /**
     * Simulates acceptWordPredictionWithPrefix("comment", "comm") on a real InputConnection
     * and verifies the result is "comment", not "commcomment".
     *
     * This is the exact sequence executed in SuggestionHandler when the user taps a prediction.
     */
    @Test
    public void acceptCommentWithCommPrefix_resultIsComment() throws Exception {
        try (ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                EditText et = activity.editText;
                et.requestFocus();

                // Get an InputConnection (as the IME would)
                InputConnection conn = et.onCreateInputConnection(new EditorInfo());
                assertNotNull("Could not get InputConnection", conn);

                // --- Simulate typing "comm" (4 chars, all auto-accepted) ---
                conn.setComposingText("c", 1);
                conn.finishComposingText();
                conn.setComposingText("o", 1);
                conn.finishComposingText();
                conn.setComposingText("m", 1);
                conn.finishComposingText();
                conn.setComposingText("m", 1);
                conn.finishComposingText();

                // Verify the field has "comm"
                CharSequence before = conn.getTextBeforeCursor(50, 0);
                assertEquals("Setup failed: expected 'comm' in field", "comm", before != null ? before.toString() : "");

                // --- Simulate acceptWordPredictionWithPrefix("comment", "comm") ---

                // Step 1: finish composing directly — do NOT call setComposingText("") first because
                // that creates an empty composing region which breaks deleteSurroundingText.
                conn.finishComposingText();

                // Step 2: delete the typed prefix (4 chars)
                boolean deleted = conn.deleteSurroundingText(4, 0);
                assertTrue("deleteSurroundingText(4, 0) should return true", deleted);

                // Step 3: insert the full prediction
                conn.setComposingText("comment", 1);
                conn.finishComposingText();

                // --- Verify ---
                CharSequence result = conn.getTextBeforeCursor(50, 0);
                String text = result != null ? result.toString() : "";
                assertEquals(
                    "Expected 'comment' but got '" + text + "' — delete+insert did not replace prefix correctly",
                    "comment",
                    text
                );
            });
        }
    }


    /**
     * Same test for the "coûter" case (prefix "co" = 2 chars).
     * This case worked before the fix — ensures we don't break it.
     */
    @Test
    public void acceptCouterWithCoPrefix_resultIsCouter() throws Exception {
        try (ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                EditText et = activity.editText;
                et.requestFocus();

                InputConnection conn = et.onCreateInputConnection(new EditorInfo());
                assertNotNull(conn);

                conn.setComposingText("c", 1);
                conn.finishComposingText();
                conn.setComposingText("o", 1);
                conn.finishComposingText();

                CharSequence before = conn.getTextBeforeCursor(50, 0);
                assertEquals("co", before != null ? before.toString() : "");

                conn.setComposingText("", 1);
                conn.finishComposingText();
                conn.deleteSurroundingText(2, 0);
                conn.setComposingText("coûter", 1);
                conn.finishComposingText();

                CharSequence result = conn.getTextBeforeCursor(50, 0);
                assertEquals("coûter", result != null ? result.toString() : "");
            });
        }
    }


    /**
     * Verifies the case where the last character is still composing when the user taps.
     *
     * If the user taps "comment" while the 2nd 'm' is mid-cycle (composing), the IME
     * must first clear the composing ('m' is replaced by ""), then delete the committed
     * prefix ("com" = 3 chars), then insert "comment".
     *
     * Result must be "comment", not "commcomment" or "comcomment".
     */
    @Test
    public void acceptCommentWhileLastCharStillComposing_resultIsComment() throws Exception {
        try (ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                EditText et = activity.editText;
                et.requestFocus();

                InputConnection conn = et.onCreateInputConnection(new EditorInfo());
                assertNotNull(conn);

                // Type "com" fully committed
                conn.setComposingText("c", 1);
                conn.finishComposingText();
                conn.setComposingText("o", 1);
                conn.finishComposingText();
                conn.setComposingText("m", 1);
                conn.finishComposingText();

                // 2nd 'm' is STILL COMPOSING (user tapped before auto-accept fired)
                conn.setComposingText("m", 1);
                // Do NOT call finishComposingText() — simulates mid-cycle state

                // acceptWordPredictionWithPrefix sees composingText="m" and clears it:
                conn.setComposingText("", 1);
                conn.finishComposingText();

                // After clearing composing, text before cursor is "com" (3 chars committed)
                CharSequence afterClear = conn.getTextBeforeCursor(50, 0);
                String afterClearStr = afterClear != null ? afterClear.toString() : "";
                assertEquals("After clearing composing, should have 'com' committed", "com", afterClearStr);

                // Now delete "com" (3 chars) and insert "comment"
                conn.deleteSurroundingText(3, 0);
                conn.setComposingText("comment", 1);
                conn.finishComposingText();

                CharSequence result = conn.getTextBeforeCursor(50, 0);
                assertEquals("comment", result != null ? result.toString() : "");
            });
        }
    }


    /**
     * Edge case: prefix in the middle of a sentence.
     * User typed "Je veux comm" — accepting "comment" should give "Je veux comment".
     */
    @Test
    public void acceptPredictionMidSentence_onlyReplacesPrefix() throws Exception {
        try (ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                EditText et = activity.editText;
                et.requestFocus();

                InputConnection conn = et.onCreateInputConnection(new EditorInfo());
                assertNotNull(conn);

                // Type "Je veux comm" (the "Je veux " part is already committed)
                conn.commitText("Je veux ", 1);
                conn.setComposingText("c", 1);
                conn.finishComposingText();
                conn.setComposingText("o", 1);
                conn.finishComposingText();
                conn.setComposingText("m", 1);
                conn.finishComposingText();
                conn.setComposingText("m", 1);
                conn.finishComposingText();

                CharSequence before = conn.getTextBeforeCursor(50, 0);
                assertEquals("Je veux comm", before != null ? before.toString() : "");

                // Accept "comment" with prefix "comm"
                conn.setComposingText("", 1);
                conn.finishComposingText();
                conn.deleteSurroundingText(4, 0);
                conn.setComposingText("comment", 1);
                conn.finishComposingText();

                CharSequence result = conn.getTextBeforeCursor(50, 0);
                assertEquals("Je veux comment", result != null ? result.toString() : "");
            });
        }
    }
}
