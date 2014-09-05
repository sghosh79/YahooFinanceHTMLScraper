package org.shu.main.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;

public class HighVolumeFilterService implements IFilterService{
	
	
	
	StockCalculatorDAO calcDao = new StockCalculatorDAO();
	StockDAO dao = new StockDAO();
	BatchUtil util = new BatchUtil();
	
	public static void main(String[] args) throws IOException{
		List stock = new ArrayList<Object>();
		IFilterService service = new HighVolumeFilterService();
		List<CommonStock> stocks = service.run(stock);
		
		for(CommonStock s : stocks){
			BatchUtil.openYahooFinancePageForStock(s);
		}
		
	}
	//REMEMBER THAT YOU NEED TO ADD THE STOCK - RIGHT NOW IT IS BEING PASSED AND NOTHING IS HAPPENING.
	public List<CommonStock> run(List<CommonStock> stock){
		return getHighVolumeForAllStocks();
	}
	
	
	
	
	public List<CommonStock> getHighVolumeForAllStocks(){
		List<CommonStock> highVolumeStocks = new ArrayList<CommonStock>();
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			List<CommonStock> stocks = dao.getStockSymbolsOver10BMktCapAnd1MVolume();
			for(CommonStock s : stocks){
				if(getHighVolumeForTodayComparedToYesterday(s, conn)){
					highVolumeStocks.add(s);
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return highVolumeStocks;
		
	}
	
	private boolean getHighVolumeForTodayComparedToYesterday(CommonStock stock, Connection conn){
		double yesterdaysVolume;
		int todaysVolume;
		
		try {
			List<StockHistory> history = calcDao.getStockHistoryForLastFewDaysReverseOrder(stock, conn, 2);
			yesterdaysVolume = history.get(0).getVolume() * 3;
			todaysVolume = history.get(1).getVolume();
			
			if(todaysVolume > yesterdaysVolume)
				return true;
			else
				return false;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;	
	}
	
	private boolean isHighRelativeVolume(CommonStock stock, Connection conn, int relativeAmount){
//		
//		try{
//			List<StockHistory> history = calcDao.getStockHistoryForLastFewDays(stock, conn, 66);
//			//int totalVolume
//			
//			
//		}
		
		return true;
		
	}
	
	private int addVolume(List<StockHistory> history){
		
		return 1;
	}
	@Override
	public boolean requiresPeriod() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
