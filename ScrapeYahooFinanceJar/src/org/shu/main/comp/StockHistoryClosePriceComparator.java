package org.shu.main.comp;

import java.util.Comparator;

import org.shu.main.bean.StockHistory;

public class StockHistoryClosePriceComparator implements
		Comparator<StockHistory> {

	private static StockHistoryClosePriceComparator comparator = null;

	private StockHistoryClosePriceComparator() {
	}

	public static StockHistoryClosePriceComparator getInstance() {
		if (comparator == null) {
			comparator = new StockHistoryClosePriceComparator();
		}

		return comparator;
	}

	@Override
	public int compare(StockHistory s1, StockHistory s2) {
		if (s1.getClose() > s2.getClose()) {
			return 1;
		} else if (s1.getClose() == s2.getClose()) {
			return 0;
		} else
			return -1;
	}

}
