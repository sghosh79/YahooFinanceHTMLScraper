package org.shu.main.service.calc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.MovingAverageCalculator;

public class SMACalculatorService {
	static StockCalculatorDAO dao = new StockCalculatorDAO();
	static StockDAO stockDao = new StockDAO();
	
	//Turn this main into a method
	public static void main(String[] args) throws Exception {
		

	}
	
	public void getMAForGivenDateAndStock(CommonStock stock, int numOfDays, Integer lowMA, Integer highMA) throws Exception{
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		List<CommonStock> stockList = stockDao.getStockSymbols();
		List<StockHistory> history = dao.getStockHistoryForLastFewDays(stock, conn, numOfDays);
		long startTime = System.nanoTime();
		long endTime;
		for(CommonStock symbol : stockList){
			System.out.println("Start " + symbol.getStockSymbol());
			calculateMA(lowMA,highMA,stock,conn);
			System.out.println("End " + symbol.getStockSymbol());
		}
		endTime = System.nanoTime() - startTime;
		endTime = endTime / 1000000;
		System.out.println("Entire process took:" + endTime);
		conn.close();
	}
	
	static List<Double> orderedList(Collection<StockHistory> history){
		List<Double> ol = new ArrayList<Double>();
		
		for(StockHistory hist : history){
			ol.add(hist.getClose());
		}
		
		return ol;
		
	}
	
	static void calculateMAGivenDates(int low, int high, CommonStock stock, Connection conn, Date dateStart, Date dateEnd) throws Exception{
		long startTime;
		long endTime;
		List<StockHistory> result = dao.getStockHistoryInfoFor2011(stock, conn);
		startTime = System.nanoTime();
		//List<Double> ma = buildMovingAverage(30);
		MovingAverageCalculator calc = new MovingAverageCalculator();
		List<Double> stockClose = orderedList(result);
		
		List<Double> highMa = calc.calculateSMA(high, stockClose);
		List<Double> lowMa = calc.calculateSMA(low, stockClose);
		
		Double endPrice = 100.0;
		List<Double> maxVal = new ArrayList<Double>();
		
		boolean buySell = false;
		Iterator it = result.iterator();
		for(int i = 0; i<highMa.size(); i++){
			StockHistory hist = (StockHistory) it.next();
			Date dt = (Date)hist.getDate();
			if(highMa.get(i) > lowMa.get(i)){
				//System.out.println("9 day above 4 day" + dt.toString());
				if(buySell){
					endPrice = endPrice + hist.getClose();
					maxVal.add(endPrice);
					System.out.println("Sell " + dt.toString() + "  \n$" +  hist.getClose() + " \nVolume:" +  hist.getVolume() );
					System.out.println("endPrice: " + endPrice);
					buySell = false;
					
				}
			}else if(lowMa.get(i) > highMa.get(i)){
				//System.out.println("4 day above 9 day" + dt.toString());
				if(!buySell){
					endPrice = endPrice - hist.getClose();
					maxVal.add(endPrice);
					System.out.println("Buy " + dt.toString() + " \n$"+ hist.getClose() +  " \nVolume:" +  hist.getVolume());
					System.out.println("endPrice: " + endPrice);
					buySell = true;
				}

			}
		}
		
		System.out.println("MAX VALUE: " + getMax(maxVal));
			
		endTime = (System.nanoTime() - startTime) / 1000000;
		System.out.println(startTime + "ms");
		System.out.println(endTime + "ms");
	}

	
	static void calculateMA(int low, int high, CommonStock stock, Connection conn) throws Exception{
		long startTime;
		long endTime;
		List<StockHistory> result = dao.getStockHistoryInfoFor2011(stock, conn);
		startTime = System.nanoTime();
		//List<Double> ma = buildMovingAverage(30);
		MovingAverageCalculator calc = new MovingAverageCalculator();
		List<Double> stockClose = orderedList(result);
		
		List<Double> highMa = calc.calculateSMA(high, stockClose);
		List<Double> lowMa = calc.calculateSMA(low, stockClose);
		
		Double endPrice = 100.0;
		List<Double> maxVal = new ArrayList<Double>();
		
		boolean buySell = false;
		Iterator it = result.iterator();
		for(int i = 0; i<highMa.size(); i++){
			StockHistory hist = (StockHistory) it.next();
			Date dt = (Date)hist.getDate();
			if(highMa.get(i) > lowMa.get(i)){
				//System.out.println("9 day above 4 day" + dt.toString());
				if(buySell){
					endPrice = endPrice + hist.getClose();
					maxVal.add(endPrice);
					System.out.println("Sell " + dt.toString() + "  \n$" +  hist.getClose() + " \nVolume:" +  hist.getVolume() );
					System.out.println("endPrice: " + endPrice);
					buySell = false;
					
				}
			}else if(lowMa.get(i) > highMa.get(i)){
				//System.out.println("4 day above 9 day" + dt.toString());
				if(!buySell){
					endPrice = endPrice - hist.getClose();
					maxVal.add(endPrice);
					System.out.println("Buy " + dt.toString() + " \n$"+ hist.getClose() +  " \nVolume:" +  hist.getVolume());
					System.out.println("endPrice: " + endPrice);
					buySell = true;
				}

			}
		}
		
		System.out.println("MAX VALUE: " + getMax(maxVal));
			
		endTime = (System.nanoTime() - startTime) / 1000000;
		System.out.println(startTime + "ms");
		System.out.println(endTime + "ms");
	}
	
	static Double getMax(Collection<Double> value){
		Double values = 0.0;
		
		Iterator it = value.iterator();
		while(it.hasNext()){
			Double check = (Double)it.next();
			if(values < check)
				values = check;
			
		}
		
		return values;
	}

}
