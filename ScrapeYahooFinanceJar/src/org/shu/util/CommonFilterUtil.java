package org.shu.util;

import java.sql.SQLException;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockDAO;

public class CommonFilterUtil {
	
	final private static int MIN_HISTORY = 0;
	final private static int MAX_HISTORY = 2;
	final private static int CURRENT_HISTORY = 1;
	

	private static boolean isWithinFortyPercentOf52WkLow(CommonStock stock)
		throws ClassNotFoundException, SQLException {
		List<StockHistory> history;
		StockHistory maxHistory = null;
		StockHistory minHistory = null;
		StockHistory currentHistory = null;

		StockDAO dao = new StockDAO();
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
	
	private static boolean isWithinFortyPercentOfFiftyTwoWeekLow(double max, double min, double current){		
	double check = ((current - min) / (max - min));
	
	if(check <= 0.4)
		return true;
	else
		return false;
	}
	
	
}
