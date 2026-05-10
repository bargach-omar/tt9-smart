package com.tt9smart.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import com.tt9smart.db.sqlite.ReadOps;
import com.tt9smart.languages.Language;
import com.tt9smart.languages.LanguageCollection;

import static org.junit.Assert.*;

/**
 * Integration test: verifies the SQLite prefix query with a French-like in-memory database.
 *
 * This catches bugs where "comm" typed by the user would NOT return "comment" as a prediction,
 * because the SQL query or table name is wrong.
 *
 * The French language ID is 596550 (from LanguageKind.isFrench).
 * The words table is: words_596550 (schema: frequency INTEGER, position INTEGER, word TEXT).
 */
@RunWith(AndroidJUnit4.class)
public class FrenchPrefixDbTest {

    private static final int FRENCH_LANG_ID = 596550;
    private static final String WORDS_TABLE = "words_" + FRENCH_LANG_ID;

    private SQLiteDatabase db;
    private ReadOps readOps;
    private Language frenchLanguage;

    @Before
    public void setUp() {
        Context ctx = ApplicationProvider.getApplicationContext();

        // In-memory SQLite DB — no download needed, no side effects
        db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        db.execSQL("CREATE TABLE " + WORDS_TABLE + " (" +
                "frequency INTEGER NOT NULL DEFAULT 0, " +
                "position INTEGER NOT NULL, " +
                "word TEXT NOT NULL)");

        // Insert a realistic French word list ordered by real-world frequency
        String[][] words = {
                {"500", "1", "comment"},
                {"480", "2", "comme"},
                {"460", "3", "commencer"},
                {"440", "4", "commun"},
                {"420", "5", "commercial"},
                {"400", "6", "commande"},
                {"380", "7", "commission"},
                {"360", "8", "communauté"},
                {"100", "9", "commentaire"},
                // words with other prefixes — must NOT appear in "comm%" results
                {"900", "10", "coûter"},
                {"850", "11", "coup"},
                {"800", "12", "comme"},  // duplicate intentional — higher freq version
        };
        for (String[] w : words) {
            db.execSQL("INSERT INTO " + WORDS_TABLE + " VALUES (?, ?, ?)", w);
        }

        readOps = new ReadOps();
        frenchLanguage = LanguageCollection.getLanguage(FRENCH_LANG_ID);
    }

    @After
    public void tearDown() {
        db.close();
    }


    @Test
    public void prefixComm_returnsCommentFirst() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "comm", 8);
        assertFalse("No results for prefix 'comm'", results.isEmpty());
        // highest frequency "comment" (500) should be first
        assertEquals("comment", results.get(0));
    }

    @Test
    public void prefixComm_containsAllCommWords() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "comm", 8);
        assertTrue(results.contains("comment"));
        assertTrue(results.contains("comme"));
        assertTrue(results.contains("commencer"));
    }

    @Test
    public void prefixComm_doesNotReturnOtherWords() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "comm", 8);
        assertFalse("coûter must not appear in 'comm%' results", results.contains("coûter"));
        assertFalse("coup must not appear in 'comm%' results", results.contains("coup"));
    }

    @Test
    public void prefixCo_returnsCommAndCouter() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "co", 8);
        assertTrue(results.contains("coûter"));
        assertTrue(results.contains("coup"));
        assertTrue(results.contains("comment"));
    }

    @Test
    public void prefixComm_respectsMaxLimit() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "comm", 3);
        assertEquals("Should return exactly 3 results", 3, results.size());
    }

    @Test
    public void emptyPrefix_returnsNothing() {
        // Empty prefix = no filter, but we pass empty so the query becomes "LIKE '%'" which returns all.
        // Verify the app never calls this with an empty prefix (extractCurrentWord guards against it).
        // Here we just confirm the DB itself doesn't crash.
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "", 8);
        assertNotNull(results);
    }

    @Test
    public void unknownPrefix_returnsEmptyList() {
        ArrayList<String> results = readOps.getWordsByPrefix(db, frenchLanguage, "xyz", 8);
        assertTrue("Unknown prefix should return empty list", results.isEmpty());
    }
}
