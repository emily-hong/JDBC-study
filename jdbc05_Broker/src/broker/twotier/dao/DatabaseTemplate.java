package broker.twotier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import brocker.twotier.vo.CustomerRec;
import brocker.twotier.vo.SharesRec;
import brocker.twotier.vo.StockRec;
import broker.twotier.exception.DuplicateSSNException;
import broker.twotier.exception.InvalidTransactionException;
import broker.twotier.exception.RecordNotFoundException;

public interface DatabaseTemplate {
	Connection getConnect() throws SQLException;
	void closeAll(PreparedStatement ps, Connection conn)throws SQLException;
	void closeAll(ResultSet rs, PreparedStatement ps, Connection conn)throws SQLException;
	
	//비지니스 로직...CRUD
	void addCustomer(CustomerRec cust)throws SQLException,DuplicateSSNException;
	void deleteCustomer(String ssn)throws SQLException,RecordNotFoundException;
	void updateCustomer(CustomerRec cust)throws SQLException,RecordNotFoundException;
	
	ArrayList<SharesRec> getPortfolio(String ssn) throws SQLException; // 조인필요없이 사용할 수 있게됨 -> getCustomer, getAllCustomers에서 사용
	CustomerRec getCustomer(String ssn) throws SQLException;
	ArrayList<CustomerRec> getAllCustomers() throws SQLException;
	ArrayList<StockRec> getAllStocks() throws SQLException;
	            
	//비지니스 로직...특화된 로직
	void buyShares(String ssn, String symbol, int quantity)throws SQLException; // 기존 주식명이 있으면 update, 없으면 insert
	void sellShares(String ssn, String symbol, int quantity)throws SQLException,InvalidTransactionException,RecordNotFoundException; // update, delete(전부매매), transactionExcep..

}













