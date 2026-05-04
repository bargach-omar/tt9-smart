package com.tt9smart.db.entities;

import androidx.annotation.NonNull;

public class Word {
	public int frequency;
	public int position;
	public String word;

	public static Word create(@NonNull String word, int frequency, int position) {
		Word w = new Word();
		w.frequency = frequency;
		w.position = position;
		w.word = word;

		return w;
	}
}
