package org.shu.main.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockDAO;

public class ReadMarketCap {
	
	private static String fileNameAndLocation = "C:\\Users\\Baby\\Desktop\\finviz\\results.csv";
	
	private Map<CommonStock, Long> stockMap = new HashMap<CommonStock, Long>();
	
	public static void main(String[] args){
		ReadMarketCap cap = new ReadMarketCap();
		Map<CommonStock, Long> cap2 = null;
		cap2 = cap.getMarketCap();
		StockDAO dao = new StockDAO();
		dao.insertMarketCap(cap2);
	}
	
	public Map<CommonStock, Long> getMarketCap(){
		
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
			String[] fileOutput; 
 
			br = new BufferedReader(new FileReader(fileNameAndLocation));
 
			while ((sCurrentLine = br.readLine()) != null) {
				setStockMap(sCurrentLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return stockMap;
		
	}	
	
	private void setStockMap(String fileOutput){
		CommonStock stock = new CommonStock();
		String[] split = fileOutput.split(",");
		stock.setStockSymbol(split[0]);
		if(stock.getStockSymbol().equalsIgnoreCase("UNH")){
			System.out.println("Here");
		}
		long num = getIntegerRep(split[1]);
		
		System.out.println("Stock: " + stock.getStockSymbol() + "Num " + num);
		stockMap.put(stock, num);		
	}
	
	private long getIntegerRep(String val){
		String[] temp = val.split("[.]");
		long num = 0;
		try{
			if(temp[1].contains("B")){
				val = temp[0] + "000000000";
			}else{
				val = temp[0] + "000000";
			}
			num = Long.parseLong(val);
		}catch(Exception e){
		}
		return num;
	}

}
