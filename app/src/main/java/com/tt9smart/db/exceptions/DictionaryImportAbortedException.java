package com.tt9smart.db.exceptions;

public class DictionaryImportAbortedException extends Exception{
	public DictionaryImportAbortedException() {
		super("Dictionary import stopped by request");
	}
}
