package org.shu.util;

import java.util.Calendar;

public class MonthUtil {	

	
	public static String getDateStart(int numOfDaysBefore){
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -numOfDaysBefore);
		sb.append(cal.get(Calendar.DATE));
		sb.append('-');
		sb.append(getMonth(cal.get(Calendar.MONTH)));
		sb.append('-');
		sb.append(cal.get(Calendar.YEAR));	
		return sb.toString();
	}
	
	//Returns today
	public static String getDateEnd(){
		StringBuilder sb = new StringBuilder();
		sb.append(Calendar.getInstance().get(Calendar.DATE));
		sb.append('-');
		sb.append(getMonth(Calendar.getInstance().get(Calendar.MONTH)));
		sb.append('-');
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		return sb.toString();	
	}
	
	private static String getMonth(int month){
			
			switch (month) {
	        case 0:  return "JAN";
	        case 1:  return "FEB";
	        case 2:  return "MAR";
	        case 3:  return "APR";
	        case 4:  return "MAY";
	        case 5:  return "JUN";
	        case 6:  return "JUL";
	        case 7:  return "AUG";
	        case 8:  return "SEP";
	        case 9:  return "OCT";
	        case 10: return "NOV";
	        case 11: return "DEC";
	        default: return "error";
			}
			
		}

}
