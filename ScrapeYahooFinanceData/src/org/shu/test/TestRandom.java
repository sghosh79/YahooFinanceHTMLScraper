package org.shu.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestRandom {
	
	public static void main(String[] x){
		
		Calendar cal = Calendar.getInstance();
		//Date date = cal.getTime();
		System.out.println(Calendar.getInstance().get(cal.MONTH));
		//System.out.println(date.getDay() + " " + date.getMonth() + " " + date.getYear());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date2 = new Date();
		System.out.println(dateFormat.format(date2));
	}

}
