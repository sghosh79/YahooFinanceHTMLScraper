package org.shu.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.RatioCalculator;
import org.shu.util.calculate.SharpeRatioCalculator;

public class StockCalculatorTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		StockDAO stockDao = new StockDAO();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		RatioCalculator calc = new SharpeRatioCalculator();
		CommonStock stock = new CommonStock();
		Map<CommonStock, Double> highRatios = new HashMap<CommonStock,Double>();
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		
		long startTime = System.currentTimeMillis();
		List<CommonStock> stocks = stockDao.getStockSymbols();
		
		for(CommonStock s : stocks){
		
			stock.setStockSymbol(s.getStockSymbol());	
			
			List<Double> result = dao.getStockHistoryFor2011(stock, conn);
			Double sharpRatio = calc.calculate(result, 252);
			System.out.println(stock.getStockSymbol() + ": " + sharpRatio);
			if(sharpRatio > 1.5){
				highRatios.put(s,sharpRatio);
			}
		}
		conn.close();
		
	
		
		System.out.println("Highest Ratios Printing");
		for (Map.Entry entry : highRatios.entrySet()) {
			CommonStock ss = (CommonStock)entry.getKey();
			Double val = (Double) entry.getValue();
			System.out.println(ss.getStockSymbol() + ", " + val);
		}
		
		long endTime = System.currentTimeMillis();
		double diffTime = (endTime-startTime)/1000.0 ;
		System.out.println("Total run time:" + diffTime + "seconds");

		
	}
	
	

}
