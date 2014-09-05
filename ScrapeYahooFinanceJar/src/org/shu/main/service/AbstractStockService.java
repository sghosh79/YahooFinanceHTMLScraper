package org.shu.main.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;

public abstract class AbstractStockService implements IFilterService{
	
	private StockCalculatorDAO calcDao = new StockCalculatorDAO();
	private StockDAO dao = new StockDAO();
	int period;
	
	public AbstractStockService(){}
	public AbstractStockService(int period){
		this.period = period;
	}
	
	protected List<StockHistory> getStockHistoryGivenDayNum(CommonStock stock, int numOfDaysRequested) throws ClassNotFoundException, SQLException{
		BatchUtil util = new BatchUtil();
		
		Connection conn = util.getConnection();		
		List<StockHistory> history = calcDao.getStockHistoryForLastFewDays(stock, conn, numOfDaysRequested);
		
		conn.close();
		
		return history;
	}
	
	protected List<CommonStock> getStockSymbolsOver10BMktCap() throws ClassNotFoundException, SQLException{
		return dao.getStockSymbolsOver10BMktCap();
	}
	
	protected void populateStockHistoryForDateRange(List<CommonStock> stocks, Date dateStart, Date dateEnd) throws ClassNotFoundException, SQLException{
		calcDao.populateMultipleStockHistoryForDateRange(stocks, dateStart, dateEnd);
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}

}
