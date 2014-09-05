package org.shu.main.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;

public class FiveDaysDownService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockCalculatorDAO calcDao = new StockCalculatorDAO();
		StockDAO dao = new StockDAO();
		BatchUtil util = new BatchUtil();
		List<CommonStock> fiveDaysDown = new ArrayList<CommonStock>();
		FiveDaysDownService service = new FiveDaysDownService();
				
		try {
			List<CommonStock> stocks = dao.getStockSymbolsOver10BMktCap();
			
			for(CommonStock stock : stocks){
				Connection conn = util.getConnection();
				List<StockHistory> history = calcDao.getStockHistoryForLastFewDays(stock, conn, 5);
				if(service.fiveDaysDown(history))
					fiveDaysDown.add(stock);
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(CommonStock s : fiveDaysDown)
			System.out.println(s.getStockSymbol());	

	}
	
	private boolean fiveDaysDown(List<StockHistory> history){
		boolean trigger = true;
		double lastClose = 0.0;
		for(StockHistory h : history){
			if(lastClose < h.getClose() || lastClose == 0.0){
				lastClose = h.getClose();
			}else{
				trigger = false;
				break;
			}			
		}		

		return trigger;
		
	}

}
