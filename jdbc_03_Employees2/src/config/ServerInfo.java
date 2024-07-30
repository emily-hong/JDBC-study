package config;

public interface ServerInfo {
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	String URL = "jdbc:mysql://127.0.0.1:3306/kosta?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
	String USER = "root";
	String PASSWORD = "1234";
	// 인터페이스 방식보다는 properties 파일에 저장하는 방식으로 하기
}
