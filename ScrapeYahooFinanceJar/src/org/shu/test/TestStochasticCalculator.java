package org.shu.test;

import java.sql.SQLException;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.StochasticCalculator;

public class TestStochasticCalculator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetHighLow();

	}
	
	public static void testGetHighLow(){
		double[] val;
		StochasticCalculator calc = new StochasticCalculator();
		BatchUtil util = new BatchUtil();
		StockCalculatorDAO dao = new StockCalculatorDAO();
		try {
			List<StockHistory> history = dao.getStockHistoryForLastFewDaysReverseOrder(new CommonStock("UNH"), util.getConnection(), 30);
			//calc.getFastStochasticValues(14, 3, history, "test");
			calc.getSlowStochasticValues(14, 3, history);
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
