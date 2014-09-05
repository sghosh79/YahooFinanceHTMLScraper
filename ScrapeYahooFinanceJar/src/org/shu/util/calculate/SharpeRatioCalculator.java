package org.shu.util.calculate;

import java.util.ArrayList;
import java.util.List;

public class SharpeRatioCalculator implements RatioCalculator{
	
	public Double calculate(List<Double> orderedValues, int numOfTradingDays){
		Double ratio = null;
		List<Double> dailyReturns = calculateDailyReturn(orderedValues);
		Double dailyReturn = getMean(dailyReturns);
		Double stdDev = getStdDev(dailyReturns);
		
		ratio = Math.sqrt(numOfTradingDays)*(dailyReturn/stdDev);	

		return ratio;		
	}
	
	private List<Double> calculateDailyReturn(List<Double> orderedValues){
		Double dailyReturn = null;
		List<Double> dailyReturns = new ArrayList<Double>();
		
		for(int i=0; i < orderedValues.size(); i++){
			if(i==0){
				dailyReturns.add(0.0);
			}else{
				dailyReturn = (orderedValues.get(i) / orderedValues.get(i-1)) - 1;
				dailyReturns.add(dailyReturn);
			}
		}		
		
		return dailyReturns;
	}	
	

    private Double getMean(List<Double> orderedValues)
    {
    	Double sum = 0.0;
        for(Double a : orderedValues)
            sum += a;
        
        return sum/orderedValues.size();
    }

    private Double getVariance(List<Double> orderedValues)
    {
        Double mean = getMean(orderedValues);
        Double temp = 0.0;

        for(Double a :orderedValues)
            temp += (mean-a)*(mean-a);           
        
        return temp/orderedValues.size();
    }

    private Double getStdDev(List<Double> orderedValues)
    {
        return Math.sqrt(getVariance(orderedValues));
    }

}
