package org.shu.main.scrape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;

public class ScrapeYahooForDailyPrice {
	
	private String YAHOO_PAGE_STRING = "http://finance.yahoo.com/q?s=";
	
	public static void main(String[] args) throws IOException{
		ScrapeYahooForDailyPrice price = new ScrapeYahooForDailyPrice();
		price.getTodaysPricesFromYahoo(new CommonStock("UNH"));
	}
	
	public StockHistory getTodaysPricesFromYahoo(CommonStock stock){
		StockHistory history = new StockHistory();
		boolean close = false;
		boolean high = false;
		boolean low = false;
		boolean open = false;
		BufferedReader in = null;
		try{
			
		
		URL url = new URL(YAHOO_PAGE_STRING + stock.getStockSymbol());
		in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String inputLine;
		String stockSymbol;
		while ((inputLine = in.readLine()) != null){
			stockSymbol = stock.getStockSymbol().toLowerCase();
			if(inputLine.contains("yfs_l84_"+stockSymbol)){
				close = true;				
				history.setClose(setClose(inputLine, stockSymbol));
			}
			else if(inputLine.contains("Open:</th>"))
			{
				close = true;				
				setOpenHighLow(inputLine,history, stockSymbol);	
				break;
			}
		}
		
		}catch(IOException e){
			e.printStackTrace();			
		}finally{
			try {
				if(in != null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		return history;		
	}	
	private double setClose(String closeString, String stock){
		double close = 0.0;
		closeString = closeString.substring(closeString.indexOf("<span id=\"yfs_l84_"+stock.toLowerCase()));
		closeString = closeString.substring(closeString.indexOf('>')+1, closeString.indexOf("</span>"));
		
		if(closeString.contains(",")){
			closeString = closeString.replace(",", "");			
		}
		try{
			close = Double.parseDouble(closeString);
		}catch(Exception e){
			e.printStackTrace();
		}		
		return close;
	}
	
	private void setOpenHighLow(String input, StockHistory history, String stock){
		double open = 0.0;
		double low = 0.0;
		double high = 0.0;
		int volume = 0 ;
		
		try{
		input = input.substring(input.indexOf("Open:"));
		input = input.substring(input.indexOf("yfnc_tabledata1")+17);
		
		String openString = input.substring(0, input.indexOf("<"));	
		if(openString.contains(",")){
			openString = openString.replace(",", "");			
		}
		
		input = input.substring(input.indexOf("yfs_g53_"+stock));
		input = input.substring(input.indexOf('>')+1);
		String lowString = input.substring(0, input.indexOf("<"));
		if(lowString.contains(",")){
			lowString = lowString.replace(",", "");		
		}
		
		input = input.substring(input.indexOf("yfs_h53_"+stock));
		input = input.substring(input.indexOf('>')+1);
		String highString = input.substring(0, input.indexOf("<"));
		if(highString.contains(",")){
			highString = highString.replace(",", "");		
		}
		
		
		input = input.substring(input.indexOf("yfs_v53_"+stock));
		input = input.substring(input.indexOf('>')+1);
		String volumeString = input.substring(0, input.indexOf("<"));
		
			open = Double.parseDouble(openString);
			low = Double.parseDouble(lowString);
			high = Double.parseDouble(highString);
			volumeString = volumeString.replace(",", "");
			volume = Integer.parseInt(volumeString);
		}catch(Exception e){
			e.printStackTrace();
		}	
		
		history.setOpen(open);
		history.setHigh(high);
		history.setLow(low);
		history.setVolume(volume);
	}
}
