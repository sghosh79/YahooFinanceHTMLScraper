package org.shu.main.signal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.shu.constants.SignalConstants;
import org.shu.main.bean.Signal;
import org.shu.main.bean.StochasticValues;

public class StochasticSignalCalculator {
	
	public List<Signal> getStochasticSignal(List<StochasticValues> sv){
		List<Signal> signals = new ArrayList<Signal>();
		Signal signal = new Signal();
		double tempK = 0;
		double tempD = 0; 
		double tempK2;
		double tempD2;
		Date date = null;
		StochasticValues sVal = null;
		tempK = sv.get(0).getK();
		tempD = sv.get(0).getD();
		for(int i = 1; i < sv.size(); i++) {
			sVal = sv.get(i);			
			tempK2 = sVal.getK();
			tempD2 = sVal.getD();
			//System.out.println("Date: " + sVal.getDate());
			//System.out.println("K: " + tempK2);
			//System.out.println("D: " + tempD2);
			
			if((tempK > tempD && tempK2 > tempD2) || (tempK < tempD && tempK2 < tempD2))
				signal.setBuySell(SignalConstants.DO_NOTHING);
			else if((tempK > tempD && tempK2 < tempD2) && (tempK < 80 && tempD < 80))
				signal.setBuySell(SignalConstants.SELL_TRIGGER);
			else if(tempK < 30 && tempD < 30)
				signal.setBuySell(SignalConstants.BUY_TRIGGER);
			
			signal.setDate(sVal.getDate());
			signals.add(signal);
			
			tempK = tempK2;
			tempD = tempD2;
			
		}
		
		return signals;	
	}

}
