package com.edu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import com.edu.exception.DuplicateNumException;
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
	// 있는지 없는지 확인하기
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
	public void insertEmployee(Employee e) throws SQLException, DuplicateNumException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = getConnect();
						
			// isExist() 함수를 호출해서 사람이 없을때만 아래코드가 실행되도록 로직은 제어한다.
			if (!inExist(e.getNum(), conn)) {	// 추가하려는 사람이 없다면
				String query = "INSERT INTO employee (num, name, salary, address) VALUES(?,?,?,?)";
				ps = conn.prepareStatement(query);
				
				ps.setInt(1, e.getNum());
				ps.setString(2, e.getName());
				ps.setDouble(3, e.getSalary());
				ps.setString(4, e.getAddress());
				
				ps.executeUpdate();
			}else {
//				throw new RuntimeException(e.getName()+ " 님은 이미 회원입니다.");
				throw new DuplicateNumException(e.getName()+ " 님은 이미 회원입니다.");
			}
		}finally{
			// try에서 실행유무 상관없이 무조건 자원 반환은 된다.
			closeAll(ps, conn);
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