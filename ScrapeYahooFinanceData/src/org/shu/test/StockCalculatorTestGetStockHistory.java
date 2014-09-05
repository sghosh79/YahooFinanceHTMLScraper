package org.shu.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.RatioCalculator;
import org.shu.util.calculate.SharpeRatioCalculator;

public class StockCalculatorTestGetStockHistory {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		StockDAO stockDao = new StockDAO();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		RatioCalculator calc = new SharpeRatioCalculator();
		CommonStock stock = new CommonStock();
		Map<CommonStock, Double> highRatios = new HashMap<CommonStock,Double>();
		BatchUtil util = new BatchUtil();
		Connection conn = null;
		try {
			conn = util.getConnection();
			List<StockHistory> closingPrices = dao.getStockHistoryForLastFewDays(new CommonStock("UNH"), conn, 15);
			for(StockHistory i : closingPrices){
				System.out.println(i.getClose() + " " + i.getDate());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.close();
		}

	}

}
