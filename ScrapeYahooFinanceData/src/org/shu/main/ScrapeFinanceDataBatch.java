package org.shu.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockDAO;
import org.shu.main.scrape.ScrapeFinviz;
import org.shu.main.service.StockService;
import org.shu.main.service.StockServiceInt;

public class ScrapeFinanceDataBatch {

	/**
	 * @param args
	 *  2 parameters:	DateStart and DateEnd
	 *  DateStart/DateEnd = 20130513 (Year, Month, Date) - always 8 digits
	 *  
	 *  For a single day, must provide startDate and endDate (next day) - only the startDate will be captured
	 * @throws InterruptedException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
		if(args.length == 1 && !args[0].equalsIgnoreCase("MT2")){
			long start = System.currentTimeMillis();
			updateAllStocksForTodayMT();
			long end = System.currentTimeMillis() - start;
			System.out.println(end + " end time ");
		}else if(args[0].equalsIgnoreCase("MT2")){
			long start = System.currentTimeMillis();
			updateAllStocksForTodayMT2();
			long end = System.currentTimeMillis() - start;
			System.out.println(end + " end time ");
			
		}
		else if(args.length > 0){
			
				int startYear, startMonth, startDate, endYear, endMonth, endDate;
				startYear = Integer.parseInt(args[0].substring(0, 4));
				startMonth = Integer.parseInt(args[0].substring(4, 6));
				startDate = Integer.parseInt(args[0].substring(6, 8));
				endYear = Integer.parseInt(args[1].substring(0, 4));
				endMonth = Integer.parseInt(args[1].substring(4, 6));
				endDate = Integer.parseInt(args[1].substring(6, 8));
				
				Date dateStart = new Date(startYear, --startMonth, startDate);
				Date dateEnd = new Date(endYear, --endMonth, endDate);				
				
				long start = System.currentTimeMillis();

				updateAllStocks(dateStart, dateEnd);
				
				long end = System.currentTimeMillis() - start;
				System.out.println(end + " end time ");
				
		}
		else{
			updateAllStocksForToday();
		}
	}
	
	public static List<CommonStock> getListOfStocks() throws ClassNotFoundException, SQLException{
		StockDAO dao = new StockDAO();
		List<CommonStock> list = dao.getStockSymbols();
		return list;
	}
	
	//Put this method on a daily timer
	public static void updateAllStocksForToday(){
		StockServiceInt service = new StockService();
		try {
			long start = System.currentTimeMillis();
			long end;
			System.out.println("Start");			
			service.scrapeAndInsertStockHistoryForToday(getListOfStocks());
			end = System.currentTimeMillis() - start;
			System.out.println("End:" + end);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateAllStocksForTodayMT(){
		StockService service = new StockService();
		try {
			long start = System.currentTimeMillis();
			long end;
			System.out.println("Start");			
			service.scrapeAndInsertStockHistoryForTodayMT(getListOfStocks());
			end = System.currentTimeMillis() - start;
			System.out.println("End:" + end);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateAllStocksForTodayMT2() throws ClassNotFoundException, SQLException, InterruptedException{
		List<List<CommonStock>> stocks = breakListAndPerformScrapeInsert(getListOfStocks(),100);
		List<StockService> stockList = createListOfStockService(stocks);
		long start = System.currentTimeMillis();
		long end;
		System.out.println("Start");
		ExecutorService executor = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
		  Runnable worker = stockList.get(i);
		  executor.execute(worker);
		}
		// This will make the executor accept no new threads
		// and finish all existing threads in the queue
		executor.shutdown();
		// Wait until all threads are finish
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		System.out.println("Finished all threads");
		
		end = System.currentTimeMillis() - start;
		System.out.println("End:" + end);
	}
	
	private static List<StockService> createListOfStockService(List<List<CommonStock>> stocks){
		List<StockService> serviceList = new ArrayList<StockService>();
		for(int i=0; i < stocks.size(); i++){
			serviceList.add(new StockService(stocks.get(i)));
		}
		return serviceList;
	}
	
	private static List<List<CommonStock>> breakListAndPerformScrapeInsert(List<CommonStock> list, int numOfLists){
			
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
		
	
	
	//Use this method if the daily stock history update failed or has not been running
	public static void updateAllStocks(Date dateStart, Date dateEnd){
		try {
			long start = System.currentTimeMillis();
			long end;
	
			System.out.println("Start");			
			getAndSetHistoryUptoToday(getListOfStocks(), dateStart, dateEnd);
			end = System.currentTimeMillis() - start;
			System.out.println("End:" + end);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getAndSetHistoryUptoToday(List<CommonStock> list, Date dateStart, Date dateEnd){
		StockServiceInt service = new StockService();
		try {
			//service.scrapeAndInsertStockHistory();
//			Date dateStart = new Date(2012,9,22);	//year, month, date
//			Date dateEnd = new Date(2013,2,6);
			//list = null;
			service.scrapeAndInsertMultipleStockHistories(dateStart, dateEnd, list);
			System.out.println("Done");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@SuppressWarnings({ "deprecation" })
	public static Date getDateStart(){
		GregorianCalendar gDate = new GregorianCalendar();
		int month = gDate.get(Calendar.MONTH);
		int day = gDate.get(Calendar.DATE);
		int year = gDate.get(Calendar.YEAR);
		Date date = new Date(year, month, day);
		return date;
	}
	
	@SuppressWarnings({ "deprecation" })
	public static Date getDateEnd(){
		GregorianCalendar gDate = new GregorianCalendar();
		int month = gDate.get(Calendar.MONTH);
		int day = gDate.get(Calendar.DATE)+1;		//Added one to the date.
		int year = gDate.get(Calendar.YEAR);
		Date date = new Date(year, month, day);
		return date;
	}
		
	
	//Unused method - This method was initially used to get the initial list of stock symbols from finviz 
	public static void insertSymbolsIntoDb(){
		// TODO Auto-generated method stub
		ScrapeFinanceDataBatch batch = new ScrapeFinanceDataBatch();
//		batch.test();
		batch.insertSymbolsIntoDB(batch.getSymbols());
		System.out.println("Done");
	}
	
	public void insertSymbolsIntoDB(List<String> symbols){
		StockDAO dao = new StockDAO();
		try {
			dao.initialInsert(symbols);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//MOVE THIS METHOD TO DAO AND MAKE IT PULL THE URL DYNAMICALLY
	public List<String> getSymbols(){
		List<String> symbols = new ArrayList<String>();
		ScrapeFinviz scraper = new ScrapeFinviz();
		
		for(int i=1; i<6681; i=i+20){
			System.out.println(i);
			String html = scraper.getHtmlFromUrl("http://finviz.com/screener.ashx?v=111&r=" + i);
			html = scraper.captureChart(html, i);	
			symbols.addAll(scraper.captureSymbolNameIndustrySector(html));
		}		
		return symbols;		
	}
	
	

}
