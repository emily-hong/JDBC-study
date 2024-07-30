package com.edu.test;

import java.sql.SQLException;

import com.edu.dao.EmployeeDAO;
import com.edu.exception.DuplicateNumException;
import com.edu.vo.Employee;

public class EmployeeDAOTest {
	public static void main(String[] args) throws SQLException {
		EmployeeDAO dao = EmployeeDAO.getinstance();
		
		// 없는 회원 삭제
		dao.removeEmployee(007);
		
		// 예외처리
		// client에서 호출, 예외를 잡음

		/* SQLIntegrityConstraintViolationException 로 처리하기
		 * java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '111' for key 'employee.PRIMARY' 
		 */
		
//		dao.insertEmployee(new Employee(346, "PAPAPA7", 2500.0, "PARIS7"));
				
		
		/*
		dao.insertEmployee(new Employee(111, "JAMES", 1500.0, "NY"));
		dao.insertEmployee(new Employee(222, "Tomas", 800.0, "Brandon"));
		dao.insertEmployee(new Employee(333, "PAPAPA", 2500.0, "PARIS"));
		dao.insertEmployee(new Employee(444, "KIM", 1900.0, "KOREA"));

		System.out.println(dao.selectEmployee(111));
		System.out.println("==========================================");
		dao.selectEmployee().forEach(System.out::println);
		
		System.out.println("\n==========================================\n");
		
		dao.updateEmployee(new Employee(111, "JAMES", 1200.0, "NY"));
		
		System.out.println(dao.selectEmployee(111));
		System.out.println("==========================================");
		dao.selectEmployee().forEach(System.out::println);
		
		System.out.println("\n==========================================\n");
		
		dao.removeEmployee(222);
		dao.selectEmployee().forEach(System.out::println);
		*/
	}
	
}
