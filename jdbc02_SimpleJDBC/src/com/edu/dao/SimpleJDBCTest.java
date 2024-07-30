package com.edu.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.edu.vo.Custom;

import config.ServerInfo;

public class SimpleJDBCTest {
	static String driver;
	static String url;
	static String user;
	static String pass;
	static String query;
	
	public SimpleJDBCTest() throws Exception {
		// 드라이버 로딩..
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("드라이버로딩 성공");
	}
	
	// 고정적으로 각 메소드마다 반복되는 로직을 공통 로직으로 정의..
	public Connection getConnect() throws SQLException{
		// 1. 디비연결
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("addCustom 2. 디비서버 연결.. 성공");
		
		return conn;
	}
	// 열린 순서 반대로 닫아준다...
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	}
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
		closeAll(ps, conn);
	}
	
	public void addCustom(Custom c) throws Exception {
		Connection conn =  null;
		// 1
		conn = getConnect();
		
		// 2. PrepareStatement 생성
		String query = "INSERT INTO custom (id, name, address) VALUES(?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(query);

		// 3. 바인딩
		ps.setInt(1, c.getId());
		ps.setString(2, c.getName());
		ps.setString(3, c.getAddress());
		
		// 4. 쿼리문 실행
		System.out.println(ps.executeUpdate() + " row insert ok");
		
		// 5. 자원반환
		closeAll(ps,conn);
	}
	public Custom getCustom(int id) throws Exception {	// Business Logic(중요한 로직...디비Access Logic)
		Custom custom = null;
		Connection conn =  null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// 1
		conn = getConnect();
	
		// 2. PrepareStatement 생성
		String query = "SELECT id, name, address FROM custom WHERE id=?";
		ps = conn.prepareStatement(query);
		
		// 3. 바인딩
		ps.setInt(1, id);
		
		// 4. 쿼리문 실행
		rs = ps.executeQuery();
		
		if(rs.next())
			custom = new Custom(id, 
								rs.getString("name"), 
								rs.getString("address"));
		// 5. 자원반환
		closeAll(ps,conn);
		
		return custom;
	}
	
	public ArrayList<Custom> getCustom() throws Exception {
		ArrayList<Custom> list = new ArrayList<Custom>();
		Connection conn =  null;
		// 1
		conn = getConnect();
	
		// 2. PrepareStatement 생성
		String query = "SELECT id, name, address FROM custom";
		PreparedStatement ps = conn.prepareStatement(query);
		
		// 4. 쿼리문 실행
		ResultSet rs = ps.executeQuery();

		while(rs.next()) {
			list.add(new Custom(rs.getInt("id"),
								rs.getString("name"),
								rs.getString("address")
								));
		}
		// 5. 자원반환
		closeAll(rs, ps, conn);
		
		return list;
	}
	
	
	public static void main(String[] args) throws Exception {
		SimpleJDBCTest dao = new SimpleJDBCTest();
//		dao.addCustom(new Custom(888, "Blake8", "LA8"));
		dao.getCustom(111); // 리턴받기
		
		dao.getCustom().stream().forEach(i -> System.out.println(i));
	}

}
