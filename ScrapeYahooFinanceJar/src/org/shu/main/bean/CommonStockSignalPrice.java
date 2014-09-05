package org.shu.main.bean;

import java.util.Date;

public class CommonStockSignalPrice {
	
	private CommonStock stock;
	private String indicatorType;
	private int low;
	private int high;
	private String signal;
	private Date date;
	
	public CommonStock getStock() {
		return stock;
	}
	public void setStock(CommonStock stock) {
		this.stock = stock;
	}
	public String getIndicatorType() {
		return indicatorType;
	}
	public void setIndicatorType(String indicatorType) {
		this.indicatorType = indicatorType;
	}
	public int getLow() {
		return low;
	}
	public void setLow(int low) {
		this.low = low;
	}
	public int getHigh() {
		return high;
	}
	public void setHigh(int high) {
		this.high = high;
	}
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
