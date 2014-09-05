package org.shu.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.main.dao.StockDAO;
import org.shu.stock.signals.HammerSignal;
import org.shu.stock.signals.Signal;
import org.shu.util.BatchUtil;
import org.shu.util.TimerUtil;

public class TestHammerSignal {
	
	public static void main(String[] args){
		
		List<CommonStock> stocks;
		StockCalculatorDAO calcDao = new StockCalculatorDAO();
		StockDAO dao = new StockDAO();
		BatchUtil util = new BatchUtil();
		List<Signal> signals = new ArrayList<Signal>();
		Signal signal = null;
		Connection conn = null;
		
		try 
		{
			stocks = dao.getStockSymbolsOver10BMktCap();
			conn = util.getConnection();

			TimerUtil.startTimer();
			for(CommonStock stock : stocks) {
				signal = new HammerSignal(stock);
				List<StockHistory> history = calcDao.getStockHistoryForLastFewDays(stock, conn, 200);				
				signal.findSignals(history);
				signals.add(signal);
			}
			TimerUtil.endTimer();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		for(Signal s : signals){
			System.out.println(s.getCommonStock().getStockSymbol() + " " + s.getDate());
		}
		
		
	}

}
