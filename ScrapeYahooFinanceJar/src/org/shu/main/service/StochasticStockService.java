//package org.shu.main.service;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.shu.constants.SignalConstants;
//import org.shu.main.bean.CommonStock;
//import org.shu.main.bean.Signal;
//import org.shu.main.bean.StochasticValues;
//import org.shu.main.bean.StockHistory;
//import org.shu.main.dao.StockCalculatorDAO;
//import org.shu.main.signal.StochasticSignalCalculator;
//import org.shu.util.BatchUtil;
//import org.shu.util.calculate.StochasticCalculator;
//
//public class StochasticStockService {
//	
//	private static List<CommonStock> run(){
//		List<CommonStock> todaysStocks = new ArrayList<CommonStock>();
//		List<CommonStock> stocks = null;
//		List<StockHistory> history = null;
//		List<StochasticValues> sv = null;
//		List<Signal> signals = null;
//		double[] val;
//		StochasticCalculator calc = new StochasticCalculator();
//		BatchUtil util = new BatchUtil();
//		StockCalculatorDAO dao = new StockCalculatorDAO();
//		StochasticSignalCalculator ssc = new StochasticSignalCalculator();
//		Signal signal = null;
//		int count = 0;
//		Connection conn = null;
//		try {
//			conn = util.getConnection();
//			stocks = getStocks();
//			for(CommonStock s : stocks){
//				history = dao.getStockHistoryForLastFewDaysReverseOrder(s, conn, 100);
//				sv = calc.getSlowStochasticValues(14, 3, history);
//				signals = ssc.getStochasticSignal(sv);
//				signal = (Signal) signals.get(signals.size() - 1); 
//				
//				if(signal.getBuySellSignal() == SignalConstants.BUY_TRIGGER){
//					todaysStocks.add(s);					
//				}
//			}
//			
//			 
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return todaysStocks;
//	}
//
//}
