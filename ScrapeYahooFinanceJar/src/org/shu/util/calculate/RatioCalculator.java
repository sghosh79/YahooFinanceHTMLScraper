package org.shu.util.calculate;

import java.util.List;

public interface RatioCalculator {
	public Double calculate(List<Double> orderedValues, int numOfTradingDays);

}
