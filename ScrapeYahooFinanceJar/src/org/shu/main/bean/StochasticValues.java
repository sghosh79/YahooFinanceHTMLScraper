package org.shu.main.bean;

import java.util.Date;

public class StochasticValues {
	
	private double high;
	private double low;
	private double close;
	private double K;
	private double D;
	private Date date;
	
	public StochasticValues(double K, double D, Date date, double high, double low, double close){
		this.K = K;
		this.D = D;
		this.date = date;
		this.high = high;
		this.low = low;
		this.close = close;
	}

	public double getK() {
		return K;
	}

	public double getD() {
		return D;
	}

	public Date getDate() {
		return date;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public void setK(double k) {
		K = k;
	}

	public void setD(double d) {
		D = d;
	}
	
	

}
