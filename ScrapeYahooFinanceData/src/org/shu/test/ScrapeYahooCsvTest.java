package org.shu.test;

import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.scrape.ScrapeYahooCsv;

public class ScrapeYahooCsvTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonStock stock = new CommonStock();
		ScrapeYahooCsv scraper = new ScrapeYahooCsv();
		stock.setStockSymbol("UNH");
		
		//List<StockHistory> history = scraper.getTodaysStockHistory(stock);
		Date dateStart = new Date(2012,9,19);	//year, month, date		(January - 0, Feb - 1, etc)
		Date dateEnd = new Date(2013,2,10);
		List<StockHistory> history = scraper.getStockHistoryByDate(stock, dateStart, dateEnd);
		for(StockHistory h : history){
			System.out.println(h.getClose());
			System.out.println(h.getDate());
			System.out.println(h.getVolume());
		}
		
	}

}
