package org.shu.main.scrape;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zenkey.net.prowser.Prowser;
import com.zenkey.net.prowser.Request;
import com.zenkey.net.prowser.Response;
import com.zenkey.net.prowser.Tab;

public class ScrapeFinviz {
	
	public String getHtmlFromUrl(String url){
		Prowser prowser = new Prowser();
	    Tab tab = prowser.createTab();
	    Request request = null;;
		try {
			request = new Request(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Response response = tab.go(request);
	    String html = response.getPageSource();
	    return html;
	}
	
		
	public String captureChart(String html, Integer firstValue){
		String captured = null;
		int val = html.indexOf(">" + firstValue + "</td>");
		captured = html.substring(val);
		captured = captured.substring(0,captured.indexOf("</tr></table>"));
		//System.out.println(captured);
		return captured;		
	}
	
	public List<String> captureSymbolNameIndustrySector(String chart){
		boolean firstRun = true;
		Map<Integer, List<String>> map = new HashMap<Integer,List<String>>();
		String symbol = "";
		String name = "";
		String industry = "";
		String sector = "";
		List<String> symbols = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		List<String> industries = new ArrayList<String>();
		List<String> sectors = new ArrayList<String>();
		String[] chartLines = parseChart(chart);
		
		for(String chartLine : chartLines){
			//Capture symbol
			int val = chartLine.indexOf("class=\"tab-link\">");
			int endVal = chartLine.indexOf("</a></td><td height=\"10\"");
			String subChart = chartLine.substring(val, endVal);
			symbol = subChart.replace("class=\"tab-link\">", "");	
			symbols.add(symbol);
			//Capture Symbol End
			chartLine = chartLine.substring(chartLine.indexOf("class=\"body-table-nw\">"));			
//			//Capture Name
//			val = chartLine.indexOf("class=\"body-table-nw\">");
//			if(val==0){
//				
//			}
//			endVal = chartLine.indexOf("</td><td height=\"10\"");
//			subChart = chartLine.substring(val, endVal);
//			if(!firstRun){				
//				chartLine = chartLine.substring(endVal);
//				val = chartLine.indexOf("class=\"body-table-nw\">");
//				endVal = chartLine.indexOf("</td><td height=\"10\"");	
//			}
//			name = subChart.replace("class=\"body-table-nw\">", "");
//			names.add(name);
//			
//			
//			//Capture Name End
//			chartLine = chartLine.substring(endVal);
//			
//			
//			//Capture Sector
//			val = chartLine.indexOf("class=\"body-table-nw\">");
//			endVal = chartLine.indexOf("</td><td height=\"10\"");
//			if(endVal < val){
//				chartLine = chartLine.substring(val);
//				val = chartLine.indexOf("class=\"body-table-nw\">");
//				endVal = chartLine.indexOf("</td><td height=\"10\"");				
//			}
//			subChart = chartLine.substring(val, endVal);
//			sector = subChart.replace("class=\"body-table-nw\">", "");
//			sectors.add(sector);
//			//Capture Sector End
//			
//			
//			
//			System.out.println("Symbol: " + symbol);
//			System.out.println("Name: " + name);
//			System.out.println("Industry: " + industry);
// 			System.out.println("Sector: " + sector);
// 			
// 			if(firstRun){
// 				firstRun = false;
// 			}
		
		}	

		return symbols;		
	}
	
	private String[] parseChart(String chart){		
	   String[] lines = chart.split("\r\n|\r|\n");
	   return  lines;
	}

}
