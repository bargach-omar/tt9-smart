package com.tt9smart.languages.exceptions;

import com.tt9smart.languages.Language;

public class InvalidLanguageCharactersException extends Exception {

	public InvalidLanguageCharactersException(Language language, String extraMessage) {
		super("Some characters are not supported in language: " + language.getName() + ". " + extraMessage);
	}

}

