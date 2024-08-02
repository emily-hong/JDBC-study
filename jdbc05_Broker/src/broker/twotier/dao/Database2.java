package broker.twotier.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;

import javax.xml.crypto.Data;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import brocker.twotier.vo.CustomerRec;
import brocker.twotier.vo.SharesRec;
import brocker.twotier.vo.StockRec;
import broker.twotier.exception.DuplicateSSNException;
import broker.twotier.exception.InvalidTransactionException;
import broker.twotier.exception.RecordNotFoundException;
import config.ServerInfo;

public class Database2 implements DatabaseTemplate{
	private static Database2 db = new Database2();
	private Database2() {
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
			System.out.println("드라이버 로딩 성공");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패...");
		}
	}
	public static Database2 getInstance() {
		return db;
	}
	
	@Override
	public Connection getConnect() throws SQLException {
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
		System.out.println("디비 연결 성공...");
		return conn;
	}

	@Override
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps!=null) ps.close();
		if(conn!=null) conn.close();
	}

	@Override
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
		closeAll(ps, conn);
	}

	// 존재유무 확인하는 비지니스 로직
	private boolean isExist(String ssn, Connection conn ) throws SQLException{
		String query = "SELECT ssn FROM customer WHERE ssn=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, ssn);
		
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	////////////////////////////////// 비지니스 로직 ///////////////////////////////////////
	@Override
	public void addCustomer(CustomerRec cust) throws SQLException, DuplicateSSNException { // 이 패턴대로 구현하기
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			if(!isExist(cust.getSsn(), conn)) { // 추가하려는 ssn이 존재하지 않는다면
				String query = "INSERT INTO customer (ssn, cust_name, address) VALUES(?,?,?)";
				ps = conn.prepareStatement(query);
				
				ps.setString(1, cust.getSsn());
				ps.setString(2, cust.getName());
				ps.setString(3, cust.getAddress());
				
				System.out.println(ps.executeUpdate() + " 명 INSERT OK~!!..addCustomer..");
			}else {
				throw new DuplicateFormatFlagsException("고객님은 이미 회원이십니다.");
			}
		}finally{
			closeAll(ps, conn);
		}
	}

	@Override
	public void deleteCustomer(String ssn) throws SQLException, RecordNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			if(isExist(ssn, conn)) {	// ssn이 존재한다면
				String query = "DELETE FROM customer WHERE ssn=?";
				ps = conn.prepareStatement(query);				
				ps.setString(1, ssn);
				
				System.out.println(ps.executeUpdate() +  " DELETE OK~!!..deleteCustomer");
			}
		}finally {
			closeAll(ps, conn);
		}
		
	}

	@Override
	public void updateCustomer(CustomerRec cust) throws SQLException, RecordNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnect();
			if(isExist(cust.getSsn(), conn)) { // ssn이 존재한다면
				String query = "UPDATE customer SET cust_name=?, address=? WHERE ssn=?";
				ps = conn.prepareStatement(query);
				
				ps.setString(1, cust.getName());
				ps.setString(2, cust.getAddress());
				ps.setString(3, cust.getSsn());
				
				System.out.println(ps.executeUpdate() + " UPDATE OK~!!..updateCustomer");
			}
		}finally {
			closeAll(ps, conn);
		}
	}

	@Override
	// 중요
	public ArrayList<SharesRec> getPortfolio(String ssn) throws SQLException {
		ArrayList<SharesRec> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			if(isExist(ssn, getConnect())) {
				String query = "SELECT symbol, ssn, quantity From shares WHERE ssn=?";
				
				conn = getConnect();
				ps = conn.prepareStatement(query);
				
				ps.setString(1, ssn);
				rs = ps.executeQuery();
				
				while(rs.next()) {
					list.add(new SharesRec(
							rs.getString("symbol"),
							rs.getString("ssn"),
							rs.getInt("quantity")
							));
				}
				return list;
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return null;
	}

	@Override
	public CustomerRec getCustomer(String ssn) throws SQLException {
		// TODO 
		CustomerRec customerRec = new CustomerRec();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConnect();
		
		try {
			if(isExist(ssn, conn)) {
				String query = "SELECT ssn, cust_name, address FROM customer WHERE ssn=?";
				
				ps = conn.prepareStatement(query);
				ps.setString(1, ssn);
				rs = ps.executeQuery();
				
				ArrayList<SharesRec> sharesRec = getPortfolio(ssn);
				
				if(rs.next()) {
					customerRec = new CustomerRec(
								ssn,
								rs.getString("cust_name"),
								rs.getString("address"),
								sharesRec.size() != 0? sharesRec : null // getPortfolio 사용하는 방법으로..
							);
				}
				
				return customerRec;
			}else {
				System.out.println("해당 " + ssn + " 회원은 존재하지 않습니다.");
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return customerRec;
	}

	@Override
	public ArrayList<CustomerRec> getAllCustomers() throws SQLException {
		// TODO
		ArrayList<CustomerRec> customerList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = getConnect();
		
		try {
			String query = "SELECT ssn, cust_name, address FROM customer";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				customerList.add(new CustomerRec(
							rs.getString("ssn"),
							rs.getString("cust_name"),
							rs.getString("address"),
							getPortfolio(rs.getString("ssn"))
						));
			}
			return customerList;
		}finally {
			closeAll(rs, ps, conn);
		}
	}

	@Override
	public ArrayList<StockRec> getAllStocks() throws SQLException {
		ArrayList<StockRec> stockRecList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = getConnect();
		
		try {
			String query = "SELECT symbol, price FROM stock";
			
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				stockRecList.add(new StockRec(
							rs.getString("symbol"),
							rs.getInt("price")
						));
			}
			
			return stockRecList;
		}finally{
			closeAll(rs, ps, conn);
		}
	}

	
	@Override
	// 기존 주식명이 있으면 update, 없으면 insert
	public void buyShares(String ssn, String symbol, int quantity) throws SQLException {
		// TODO 
		CustomerRec customer = new CustomerRec();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = getConnect();
		
		// 현재 가지고 있는 주식의 보유량
		// 100 개 가지고 있을시 10개 구매
		try {
			conn = getConnect();
			if(!isExist(ssn, conn)) {
				// 기존 주식명이 있을때
				String updateQuery = "";
				// 기존 주식명이 없을때 
				String insertQuery = "";
				
				customer = getCustomer(ssn);
				System.out.println("customer : " + customer);
				
				
				
				
			}
		}finally {
			closeAll(rs, ps, conn);
		}
	}

	@Override
	// update, delete(전부매매), transactionExcep
	public void sellShares(String ssn, String symbol, int quantity)
			throws SQLException, InvalidTransactionException, RecordNotFoundException {

		// 
	}
	
	public static void main(String[] args ) throws SQLException, DuplicateSSNException, RecordNotFoundException { // 단위 테스트용
		Database2 database = Database2.getInstance();		
//		database.addCustomer(new CustomerRec("111-120", "Yufirst10", "Busan"));

		// deleteCustomer
//		database.deleteCustomer("111-120");
		
		// updateCustomer
//		database.updateCustomer(new CustomerRec("111-120", "Yufirst10", "NY"));
		
		// getPortfolio
//		database.getPortfolio("111-111").forEach(System.out::println);
		
		// TODO getCustomer
//		System.out.println(database.getCustomer("111-111"));
		
		// TODO getAllCustomers
//		database.getAllCustomers().forEach(System.out::println);
		
		// TODO getAllStocks
//		database.getAllStocks().forEach(System.out::println);
		
		// TODO buyShares
		database.buyShares("111-111","JDK", 0);
		
		
		
		// TODO sellShares
		

	}


}

