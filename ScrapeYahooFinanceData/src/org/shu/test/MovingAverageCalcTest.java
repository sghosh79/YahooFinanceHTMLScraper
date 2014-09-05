package org.shu.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockCalculatorDAO;
import org.shu.util.BatchUtil;
import org.shu.util.calculate.MovingAverageCalculator;

public class MovingAverageCalcTest {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	
	//This main program will find the buy and sell signals for the last year of a given stock
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		StockCalculatorDAO dao = new StockCalculatorDAO();
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		long startTime = System.currentTimeMillis();
		long endTime;
		List<Double> result = dao.getStockHistoryFor2011(new CommonStock("UNH"), conn);
		System.out.println(System.currentTimeMillis() - startTime + "ms");
		startTime = System.nanoTime();
		//List<Double> ma = buildMovingAverage(30);
		MovingAverageCalculator calc = new MovingAverageCalculator();

		
		List<Double> nineDayMa = calc.calculateSMA(9, result);
		List<Double> fourDayMa = calc.calculateSMA(4, result);
		
		Double yesterdayNine = 0.0;
		Double yesterdayFour = 0.0;
		boolean buySell = false;
		
		for(int i = 0; i<nineDayMa.size(); i++){			
			if(nineDayMa.get(i) > fourDayMa.get(i)){
				System.out.println("9 day above 4 day");
				if(buySell){
					System.out.println("Buy");
					buySell = false;
				}
			}else if(fourDayMa.get(i) > nineDayMa.get(i)){
				System.out.println("4 day above 9 day");
				if(!buySell){
					System.out.println("Sell");
					buySell = true;
				}
			}
		}
			
		endTime = (System.nanoTime() - startTime) / 1000000;
		System.out.println(startTime + "ms");
		System.out.println(endTime + "ms");
		conn.close();

	}
	
	public static List<Double> buildMovingAverage(int size){
		List<Double> vals = new ArrayList<Double>();
		for(int i = 0; i < size; i++){
			vals.add(i,i+3.0);
		}
		return vals;	
	}

}
