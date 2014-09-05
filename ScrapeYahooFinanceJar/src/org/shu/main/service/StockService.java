package org.shu.main.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockDAO;
import org.shu.main.scrape.ScrapeYahooCsv;
import org.shu.main.scrape.ScrapeYahooForDailyPrice;
import org.shu.util.BatchUtil;

public class StockService implements StockServiceInt, Runnable{
	
	List<CommonStock> stock;
	
	public StockService(){}
	
	public StockService(List<CommonStock> stock){
		this.stock = stock;
	}
	
	
	public void scrapeAndInsertMultipleStockHistories(Date dateStart, Date dateEnd, List<CommonStock> list) throws ClassNotFoundException, SQLException{
		StockDAO dao = new StockDAO();
		ScrapeYahooCsv scraper = new ScrapeYahooCsv();
		List<StockHistory> history = null;
		CommonStock stock = null;
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		
		if(list == null){
			list = new ArrayList<CommonStock>();
			list.add(new CommonStock(6150,"UNH"));
		}
		
		System.out.println("Start time - TOTAL: " + System.currentTimeMillis());
		try{	
			for(int i=0; i < list.size(); i++){
				stock = list.get(i);
				history = scraper.getStockHistoryByDate(stock, dateStart, dateEnd);
				dao.insertStockHistory(stock, history, conn);
				
				System.out.println(stock.getStockSymbol() + " " + i);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.close();
		}
		
		
		System.out.println("END time - total: " + System.currentTimeMillis());
		System.out.println("done");
			
	}
	
	private List<List<CommonStock>> breakListAndPerformScrapeInsert(List<CommonStock> list, int numOfLists){
		
		List<List<CommonStock>> lists = new ArrayList<List<CommonStock>>();
		List<CommonStock> stocks = null;
		
		
		//Create a few lists and add them to a single list
		for(int i=0; i < numOfLists; i++){
			lists.add(new ArrayList<CommonStock>());			
		}
		
		
		//Round robin assignment to the new lists that were created
		int listIndex = 0;
		for(int i=0; i < list.size(); i++){
			stocks = lists.get(listIndex);
			stocks.add(list.get(i));
			listIndex++;
			if(listIndex == lists.size()){
				listIndex = 0;
			}			
		}
		
		return lists;		
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.shu.main.service.StockServiceInt#scrapeAndInsertStockHistoryByDate(java.util.Date, java.util.Date, java.util.List)
	 */
	public void scrapeAndInsertStockHistoryForTodayMT(List<CommonStock> list) throws ClassNotFoundException, SQLException{
		int numOfThreads = 4;
		List<List<CommonStock>> lists = breakListAndPerformScrapeInsert(list, numOfThreads);
		final List<CommonStock> listA = lists.get(0);
		final List<CommonStock> listB = lists.get(1);
		final List<CommonStock> listC = lists.get(2);
		final List<CommonStock> listD = lists.get(3);
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				scrapeAndInsertStockHistoryForToday(listA);	
				threadComplete(1);
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
					
			@Override
			public void run() {
				scrapeAndInsertStockHistoryForToday(listB);	
				threadComplete(2);
			}
		});
		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				scrapeAndInsertStockHistoryForToday(listC);	
				threadComplete(3);
			}
		});
		Thread t4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				scrapeAndInsertStockHistoryForToday(listD);	
				threadComplete(4);
			}
		});	
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		
		
	}	
	
	private static void threadComplete(int a){
		System.out.println("Thread Complete " + a);
	}
	
		
	/* (non-Javadoc)
	 * @see org.shu.main.service.StockServiceInt#scrapeAndInsertStockHistoryForToday(java.util.List)
	 */
	@Override
	public void scrapeAndInsertStockHistoryForToday(List<CommonStock> list){
		StockDAO dao = new StockDAO();
		ScrapeYahooForDailyPrice scraper = new ScrapeYahooForDailyPrice();
		StockHistory history = null;
		CommonStock stock = null;
		List<StockHistory> historyList = null;
		BatchUtil util = new BatchUtil();
		Connection conn = null;
		
		if(list == null){
			list = new ArrayList<CommonStock>();
			list.add(new CommonStock(6150,"UNH"));
		}
		
		System.out.println("Start time - TOTAL: " + System.currentTimeMillis());
		try{	
			conn = util.getConnection();

			for(int i=0; i < list.size(); i++){
				historyList = new ArrayList<StockHistory>();
				stock = list.get(i);
				history = scraper.getTodaysPricesFromYahoo(stock);
				history.setDate(getTodaysDate());
				historyList.add(history);
				dao.insertStockHistory(stock, historyList, conn);
				//System.out.println(stock.getStockSymbol() + " " + i);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("This was hit");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println("END time - total: " + System.currentTimeMillis());
		System.out.println("done");
	}
	
	private java.sql.Date getTodaysDate(){
		Date date = new Date();
		 java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		 return sqlDate;
	}

	@Override
	public void scrapeAndInsertMultipleStockHistoriesByDateMT(Date dateStart,
			Date dateEnd, List<CommonStock> list)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		scrapeAndInsertStockHistoryForToday(stock);
		System.out.println("complete");
	}

	

}
