package org.shu.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MultiThreadProject implements Runnable {
	
	
	private String url;
	
	public MultiThreadProject(String url){
		this.url = url;
	}
	
	public static void main (String [] args) throws Exception{
		MultiThreadProject mtp = new MultiThreadProject("http://www.computercontrol.net/indiaIT.htm");
		Long startTime = System.currentTimeMillis();		
		System.out.println(mtp.findLessThanCount("http://www.cnn.com/WORLD/?hpt=sitenav"));
		System.out.println(mtp.findLessThanCount("http://www.cnn.com/POLITICS/?hpt=sitenav"));
		System.out.println(mtp.findLessThanCount("http://stackoverflow.com/questions/1414302/how-can-i-get-html-content-from-a-specific-url-on-server-side-by-using-java"));
		Long endTime = System.currentTimeMillis() - startTime;
		System.out.println("first start endtime " + endTime);

		
		MultiThreadProject mtp2 = new MultiThreadProject("http://www.cnn.com/WORLD/?hpt=sitenav");
		MultiThreadProject mtp3 = new MultiThreadProject("http://www.cnn.com/POLITICS/?hpt=sitenav");
		MultiThreadProject mtp4 = new MultiThreadProject("http://stackoverflow.com/questions/1414302/how-can-i-get-html-content-from-a-specific-url-on-server-side-by-using-java");

		new Thread(mtp2).start();
		new Thread(mtp3).start();
		new Thread(mtp4).start();
	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("calculating thread");
			Long startTime2 = System.currentTimeMillis();	
			System.out.println(findLessThanCount(url));
			Long endTime3 = System.currentTimeMillis() - startTime2;
			System.out.println("endtime3 " + endTime3);			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private String readPage(String url) throws Exception{
		URL url1 = new URL(url);
		URLConnection yc = url1.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String inputLine = "";
        
        while((inputLine = in.readLine()) != null) 
        	sb.append(inputLine);
        
        in.close();
       
		
		return sb.toString();
		
	}
	
	public int findLessThanCount(String url) throws Exception{
		String page = readPage(url);
		
		int count = 0;
		
		for(int i = 0; i < page.length(); i++){
			if(page.charAt(i)=='>')
				count++;
		}		
		
		return count;		
	}

}
