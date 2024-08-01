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
		
		// 8. 제목검색
//		System.out.println(dao.findBook("JAVA 기본", "title"));
	
		// 9. 출판사검색
//		System.out.println(dao.findBook("자앤 출판사", "publisher"));
		
		// 10. TODO 저자 검색
//		System.out.println(dao.findBook("자앤 기술연구소", "author"));
//		System.out.println(dao.findBook("김", "author"));
	
		// 11. 할인 가격
//		System.out.println(dao.discountBooks("자앤 출판사", 15));
		
		// TODO 
		// 12. 김% 검색
		
		/*
		  13. Author 테이블에 있는 저자명 별로 출간된 도서들을 
		  도서명, 출판사, 가격, 저자명을 출력하는 기능을 구현한다.
		*/
		// 
		
		
		/*
		 14. Book 테이블에 있는 title와 publisher를 이용하여 서로의 관계를 다음과 같이
 		 출력되도록 기능을 구현한다. ( ‘IoT세상은 미래닷컴에서 출판했다’)
		*/
		System.out.println(dao.listbooksByTitle());
		
//		System.out.print(dao.);
		
		/*
 			검색할때 : Book 내에 author 번호랑 이름 다 넣어서 ArrayList<Book>으로 반환해야함
 			나머지는 그냥 DAO에서 출력
		*/
		
	}
	
	private static void printAllBooks(List<Book> list) {
		for (Book book : list) {
			System.out.println(book);
		}
	}
}
