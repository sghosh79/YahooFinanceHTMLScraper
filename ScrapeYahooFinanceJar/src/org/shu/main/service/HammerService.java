package org.shu.main.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;

public class HammerService extends AbstractStockService {
	
	private static final int HIGH_CLOSE_PERCENT = 25;
	
	public static void main(String[] x) throws ClassNotFoundException, SQLException{
		HammerService service = new HammerService();
		List<CommonStock> s = service.getHammerStocks(service.getStockSymbolsOver10BMktCap());
		for(CommonStock s1 : s){
			System.out.print (s1.getStockSymbol() + ",");
		}		
	}
	
	@Override
	public List<CommonStock> run(List<CommonStock> stock) {
		// TODO Auto-generated method stub
		return getHammerStocks(stock);
	}

		

	public List<CommonStock> getHammerStocks(List<CommonStock> stocks){
		List<CommonStock> hammerStocks = new ArrayList<CommonStock>();		
		try 
		{
			for(CommonStock stock : stocks) {
				if(isHammer(getStockHistoryGivenDayNum(stock, 1), HIGH_CLOSE_PERCENT)){
					hammerStocks.add(stock);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return hammerStocks;
	}
	
	

	private synchronized boolean isHammer(List<StockHistory> history, int highClosePercent) {
		// TODO Auto-generated method stub

		
		boolean isHammer = false;
		double high = history.get(0).getHigh();
		double close = history.get(0).getClose();
		double low = history.get(0).getLow();
		double open = history.get(0).getOpen();
		
		double highCloseDiff = (high - close);
		highCloseDiff = highCloseDiff / (high - low);
		highCloseDiff = highCloseDiff * 100;
		
		double highOpenDiff = (high - open);
		highOpenDiff = highOpenDiff / (high - low);
		highOpenDiff = highOpenDiff * 100;
		
	
		if(highCloseDiff <= highClosePercent && highOpenDiff <= highClosePercent)
			isHammer = true;
		
		return isHammer;
	}

	@Override
	public boolean requiresPeriod() {
		// TODO Auto-generated method stub
		return false;
	}


	

}
