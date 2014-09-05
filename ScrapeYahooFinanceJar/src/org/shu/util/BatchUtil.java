package org.shu.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.shu.main.bean.CommonStock;

public class BatchUtil {
	
	
	//Local Oracle
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521/xexdb","system","admin");
			
			//System.out.println("Connection opened");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return connection;
	}	

	//Cloud postgresql
	public Connection getPgConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
				"jdbc:postgresql://babar.elephantsql.com:5432/tswponbg","tswponbg","gNevgAhn8MS9n1kok5M7VwjQcNsgHZNQ");
			
			//System.out.println("Connection opened");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return connection;
	}
	
	public static void openYahooFinancePageForStock(CommonStock stock) throws IOException{
		java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://finance.yahoo.com/echarts?s=" + stock.getStockSymbol()));
	}
	

}
