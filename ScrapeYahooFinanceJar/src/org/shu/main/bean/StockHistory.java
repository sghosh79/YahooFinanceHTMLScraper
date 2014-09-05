package org.shu.main.bean;

import java.util.Comparator;
import java.util.Date;

public class StockHistory implements Comparable<StockHistory> {
	
	private double open;
	private double close;
	private double high;
	private double low;
	private int volume;
	private Date date;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	@Override
	public int compareTo(StockHistory o) {
		return this.date.compareTo(o.date);
	}
	
	
}
