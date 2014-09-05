package org.shu.test;

import java.sql.SQLException;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.Signal;
import org.shu.main.bean.StochasticValues;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.signal.StochasticSignalCalculator;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.StochasticCalculator;

public class TestStochasticSignals {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testStochasticSignalCalculator();

	}
	
	public static void testStochasticSignalCalculator(){
		double[] val;
		StochasticCalculator calc = new StochasticCalculator();
		BatchUtil util = new BatchUtil();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		StochasticSignalCalculator ssc = new StochasticSignalCalculator();
		try {
			List<StockHistory> history = dao.getStockHistoryForLastFewDaysReverseOrder(new CommonStock("FB"), util.getConnection(), 100);
			//calc.getFastStochasticValues(14, 3, history, "test");
			List<StochasticValues> sv = calc.getSlowStochasticValues(14, 3, history);
			List<Signal> signals = ssc.getStochasticSignal(sv);
			//calc.getHighLowOfN(14, history, "fake");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
