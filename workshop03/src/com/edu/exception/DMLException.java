package com.edu.exception;

// dml 로 이루어진 모든 실행문 예외처리
public class DMLException extends Exception {
	public DMLException (String message) {
		super(message);
		
	}
	public DMLException() {
		this("도서 추가, 삭제, 수정시 작업이 제대로 진행되지 않았습니다.");
	}
}
