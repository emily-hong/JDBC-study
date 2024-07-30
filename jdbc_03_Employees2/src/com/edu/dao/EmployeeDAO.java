package com.edu.dao;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import com.edu.vo.Employee;

import config.ServerInfo;

public class EmployeeDAO {
	private static EmployeeDAO dao = new EmployeeDAO();
	
	private EmployeeDAO() {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
		}catch (ClassNotFoundException e) {
			System.out.println("드라이브 로딩 실패");
		}
	}
	public static EmployeeDAO getinstance() {
		return dao;
	}
	
	////////////////// 공통적인 로직 ////////////////////
	// getConnect()
	public Connection getConnect() throws SQLException {
		System.out.println("db연결 성공....");
		return DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
	}
	
	// closeAll(ps,conn)
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps != null) ps.close();
		if(conn != null) conn.close();
	}
	// closeAll(rs, ps,conn)
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs != null) rs.close();
		closeAll(ps, conn);
	}
	
	////////////////// 비즈니스 로직 ///////////////////
	// isExist() 사용안하는 경우... (쿼리문 2번 동작할 이유가 없다(select 안돌아간다))
	private boolean inExist(int num, Connection conn) throws SQLException{	//PK에 해당하는 사람이 있는지 여부 리턴
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT num FROM employee WHERE num=?";
		
		ps = conn.prepareStatement(query);
		ps.setInt(1, num);
		rs = ps.executeQuery(); 
	
		return rs.next();	// 값이 있는지 없는지만 확인
	}
	// insertEmployee(emp)
	public void insertEmployee(Employee emp){
		// try with resource 구문으로 변경
		String query = "INSERT INTO employee (num, name, salary, address) VALUES(?,?,?,?)";
		
		try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, emp.getNum());
			ps.setString(2, emp.getName());
			ps.setDouble(3, emp.getSalary());
			ps.setString(4, emp.getAddress());
			
			ps.executeUpdate();
			
		}catch(SQLIntegrityConstraintViolationException e) { // 중복오류 예외(pk중복찾아줌)
			System.out.println(e.getMessage());
			
		}catch(SQLException e) { // 중복오류
			System.out.println(e.getMessage());
		}
	}
	
	// removeEmployee (int num)
	public void removeEmployee(int num) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnect();
		
		String query = "DELETE FROM employee WHERE num=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, num);

		ps.executeUpdate();
		
		closeAll(ps, conn);
	}
	
	// updateEmployee (emp)
	public void updateEmployee(Employee emp) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnect();
		
		String query = "UPDATE employee SET name=?, salary=?, address=? WHERE num=?";
		ps = conn.prepareStatement(query);
		
		ps.setString(1, emp.getName());
		ps.setDouble(2, emp.getSalary());
		ps.setString(3, emp.getAddress());
		ps.setInt(4, emp.getNum());
		
		ps.executeUpdate();
	}
	
	// selectEmployee () | selectEmployee (int num)
	public ArrayList<Employee> selectEmployee() throws SQLException{
		ArrayList<Employee> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = getConnect();
		
		String query = "SELECT num, name, salary, address FROM employee";
		ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			list.add(new Employee(
						rs.getInt("num"),
						rs.getString("name"),
						rs.getDouble("salary"),
						rs.getString("address")
					));
		}
		
		closeAll(rs, ps, conn);
		
		return list;
	}
	public Employee selectEmployee(int num) throws SQLException {
		Employee employee = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		
		conn = getConnect();
		String query = "SELECT num, name, salary, address FROM employee WHERE num=?";
		ps = conn.prepareStatement(query);
		
		ps.setInt(1, num);
		
		rs = ps.executeQuery();
		
		if(rs.next()) {
			employee = new Employee(
						num,
						rs.getString("name"),
						rs.getDouble("salary"),
						rs.getString("address")
					);
		}
		
		closeAll(rs, ps, conn); 
		
		return employee;
	}
}