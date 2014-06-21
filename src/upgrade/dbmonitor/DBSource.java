package upgrade.dbmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import upgrade.pojo.City;

public class DBSource{
	public static Connection getConnection(City city){
		Connection conn=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");// 加载驱动
			conn=DriverManager.getConnection(city.getDburl(), city.getDbusername(), city.getDbpassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return conn;
	}
}