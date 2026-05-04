package com.tt9smart.db.exceptions;

public class DictionaryImportException extends Exception {
	public final long line;

	public DictionaryImportException(String message, long line) {
		super(message);
		this.line = line;
	}
}
