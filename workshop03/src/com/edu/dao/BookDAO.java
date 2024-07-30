package com.edu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.edu.exception.DMLException;
import com.edu.exception.DuplicateIsbnException;
import com.edu.exception.RecordNotFoundException;
import com.edu.vo.Book;

import config.ServerInfo;

public class BookDAO {
	private static BookDAO dao = new BookDAO();

	private BookDAO() {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("드라이브 로딩 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이브 로딩 실패");
		}
	}

	public static BookDAO getInstance() {
		return dao;
	}

	// count() : 전체 책의 숫자
	// isbn : 공백 처리 방법

	// 공통적인 로직
	// getConnection
	public Connection getConnect() throws SQLException {
		System.out.println("db연결 성공....");
		return DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
	}

	public void close(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}

	public void close(PreparedStatement ps) throws SQLException {
		if (ps != null)
			ps.close();
	}

	public void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}

	// 비즈니스 로직
	// insertBook
	public void insertBook(Book book) throws DuplicateIsbnException, DMLException {
		String query = "INSERT INTO book (isbn, title, author, publisher, price, description)  VALUES(?,?,?,?,?,?)";

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, book.getIsbn());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getPublisher());
			ps.setInt(5, book.getPrice());
			ps.setString(6, book.getDescription());

			System.out.println("[ Result OK Message ] => " + ps.executeUpdate() + " ROW INSERT OK");

		} catch (SQLIntegrityConstraintViolationException e) { // 중복오류
			throw new DuplicateIsbnException("[ Result Error Message ] => " + "이미 존재하는 도서번호 입니다.");
		} catch (SQLException e) { // 문법오류
			throw new DMLException("[ Result Error Message ] => " + "도서 추가시 문제가 발생해서 이뤄지지 않았습니다.");
		}

	}

	// updateBook
	public void updateBook(Book book) throws DMLException, RecordNotFoundException {
		String query = "UPDATE book SET title=?, author=?, publisher=?, price=?, description=? WHERE isbn=?)";

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, book.getTitle().trim());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getPublisher());
			ps.setInt(4, book.getPrice());
			ps.setNString(5, book.getDescription());

			if (ps.executeUpdate() == 0) {
				throw new RecordNotFoundException("해당하는 " + book.getIsbn() + " 은 존재하지 않습니다.");
			}

		} catch (SQLException e) {
			throw new DMLException("도서 추가시 문제가 발생해서 이뤄지지 않았습니다.");
		}

	}

	// deleteBook
	public void deleteBook(String isbn) throws SQLException, RecordNotFoundException, DMLException {
		String query = "DELETE FROM book WHERE isbn=?";

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, isbn);

			if (ps.executeUpdate() == 0) {
				throw new RecordNotFoundException("존재하지않는 도서입니다.");
			}
			System.out.println("[ Result OK Message ] => 도서 삭제 완료");

		} catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => 문제가 발생하여 도서 삭제가 이뤄지지 않았습니다.");
		}
	}

	// findBook
	public Book findBook(String isbn) throws RecordNotFoundException, DMLException {
		String query = "SELECT isbn, title, author, publisher, price, description FROM book WHERE isbn=trim(?)";

		Book book = null;

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {

			ps.setString(1, isbn);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) { // 찾고자 하는 책이 없다면
				book = new Book(isbn, rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
						rs.getInt("price"), rs.getString("description"));
			} else { // 없다면
				throw new RecordNotFoundException("[ Result Error Message ] => 해당 " + isbn + " 번호의 도서가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => 문제가 발생하여 도서 조회가 이뤄지지 않았습니다.");
		}

		return book;
	}

	// 도서 제목 검색
	public List<Book> findBook(String keyword, String type) throws RecordNotFoundException, DMLException {
		String query = "SELECT isbn, title, author, publisher, price, description FROM book WHERE "+type+"=?";
		List<Book> list = new ArrayList<>();

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {

			ps.setString(1, keyword);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// 공통 메서드 호출
				list.add(handleAddList(rs));
			}
			if (list.size() == 0) {
				throw new RecordNotFoundException("[ Result Error Message ] => 해당 " + keyword + " 제목의 도서가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => 문제가 발생하여 도서 조회가 이뤄지지 않았습니다.");
		}

		return list;
	}
	
	// 가격
	public List<Book> findBook(int price) throws RecordNotFoundException, DMLException {
		String query = "SELECT isbn, title, author, publisher, price, description FROM book WHERE price=?";

		List<Book> list = new ArrayList<>();
		
		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {

			ps.setInt(1, price);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// 공통 메서드 호출
				list.add(handleAddList(rs));
			}
			if (list.size() == 0) {
				throw new RecordNotFoundException("[ Result Error Message ] => 해당 가격의 도서가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => 문제가 발생하여 도서 조회가 이뤄지지 않았습니다.");
		}

		return list;
	}

	// listBooks
	public List<Book> listBooks() throws SQLException, RecordNotFoundException {
		List<Book> list = new ArrayList<>();
		String query = "SELECT isbn, title, author, publisher, price, description FROM book";
		try (Connection conn = getConnect();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(handleAddList(rs));
			}

			if (list.size() == 0) {
				throw new RecordNotFoundException("도서 목록이 없습니다.");
			}
		}
		return list;
	}

	// 총 도서갯수를 구하는 비즈니스 로직... count() 행의수를 리턴하는 그룹함수
	public int count() throws SQLException, DMLException {
		String query = "SELECT count(isbn) count FROM book";
		int totalBooks = 0;

		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
//	            totalBooks = rs.getInt(1); // 첫 번째 열의 값
				totalBooks = rs.getInt("count");
			}
		} catch (SQLException e) {
			throw new DMLException("[ Result Error Message ] => 문제가 발생하여 도서의 총 갯수를 알기 힘듭니다.");
		}
		return totalBooks;
	}

	public List<Book> discountBooks(String publisher, int percent) throws SQLException, RecordNotFoundException {
	    String query = "SELECT isbn, title, author, publisher, price, description FROM book WHERE publisher=?";

	    double discount = percent * 0.01;

	    List<Book> list = new ArrayList<>();
	    try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, publisher);

	        // 쿼리 실행
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            list.add(new Book(
	                rs.getString("isbn"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getString("publisher"),
	                (int) (rs.getInt("price") - Math.ceil(rs.getInt("price") * discount)), // 할인 적용
	                rs.getString("description")
	            ));
	        }

	        if (list.isEmpty()) {
	            throw new RecordNotFoundException("해당 출판사에 대한 도서가 없습니다.");
	        }
	    }
	    return list;
	}

	// 도서 리스트 추가 기능 (공통)
	public Book handleAddList(ResultSet rs) throws SQLException {
		Book book = new Book(
				rs.getString("isbn"), 
				rs.getString("title"), 
				rs.getString("author"),
				rs.getString("publisher"), 
				rs.getInt("price"), 
				rs.getString("description")
			);
		return book;
	}

}
