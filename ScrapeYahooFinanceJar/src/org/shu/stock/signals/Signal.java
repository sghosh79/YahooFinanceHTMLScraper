package org.shu.stock.signals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;

public abstract class Signal {
	
	private CommonStock stock;
	private List<Date> dates;
	
	public Signal(){
		dates = new ArrayList<Date>();	
	}
	
	public Signal(CommonStock stock){
		this.stock = stock;
		dates = new ArrayList<Date>();		
	}
	
	public void setCommonStock(CommonStock stock){
		this.stock = stock;
	}
	
	public CommonStock getCommonStock() {
		return stock;
	}
	public List<Date> getDate() {
		return dates;
	}
	
	public void findSignals(List<StockHistory> histories) {
	}
	
}
