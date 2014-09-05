package org.shu.util;

public class TimerUtil {

	private static Long startTime, endTime;
	public static void startTimer(){
		startTime = System.nanoTime();
	}
	
	public static void endTimer(){
		endTime = System.nanoTime() - startTime;
		System.out.println();
		System.out.println(endTime / 1000000);		
	}
}
