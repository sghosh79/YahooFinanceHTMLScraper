package org.shu.main.service.calc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.CommonStockSignalPrice;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.MovingAverageCalculator;

public class DailySMACalcService implements IDailySMACalcService {

	public static void main(String[] args){
		int buyCount = 0;
		int sellCount = 0;
		long startTime = 0;
		long endTime = 0;
		String signal = "";
		DailySMACalcService service = new DailySMACalcService();
		//List<CommonStockSignalPrice> list = service.getStockSignals();
		List<CommonStock> stocks = service.getSymbols();
		startTime = System.currentTimeMillis();
		for(CommonStock stock : stocks){
			try{
				signal = service.getSMASignal(stock, 9, 4);
				if(signal.equalsIgnoreCase("buyTrigger")){
					buyCount++;

				}else if(signal.equalsIgnoreCase("sellTrigger"))
				{
					sellCount++;
				}
			
			}catch(IndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		System.out.println("Buycount: " + buyCount);
		System.out.println("Sell count: " + sellCount);
		System.out.println("Total size: " + stocks.size());
		endTime = System.currentTimeMillis() - startTime;
		endTime = endTime / 1000;
		System.out.println("Total run time: " + endTime);

	}
	
	
	@Override
	public List<CommonStockSignalPrice> getStockSignals() {
		List<CommonStockSignalPrice> list = new ArrayList<CommonStockSignalPrice>();
		
		// TODO Auto-generated method stub
		return list;
	}
	
	private List<CommonStock> getSymbols(){
		StockDAO dao = new StockDAO();
		List<CommonStock> list = null;
		try {
			list = dao.getStockSymbolsVolumeGreaterThanOneMillion();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private String getSMASignal(CommonStock stock, int high, int low){

		List<Double> highValues;
		List<Double> lowValues;		
		String buySell = "";
		highValues = getMAForLastTwoDaysBasedOnPeriod(stock, high);
		lowValues = getMAForLastTwoDaysBasedOnPeriod(stock, low);
		for(int i=0; i < highValues.size(); i++){
			if(highValues.get(i) > lowValues.get(i)){	
				
				if(buySell.equalsIgnoreCase("buy")  ){
					System.out.println("SELL SIGNAL!!!" + stock.getStockSymbol());
					buySell = "sellTrigger";
					for(int j=0; j < highValues.size(); j++){
						//System.out.println("High Value:" + highValues.get(j));
						//System.out.println("Low Value:" + lowValues.get(j));
					}
				}else{
					buySell = "sell";
				}
				
			}else{
				
				if(buySell.equalsIgnoreCase("sell") ){
					System.out.println("BUY SIGNAL!!!"  + stock.getStockSymbol());
					buySell = "buyTrigger";
					for(int j=0; j < highValues.size(); j++){
						//System.out.println("High Value:" + highValues.get(j));
						//System.out.println("Low Value:" + lowValues.get(j));						
					}
				}	
				else{
					buySell = "buy";
				}
					
				
			}
		}
	
	
		return buySell;
	}
	
	private List<Double> getMAForLastTwoDaysBasedOnPeriod(CommonStock stock, int period){
		MovingAverageCalculator calculator = new MovingAverageCalculator();
		List<Double> maForLastTwoDays = new ArrayList<Double>();
		List<Double> oL = new ArrayList<Double>();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		BatchUtil util = new BatchUtil();
		List<StockHistory> result = null;
		
		try {
			result = dao.getStockHistoryForLastFewDays(stock, util.getConnection(), period+1);
			oL = orderedList(result);
			maForLastTwoDays = calculator.calculateSMA(period, oL);
			clearEmptyValues(maForLastTwoDays);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return maForLastTwoDays;
	}	
	
	private List<Double> orderedList(List<StockHistory> history){
		List<Double> ol = new ArrayList<Double>();		
		for(int i = history.size()-1; i >= 0; i--){
			ol.add(history.get(i).getClose());
		}
		return ol;		
	}
	
	private void clearEmptyValues(List<Double> list){
		Iterator<Double> it = list.iterator();
		while(it.hasNext()){
			if(it.next()<= 0.0)
				it.remove();
		}		
	}
}
