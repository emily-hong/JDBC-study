package com.edu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	// getConnect()
	public Connection getConnect() throws SQLException {
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
	
	// insertEmployee(emp)
	public void insertEmployee(Employee e) throws SQLException {
		Connection conn = null;
		conn = getConnect();
		
		String query = "INSERT INTO employee (num, name, salary, address) VALUES(?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setInt(1, e.getNum());
		ps.setString(2, e.getName());
		ps.setDouble(3, e.getSalary());
		ps.setString(4, e.getAddress());
		
		System.out.println(ps.executeUpdate() + " row insert ok");
	
		closeAll(ps, conn);
	}
	// removeEmployee (int num)
	public void removeEmployee(int num) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		
		String query = "DELETE FROM employee WHERE num=?";
		ps = conn.prepareStatement(query);
		
		ps.setInt(1, num);
		
		System.out.println(ps.executeUpdate() + " row DELETE ok");
		
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
		
		System.out.println(ps.executeUpdate() + " row UPDATE ok");
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