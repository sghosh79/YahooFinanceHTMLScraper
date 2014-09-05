package org.shu.util.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.shu.main.bean.StochasticValues;
import org.shu.main.bean.StockHistory;

public class StochasticCalculator {
	private List<StochasticValues> stochastics;
	
	public List<StochasticValues> getFastStochasticValues(int N, int M, List<StockHistory> history){
		stochastics = new ArrayList<StochasticValues>();
		getHighLowOfN(N, history);
		calculateK(N);
		calculateD(M, true);
		
		return stochastics;		
	}
	
	public List<StochasticValues> getSlowStochasticValues(int N, int M, List<StockHistory> history){
		stochastics = new ArrayList<StochasticValues>();
		getHighLowOfN(N, history);
		calculateK(N);
		calculateD(M,false);
		calculateD(M,true);
		
		return stochastics;		
	}
		
	//This method will set the high/low/close for a list of prices and a corresponding date
	private void getHighLowOfN(int N, List<StockHistory> historyList){
		StockHistory history = null;
		StochasticValues sv = null;
		Queue<Double> highs = new LinkedList<Double>();
		Queue<Double> lows = new LinkedList<Double>();
		
		history = historyList.get(0);		
		double tempHigh = history.getHigh();
		double tempLow = history.getLow();
		double removeHigh = 0;
		double removeLow = 0;
		
		highs.add(tempHigh);
		lows.add(tempLow);
		
		sv = new StochasticValues(0, 0, history.getDate(), 0, 0, history.getClose());
		stochastics.add(sv);
		
		for(int i = 1; i < historyList.size(); i++ ){		
			history = historyList.get(i);
			
			if(i < N){	
				highs.add(history.getHigh());
				lows.add(history.getLow());				 			 				 
			}else{
				 highs.add(history.getHigh());
				 lows.add(history.getLow());
				 removeHigh = highs.remove();
				 removeLow = lows.remove();
				 
				 if(history.getHigh() > tempHigh){
					 tempHigh = history.getHigh();
				 }else if(removeHigh == tempHigh){
					 tempHigh = Collections.max(highs);
				 }
				 
				 if(history.getLow() < tempLow){
					 tempLow = history.getLow();
				 }else if(removeLow == tempLow){
					 tempLow = Collections.min(lows);
				 }				 
			}
			
			if(i < N)
				sv = new StochasticValues(0, 0, history.getDate(), 0, 0, history.getClose());
			else
				sv = new StochasticValues(0, 0, history.getDate(), tempHigh, tempLow, history.getClose());			 
					 
			 stochastics.add(sv);			 
		}
		
//		for(StochasticValues sv2 : stochastics){
//			System.out.println(sv2.getDate() + "," + sv2.getHigh() +  "," + sv2.getLow() + "," + sv2.getClose());
//			System.out.println("Date: " + sv2.getDate());
//			System.out.println("Low: " + sv2.getLow());
//			System.out.println("High: " + sv2.getHigh());
//			System.out.println("Close: " + sv2.getClose());
//		}
 	}
	
	private void calculateK(int N){
		double K = 0;
		for(StochasticValues sv : stochastics){
			K = (sv.getClose() - sv.getLow())/(sv.getHigh() - sv.getLow()) * 100;
			sv.setK(K);
		}
		
		
//		for(StochasticValues sv2 : stochastics){
//		System.out.println(sv2.getDate() + "," + sv2.getHigh() +  "," + sv2.getLow() + "," + sv2.getClose() + "," + sv2.getK());
//		System.out.println("Date: " + sv2.getDate());
//		System.out.println("Low: " + sv2.getLow());
//		System.out.println("High: " + sv2.getHigh());
//		System.out.println("Close: " + sv2.getClose());
//		}			
	}
	
	//This can also be used to calculate %K for Slow Stochastics
	private void calculateD(int M, boolean fast){
		Queue<Double> kValues = new LinkedList<Double>();
		double D = 0;
		double temp = 0;	
		double temp2 = 0;
		
		for(int i = 0; i < stochastics.size(); i++){
			StochasticValues sv = stochastics.get(i);
			temp = sv.getK();
			kValues.add(temp);
			if(i >= M-1){
				temp2 = 0;
				for(Double v : kValues){
					temp2 = temp2 + v;					
				}
				kValues.remove();				
				D = temp2 / M;	
				
			}
			
			if(fast)
				sv.setD(D);		
			else
				sv.setK(D);
		}
		
		for(StochasticValues sv2 : stochastics){
//			System.out.println(sv2.getDate() + "," + sv2.getHigh() +  "," + sv2.getLow() + "," + sv2.getClose() + "," + sv2.getK() + "," + sv2.getD());
//			System.out.println("Date: " + sv2.getDate());
//			System.out.println("Low: " + sv2.getLow());
//			System.out.println("High: " + sv2.getHigh());
//			System.out.println("Close: " + sv2.getClose());
			}	
			
			
		
	}
	
	

}
