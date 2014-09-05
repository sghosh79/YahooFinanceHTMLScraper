package org.shu.main.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;

public class MultiDayDownService {

	/**
	 * @param args
	 */
	StockCalculatorDAO calcDao = new StockCalculatorDAO();
	StockDAO dao = new StockDAO();
	BatchUtil util = new BatchUtil();
	List<CommonStock> fiveDaysDown = new ArrayList<CommonStock>();
	private static final Logger logger = Logger.getLogger(MultiDayDownService.class);
	
	final private static int MIN_HISTORY = 0;
	final private static int MAX_HISTORY = 2;
	final private static int CURRENT_HISTORY = 1;
	
	public static void main(String[] x){
		MultiDayDownService service = new MultiDayDownService();
		List<CommonStock> s = service.getStocksGoingDown(5, true);
		for(CommonStock s1 : s){
			System.out.print (s1.getStockSymbol() + ",");
		}
		System.out.println();
		List<CommonStock> s2 = service.getStocksGoingDown(5, false);
		for(CommonStock s3 : s2){
			System.out.print (s3.getStockSymbol() + ",");
		}
		logger.info("test");
		System.out.println("Completed");
	}
	
	public List<CommonStock> getStocksGoingDown(int numOfDaysRequested, boolean withinBottomForty){
		List<CommonStock> stocks;
				
		try {
			stocks = dao.getStockSymbolsOver10BMktCap();
			
			for(CommonStock stock : stocks) {
				Connection conn = util.getConnection();

				List<StockHistory> history = calcDao.getStockHistoryForLastFewDays(stock, conn, numOfDaysRequested);
				if (isGoingDown(history))
					if(withinBottomForty){
						if(isWithinFortyPercentOf52WkLow(stock))							
							fiveDaysDown.add(stock);
					}
					else 
						fiveDaysDown.add(stock);
				
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fiveDaysDown;
	}

	private boolean isGoingDown(List<StockHistory> history) {
		boolean trigger = true;
		double lastClose = 0.0;
		for (StockHistory h : history) {
			if (lastClose < h.getClose() || lastClose == 0.0) {
				lastClose = h.getClose();
			} else {
				trigger = false;
				break;
			}
		}
		return trigger;
	}

	private boolean isWithinFortyPercentOf52WkLow(CommonStock stock)
			throws ClassNotFoundException, SQLException {
		List<StockHistory> history;
		StockHistory maxHistory = null;
		StockHistory minHistory = null;
		StockHistory currentHistory = null;

		history = dao.getHighLowTodayForStock(stock);

		if(history.size() == 3){
			for (int i = 0; i < history.size(); i++) {
				switch (i) {
				case MIN_HISTORY:
					minHistory = history.get(i);
					break;
				case MAX_HISTORY:
					maxHistory = history.get(i);
					break;
				case CURRENT_HISTORY:
					currentHistory = history.get(i);
					break;
				}
			}
		}else
			return false;

		return isWithinFortyPercentOfFiftyTwoWeekLow(maxHistory.getHigh(), minHistory.getLow(), currentHistory.getClose());
	}
	
	private boolean isWithinFortyPercentOfFiftyTwoWeekLow(double max, double min, double current){		
		double check = ((current - min) / (max - min));
		
		if(check <= 0.4)
			return true;
		else
			return false;
	}

}
