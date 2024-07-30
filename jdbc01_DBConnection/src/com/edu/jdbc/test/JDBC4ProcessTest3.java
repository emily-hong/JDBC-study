package com.edu.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ServerInfo;
/*
 * 서버주소
 * 계정명
 * 계정 비번
 * 이런 값들은 프로그램 코드에 노출되면 안되는 값들이다.
 * 프로그램에 이런 실제값이 들어가는 것 == 하드코딩이라 한다.
 * 
 * 서버의 파편적인 정보를 외부에 모듈화(메타데이터화) 시켜야 한다.
 * 인터페이스 사용 예제
 */
public class JDBC4ProcessTest3 {
	
	public JDBC4ProcessTest3() { 
		try {
			// 작업 2.
			Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
			System.out.println("2. DB연결 성공..");
			
			// 작업 3.
			// INSERT
			/*
			String query = "INSERT INTO custom (id, name, address) VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query); // 실행x, 미리 쿼리문이 컴파일만 먼저 수행됨
			System.out.println("3. PreparedStatement 생성...");
			
			// 작업 4. 바인딩 및 쿼리문 실행
			ps.setInt(1, 444);
			ps.setString(2, "황선우");
			ps.setString(3, "서울");
			
			System.out.println(ps.executeUpdate() + " ROW INSERT OK~!!");
			*/
			
			// DELETE...id값이 2인 사람을 삭제
			/*
			String query ="DELETE FROM custom WHERE id=?";
			
			PreparedStatement ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement 생성...");
			ps.setInt(1, 222); // id 222 찾아서 삭제
			
			System.out.println(ps.executeUpdate() + " row DELETE OK~!!");
			*/
			
			// UPDATE id 값이 444사람의 정볼르 수정.. name(오상욱), address(광주)
			/*
			String query ="UPDATE custom SET name=?, address=? WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			
			System.out.println("PreparedStatement 생성...");
			ps.setString(1, "오상욱");
			ps.setString(2, "광주");
			ps.setInt(3, 444);
			// pk 지정된 컬럼은 수정의 대상이 아니다.
			
			System.out.println(ps.executeUpdate() + " row UPDATE OK~!!");
			*/
			
			// int excuteUpdate()의 int... : modify된 행의수 .. 0(실패) 1(성공)
			
			// 조회하기 
			// ResultSet executeQuery() 사용 : 반환 타입이 ResultSet, table에 있는 데이터를 이곳에 담아서 반환
			// ResultSet
			// 	- 데이터베이스와 붙어있다. next()로 행의 유무 확인, 없으면 while 빠져나오는 구조, 서버나클라이언트사이드렌더링과 관련 없이 데이터베이스 안에서 이루어짐
			String query ="SELECT id, name, address FROM custom";
			PreparedStatement ps = conn.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			System.out.println("----------------------------------");
			while(rs.next()) {
//				System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3)); // 순번으로 조회
				System.out.println(rs.getInt("id") + ", " + rs.getString("name") + ", " + rs.getString("address")); // 컬럼명으로 조회
			}
			System.out.println("----------------------------------");
						
		}catch (SQLException e) {
			System.out.println("DB Connection Fail...");
		}
	}
	
	
	public static void main(String[] args) {
		new JDBC4ProcessTest3();		
	}// main
		
	// 작업 1.
	static {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("드라이버 로딩 성공..");
		}catch(ClassNotFoundException e){
			System.out.println("드라이버 로딩 실패..");
		}
	}// static
}

/*
	고객등록 ----> addCustomer(Customer c){
		1. db연결
		2. preparedStatement 객체생성
		3. 바인딩
		4. query 실행 (executeUpdate() -> 1)
	}
	회원탈퇴 ----> deleteCustomer(int id){
		1. db연결
		2. preparedStatement 객체생성
		3. 바인딩
		4. query 실행 (executeUpdate() -> 1)
	}
	회원정보수정..회원정보검색........................
	
 	클라이언트 요청 하나당 함수 실행 한번하고 DB연결 끊어야함
 	위의 작업이 진행되기전에 선행되어야하는 작업 -> 드라이버 로딩..
 	
 	*** 드라이버 로딩
 	- 생성자에서 X, static initialization block O
 	*** 전체 순서
 	1. 드라이버 로딩 (글로벌하게 초기화 선행)
 	2. db연결
	3. preparedStatement 객체생성
	4. 바인딩
	5. query 실행 (executeUpdate() -> 1)
 	
*/
