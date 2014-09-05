package org.shu.main.scrape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;

import com.zenkey.net.prowser.Prowser;
import com.zenkey.net.prowser.Request;
import com.zenkey.net.prowser.Response;
import com.zenkey.net.prowser.Tab;

public class ScrapeYahooCsv {
	
	public String getMonth(int month){
		
		switch (month) {
        case 0:  return "00";
        case 1:  return "01";
        case 2:  return "02";
        case 3:  return "03";
        case 4:  return "04";
        case 5:  return "05";
        case 6:  return "06";
        case 7:  return "07";
        case 8:  return "08";
        case 9:  return "09";
        case 10: return "10";
        case 11: return "11";
        default: return "error";
		}
		
	}
	
	public List<StockHistory> getStockHistoryByDate(CommonStock stock, Date dateStart, Date dateEnd) {
		List<StockHistory> list = new ArrayList<StockHistory>();
		String[] values = null;
        BufferedReader br = null;
        boolean firstRun = true;
        StockHistory history = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            String downloadSite = getCsvUrlForDates(stock, dateStart, dateEnd);
            URL url = new URL(downloadSite);
            InputStream is = url.openConnection().getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
            {
            	values = line.split(",");
            	if(values.length > 3 && firstRun == false){
            		history = new StockHistory();
            		history.setDate(format.parse(values[0]));
            		history.setOpen(Double.parseDouble(values[1]));
            		history.setHigh(Double.parseDouble(values[2]));
            		history.setLow(Double.parseDouble(values[3]));
            		history.setClose(Double.parseDouble(values[4]));
            		history.setVolume(Integer.parseInt(values[5]));
            		list.add(history);
            	}else{
            		firstRun = false;
            	}
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {             	
            	if (br != null) 
            		br.close(); 
        	} 
            catch(IOException e) 
        	{ 
        		e.printStackTrace(); 
    		}
        }
        
        return list;
	}
	public List<StockHistory> getAllStockHistory(CommonStock stock) {
		List<StockHistory> list = new ArrayList<StockHistory>();
		String[] values = null;
        BufferedReader br = null;
        boolean firstRun = true;
        StockHistory history = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            String downloadSite = getCsvUrl(stock);
            URL url = new URL(downloadSite);
            InputStream is = url.openConnection().getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
            {
            	values = line.split(",");
            	if(values.length > 3 && firstRun == false){
            		history = new StockHistory();
            		history.setDate(format.parse(values[0]));
            		history.setOpen(Double.parseDouble(values[1]));
            		history.setHigh(Double.parseDouble(values[2]));
            		history.setLow(Double.parseDouble(values[3]));
            		history.setClose(Double.parseDouble(values[4]));
            		history.setVolume(Integer.parseInt(values[5]));
            		list.add(history);
            	}else{
            		firstRun = false;
            	}
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {             	
            	if (br != null) 
            		br.close(); 
        	} 
            catch(IOException e) 
        	{ 
        		e.printStackTrace(); 
    		}
        }
        
        return list;
	}
	
	//Modify this method to take dateStart and dateEnd
	private String getCsvUrlForDates(CommonStock stock, Date dateStart, Date dateEnd){		
		
	    String url = "http://ichart.finance.yahoo.com/table.csv?s=" + stock.getStockSymbol() + "&d=" + (getMonth(dateEnd.getMonth())) + "&e=" + dateEnd.getDate()+"&f="+dateEnd.getYear()+"" +
	    		"&g=d&a="+(getMonth(dateStart.getMonth()))+"&b="+dateStart.getDate()+"&c="+dateStart.getYear()+"&ignore=.csv";
	    return url; 
	

	
	}
	
	private String getCsvUrl(CommonStock stock){
//		Prowser prowser = new Prowser();
//	    Tab tab = prowser.createTab();
//	    Request request = null;;
//		try {
//			request = new Request("http://finance.yahoo.com/q/hp?s=" + stock.getStockSymbol() + "+Historical+Prices");
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    Response response = tab.go(request);
//	    String html = response.getPageSource();
//	    String url = html.substring(html.indexOf("http://ichart.finance.yahoo.com/table.csv?s="));
//	    url = url.substring(0,url.indexOf("\""));
	    String url = "http://ichart.finance.yahoo.com/table.csv?s=" + stock.getStockSymbol() + "&d=9&e=22&f=2012&g=d&a=6&b=19&c=2005&ignore=.csv";
	    return url;
	}
	
	public List<StockHistory> getTodaysStockHistory(CommonStock stock) {
		List<StockHistory> list = new ArrayList<StockHistory>();
		String[] values = null;
        BufferedReader br = null;
        boolean firstRun = true;
        StockHistory history = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            String downloadSite = getSingleCsvUrl(stock);
            URL url = new URL(downloadSite);
            InputStream is = url.openConnection().getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
            {
            	values = line.split(",");
            	if(values.length > 3 && firstRun == false){
            		history = new StockHistory();
            		history.setDate(format.parse(values[0]));
            		history.setOpen(Double.parseDouble(values[1]));
            		history.setHigh(Double.parseDouble(values[2]));
            		history.setLow(Double.parseDouble(values[3]));
            		history.setClose(Double.parseDouble(values[4]));
            		history.setVolume(Integer.parseInt(values[5]));
            		list.add(history);
            	}else{
            		firstRun = false;
            	}
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {             	
            	if (br != null) 
            		br.close(); 
        	} 
            catch(IOException e) 
        	{ 
        		e.printStackTrace(); 
    		}
        }
        
        return list;
	}
	
	private String getSingleCsvUrl(CommonStock stock){		
	    Calendar c = Calendar.getInstance();
	    int day, nextDay, month, year;
	    day = c.get(Calendar.DAY_OF_MONTH)-1;
	    nextDay = day + 1;
	    month = c.get(Calendar.MONTH);
	    year = c.get(Calendar.YEAR);	    
	
	    String url = "http://ichart.finance.yahoo.com/table.csv?s=" + stock.getStockSymbol() + "&a="+month+"&b="+day+"&c="+year+"&d="+month+"&e="+nextDay+"&f="+year+"&g=d&ignore=.csv";
	    
	    return url;
	}
	
	
	

}
