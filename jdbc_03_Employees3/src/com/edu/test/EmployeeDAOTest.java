package com.edu.test;

import java.sql.SQLException;

import com.edu.dao.EmployeeDAO;
import com.edu.exception.DMLException;
import com.edu.exception.DuplicateNumException;
import com.edu.exception.RecordNotFoundException;
import com.edu.vo.Employee;

public class EmployeeDAOTest {
	public static void main(String[] args) throws SQLException, RecordNotFoundException, DMLException {
		EmployeeDAO dao = EmployeeDAO.getinstance();
		
//		try {
//			// 중복회원
//			dao.insertEmployee(new Employee(111, "JAMES", 1500.0, "NY"));
//			// 없는 회원, 쿼리문 잘못 작성 시
//			dao.insertEmployee(new Employee(485, "JAMES", 1500.0, "NY"));
//		}catch(DuplicateNumException e) {
//			System.out.println(e.getMessage()); // dao에서 던진에러를 메세지로 받음
//		}catch(DMLException e) {
//			System.out.println(e.getMessage()); // dao에서 던진에러를 메세지로 받음
//		}
		
		// 없는 회원 삭제
//		try {
//			dao.removeEmployee(007);
//		}catch(RecordNotFoundException e) {
//			System.out.println(e.getMessage());
//		}catch(DMLException e) {
//			System.out.println(e.getMessage());
//		}
		
		// 회원 수정
		try {
			dao.updateEmployee(new Employee(885, "JAMES", 1200.0, "NY"));
		}catch (RecordNotFoundException e) {
			System.out.println(e.getMessage());
		}catch (DMLException e) {
			System.out.println(e.getMessage());			
		}
		
		
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
