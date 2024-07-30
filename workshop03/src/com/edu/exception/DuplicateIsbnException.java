package com.edu.exception;

public class DuplicateIsbnException extends Exception {
	public DuplicateIsbnException (String message) {
		super(message);
		
	}
	public DuplicateIsbnException() {
		this("Duplicate isbn... 이미 isbn이 존재하는 도서 입니다. ");
	}
}
