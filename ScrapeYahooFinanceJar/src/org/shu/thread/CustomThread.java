package org.shu.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.service.HammerService;

public class CustomThread extends Thread {
	
	private Object o;
	private List<StockHistory> history;
	private List<CommonStock> hammerStocks = new ArrayList<CommonStock>();
	private int _highClosePercent;
	private Map<CommonStock, List<StockHistory>> map;
	
	public CustomThread(Map<CommonStock, List<StockHistory>> map){
		this.map = map;
	}
	

	public void run(){
		
		List<StockHistory> history;
		CommonStock s;
		try{
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
	    	history = (List<StockHistory>) pairs.getValue(); 
	    	s = (CommonStock) pairs.getKey();

	    	if(isHammer(history, 25)){
	    		hammerStocks.add(s);
	    	}
	    	it.remove();
	    }
		}catch
		(Exception e){
		
		}

		
	//	HammerService.threadComplete(this);
		
	}
	
	private boolean isHammer(List<StockHistory> history, int highClosePercent) {
		// TODO Auto-generated method stub
		highClosePercent = 25;
		
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
	
	public List<CommonStock> getHammerStocks(){
		return hammerStocks;		
	}

}
