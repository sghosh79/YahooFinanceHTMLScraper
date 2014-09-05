package org.shu.main.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.Indicator;
import org.shu.main.bean.StockHistory;
import org.shu.main.comp.StockHistoryClosePriceComparator;
import org.shu.util.DateUtil;

public class NewHighFilter extends AbstractStockService implements Indicator {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		NewHighFilter s = new NewHighFilter();

		System.out.println(s.getHighDateForStock(new CommonStock("GS")));
		
	}
	
	//This is to get the date high for a single stock
	public Date getHighDateForStock(CommonStock stock) throws ClassNotFoundException, SQLException{
		List<CommonStock> stocks = new ArrayList<CommonStock>();
		stocks.add(stock);
		populateStockHistoryForDateRange(stocks, DateUtil.getOneYearAgoForStartDate(), DateUtil.getDateEnd());
		return getHistoryMax(stocks.get(0).getHistoryList()).getDate();		
	}
	 

	@Override
	public List<CommonStock> run(List<CommonStock> stocks) {
		// TODO Auto-generated method stub
		List<CommonStock> returnList = null;
		try {
			returnList = getNewHighStocks(stocks);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}

	private List<CommonStock> getNewHighStocks(List<CommonStock> stocks) throws ClassNotFoundException, SQLException {
		List<CommonStock> returnStocks = new LinkedList<CommonStock>();
		List<StockHistory> historyList;
		StockHistory history;
				
		populateStockHistoryForDateRange(stocks, DateUtil.getOneYearAgoForStartDate(), DateUtil.getToday());
		
		for(CommonStock s : stocks){

			historyList = s.getHistoryList();
			
			history = getHistoryMax(historyList);
			if(history.getDate().after(DateUtil.getToday()))
				returnStocks.add(s);
			
		}
		
		return returnStocks;	
	}
	
	private StockHistory getHistoryMax(List<StockHistory> historyList){
		StockHistoryClosePriceComparator comparator = StockHistoryClosePriceComparator.getInstance();
		return Collections.max(historyList, comparator);
	}
	
	private void sortHistory(List<StockHistory> historyList){
		Collections.sort(historyList);
	}

	@Override
	public boolean requiresPeriod() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
	

}
