package org.shu.stock.signals;

import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;

public class HammerSignal extends Signal {

	public HammerSignal(){
		super();
	}

	public HammerSignal(CommonStock stock) {
		super(stock);
	}

	public void findSignals(List<StockHistory> histories) {
		//Pass the histories into isHammer and add the dates
		for(StockHistory h : histories){
			if(isHammer(h,25)){
				getDate().add(h.getDate());
			}
		}		
	}
	
	private boolean isHammer(StockHistory history, int highClosePercent) {

		boolean isHammer = false;
		double high = history.getHigh();
		double close = history.getClose();
		double low = history.getLow();
		double open = history.getOpen();
		
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

}
