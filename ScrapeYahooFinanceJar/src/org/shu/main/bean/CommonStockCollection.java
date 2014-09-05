package org.shu.main.bean;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CommonStockCollection  {
	
	private CommonStock stock;
	private Collection price;
	private List<Date> dates;
	private List<Double> nineDayMa;
	private List<Double> fourDayMa;
	private int count;
	
	/**
	 * @return the stock
	 */
	public CommonStock getStock() {
		return stock;
	}
	/**
	 * @param stock the stock to set
	 */
	public void setStock(CommonStock stock) {
		this.stock = stock;
	}

	/**
	 * @return the dates
	 */
	public List<Date> getDates() {
		return dates;
	}
	/**
	 * @param dates the dates to set
	 */
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	/**
	 * @return the nineDayMa
	 */
	public List<Double> getNineDayMa() {
		return nineDayMa;
	}
	/**
	 * @param nineDayMa the nineDayMa to set
	 */
	public void setNineDayMa(List<Double> nineDayMa) {
		this.nineDayMa = nineDayMa;
	}
	/**
	 * @return the fourDayMa
	 */
	public List<Double> getFourDayMa() {
		return fourDayMa;
	}
	/**
	 * @param fourDayMa the fourDayMa to set
	 */
	public void setFourDayMa(List<Double> fourDayMa) {
		this.fourDayMa = fourDayMa;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Collection price) {
		this.price = price;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the price
	 */
	public Collection getPrice() {
		return price;
	}
	
	

}
