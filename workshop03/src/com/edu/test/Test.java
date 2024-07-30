package com.edu.test;

import java.sql.SQLException;
import java.util.List;

import com.edu.dao.BookDAO;
import com.edu.exception.DMLException;
import com.edu.exception.DuplicateIsbnException;
import com.edu.exception.RecordNotFoundException;
import com.edu.vo.Book;

public class Test {

	public static void main(String[] args) throws SQLException, RecordNotFoundException, DuplicateIsbnException, DMLException {
		BookDAO dao = BookDAO.getInstance();
		
//		dao.insertBook(new Book("a1101", "JAVA 기본", "자앤 기술연구소", "자앤 출판사", 23000, "기본"));
//		dao.insertBook(new Book("a1101", "JAVA 기본", "자앤 기술연구소", "자앤 출판사", 23000, "기본"));
//		dao.insertBook(new Book("a1102", "JAVA 중급", "자앤 기술연구소", "자앤 출판사", 25000, "중급"));
//		dao.insertBook(new Book("a1103", "JAVA 실전", "자앤 기술연구소", "자앤 출판사", 30000, "실전"));
		
		// 2.
//		printAllBooks(dao.listBooks());
		
		// 3.
//		System.out.println(dao.findBook("a1101"));
		
		// 4.
//		dao.insertBook(new Book("a1104", "JAVA 심화", "자앤 기술연구소", "자앤 출판사", 28000, "심화"));
		
		// 5.
//		dao.updateBook(new Book("a1101", "JAVA 기본", "자앤 기술연구소", "자앤 출판사", 20000, "기본"));
//		printAllBooks(dao.listBooks());
		
		// 6.
//		dao.deleteBook("a1103");
//		printAllBooks(dao.listBooks());
		
		// 7.
//		System.out.println(dao.count());
		
		// 8. 
		System.out.println(dao.findBook("JAVA 기본", "title"));
	
		// 9.
		System.out.println(dao.findBook("자앤 출판사", "publisher"));
		
		// 10.
		System.out.println(dao.findBook("자앤 기술연구소", "author"));
	
		// 11. 할인 가격
		System.out.println(dao.discountBooks("자앤 출판사", 15));
	
	}
	
	private static void printAllBooks(List<Book> list) {
		for (Book book : list) {
			System.out.println(book);
		}
	}
}
