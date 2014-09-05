package org.shu.util.calculate;

import java.util.ArrayList;
import java.util.List;

public class MovingAverageCalculator {
	
	public List<Double> calculateSMA(int period, List<Double> orderedValues){
		List<Double> simpleMovingAverage = new ArrayList<Double>();
		Double[] temp = new Double[period];
		int counter = 0;		
		Double sma = null;
		
		for(int i=0; i < orderedValues.size(); i++ ){
			if(i < period){
				temp[counter] = orderedValues.get(i); 
				counter++;
				sma = calculateSma(temp, period);
				simpleMovingAverage.add(i, sma);
			}
			else
			{
				counter = 0;
				temp[counter] = orderedValues.get(i); 
				sma = calculateSma(temp, period);
				simpleMovingAverage.add(i, sma);
				
			}
		}
		
		return simpleMovingAverage;
	}
	
	private Double calculateSma(Double[] val, int period){
		Double returnVal = 0.0;
		for(int i=0; i < val.length; i++){
			if(val[i] == null){
				return 0.0;
			}else{
				returnVal = returnVal + val[i]; 
			}
		}
		
		
		return returnVal/period;		
	}
	

}
