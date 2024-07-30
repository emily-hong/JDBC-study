package com.edu.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 	JDBC 기본 4단계 작업
 	1. 서버 정보를 담고 있는 드라이버 로딩시킴 (build 파일 경로를 잡아야함)
 	2. 디비 서버 연결...Connection 객체 만들어지고 반환됨
 	3. Connection이 가지고 있는 함수를 통해 PreparedStatement 객체를 생성함 (쿼리를 실행하는 함수를 가지고 있음)
 	4. SQL 쿼리문 실행
 */
public class JDBC4ProcessTest1 {
	public JDBC4ProcessTest1() { 
		try {
			// 1. 실행하려면 build 파일 경로를 잡아야함
			Class.forName("com.mysql.cj.jdbc.Driver"); // FQCN. 드라이버의 실제이름
			System.out.println("Driver Loading Success..."); // 확인
			
			// 2. 
			String url = 
					"jdbc:mysql://127.0.0.1:3306/kosta?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";	// 주소
			Connection conn = DriverManager.getConnection(url, "root", "1234"); // SQLException catch 밑에서 잡기
			System.out.println("DBConnection...."); // 확인
			
			// 3.
			String query = "INSERT INTO custom (id, name, address) VALUES(?,?,?)"; // values안에는 ?(물음표)로 대신함
			PreparedStatement ps = conn.prepareStatement(query); // 실행x, 컴파일만 됨 -> ps.excuteUpdate(); 부분에서 qery실행(db입력됨) 중요***
			System.out.println("PreparedStatement Creating...."); // 확인
			
			// 4. 
			// 	1) ?에 값 바인딩 (set..)
			// 	2) sql실행..이때 DB에 데이터가 입력됨
			ps.setInt(1, 111);
			ps.setString(2, "James");
			ps.setString(3, "NY");
			
			int row = ps.executeUpdate(); // 반환: 성공1, 실패0
			System.out.println(row + " ROW Record 등록 성공~!!");
									
		}catch (ClassNotFoundException e) {
			System.out.println("Driver Loading Fail...");
		}catch (SQLException e) {
			System.out.println("Connection Fail...");
		}
		
	}
	
	
	// main
	public static void main(String[] args) {
		new JDBC4ProcessTest1();
		
		
	}// main

}
