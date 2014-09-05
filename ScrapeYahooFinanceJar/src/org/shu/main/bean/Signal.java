package org.shu.main.bean;

import java.util.Date;

public class Signal {
	
	private Date date; 
	private int buySell;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getBuySellSignal() {
		return buySell;
	}
	public void setBuySell(int buySell) {
		 
		this.buySell = buySell;
		
//			switch (buySell) {
//		        case 1:  System.out.println("Buy");
//		                 break;
//		        case 3:  System.out.println("Do Nothing");
//		                 break;
//		        case 2: System.out.println("Sell");
//		                 break;
//			}
	}
	
	
	

}
