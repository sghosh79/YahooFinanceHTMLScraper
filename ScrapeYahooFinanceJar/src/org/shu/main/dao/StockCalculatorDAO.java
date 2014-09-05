package org.shu.main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.util.BatchUtil;
import org.shu.util.DateUtil;

public class StockCalculatorDAO {
	
	static final String GET_STOCK_SYMBOLS = "select stock_key, stock_symbol from YHOOFINDB.MASTER_STOCK";
	static final String GET_STOCK_HISTORY_FOR_2011 = "select day_close, volume from YHOOFINDB.stock_daily_history s join YHOOFINDB.master_stock m on  s.stock_key = m.stock_key " +
			" and dt >= to_date( '01-JAN-2011','DD-MON-YYYY')" +
			" and dt < to_date( '31-DEC-2011','DD-MON-YYYY')" +
			" and m.stock_symbol = ? " +
			" order by dt asc";
	
	static final String GET_STOCK_HISTORY_INFO_FOR_2011 = "select volume, day_close, dt from YHOOFINDB.stock_daily_history s join YHOOFINDB.master_stock m on  s.stock_key = m.stock_key " +
	" and dt >= to_date( '01-JAN-2011','DD-MON-YYYY')" +
	" and dt < to_date( '31-DEC-2011','DD-MON-YYYY')" +
	" and m.stock_symbol = ? " +
	" order by dt asc";
	
//	static final String GET_STOCK_HISTORY_INFO_FOR_GIVEN_X = "select volume, day_close, dt from YHOOFINDB.stock_daily_history s join YHOOFINDB.master_stock m on  s.stock_key = m.stock_key " +
//	" and dt >= to_date( ?,'DD-MON-YYYY')" +
//	" and dt < to_date( ?,'DD-MON-YYYY')" +
//	" and m.stock_symbol = ? " +
//	" order by dt desc";
	
	static final String GET_STOCK_HISTORY_INFO_FOR_GIVEN_X = 
			"SELECT * FROM " +
				"(SELECT * FROM yhoofindb.stock_daily_history s " +
				"JOIN yhoofindb.master_stock m " +
				"ON s.stock_key = m.stock_key " +
				"WHERE m.stock_symbol = ? " +
				"ORDER BY dt desc) " +
			"WHERE ROWNUM <= ?";
	
	static final String GET_STOCK_HISTORY_INFO_FOR_GIVEN_X_ORDER_BY_DSC  =
		"SELECT *  FROM (SELECT * FROM " +
			"(SELECT * FROM yhoofindb.stock_daily_history s " +
			"JOIN yhoofindb.master_stock m " +
			"ON s.stock_key = m.stock_key " +
			"WHERE m.stock_symbol = ? " +
			"ORDER BY dt desc) " +
		"WHERE ROWNUM <= ? ) ORDER by dt ASC";
	
	//Can not be re-used, need to append IN condition
	private static final String GET_STOCK_HISTORY_FOR_MULTIPLE_STOCK_DATE_RANGE = "select MS.STOCK_SYMBOL, " +
			"SDH.DAY_OPEN, SDH.DAY_CLOSE, SDH.DAY_HIGH, SDH.DAY_LOW, SDH.VOLUME, SDH.DT " +
			"from YHOOFINDB.STOCK_DAILY_HISTORY sdh " +
			"join YHOOFINDB.MASTER_STOCK ms on sdh.STOCK_KEY = ms.STOCK_KEY " +
			"where sdh.DT >= to_date (?)  and sdh.dt <= to_date(?)";
	private static final String ORDER_BY_DATE_DESC = " order by sdh.dt asc" ;
	
	public static void main(String [] args){
		StockCalculatorDAO dao = new StockCalculatorDAO();
		BatchUtil util = new BatchUtil();
		CommonStock unh = new CommonStock("UNH");
		CommonStock gs = new CommonStock("GS");
		List<CommonStock> list = new LinkedList<CommonStock>();
		list.add(unh);
		list.add(gs);
		Date dateStart = new Date(114, 3, 1);
		Date dateEnd = new Date(114, 3, 29);

		try {
			dao.populateMultipleStockHistoryForDateRange(list, dateStart, dateEnd);
			
			//Collections.sort(unh.getHistoryList());
			for(StockHistory s : unh.getHistoryList()){
				System.out.println("UNH " + s.getDate() + " " + s.getClose());
			}
			
			//Collections.sort(gs.getHistoryList());
			for(StockHistory s : gs.getHistoryList()){
				System.out.println("GS " + s.getDate() + " " + s.getClose());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//			List<StockHistory> history = dao.getStockHistoryForLastFewDays(new CommonStock("UNH"), util.getConnection(), 30);
//			int count = 0;
//			for(StockHistory h : history){
//				System.out.println(h.getClose() + " " + h.getVolume() + " date: " + h.getDate());
//				count++;
//				if(count >= 10)
//					break;
////			}
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	//This method will return the stock history for a given stock for the last X days given (Example - 20 would give the last 20 days from today)
	public List<StockHistory> getStockHistoryForLastFewDays(CommonStock stock, Connection conn, int numOfDaysRequested) throws ClassNotFoundException, SQLException{
		List<StockHistory> result = new ArrayList<StockHistory>();
		BatchUtil util = new BatchUtil();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_HISTORY_INFO_FOR_GIVEN_X);
		ResultSet rs = null;
		 		
		try{
//			String dateStart =  MonthUtil.getDateStart(numOfDaysRequested);
//			String dateEnd = MonthUtil.getDateEnd();
			ps.setString(1,stock.getStockSymbol());
			ps.setInt(2, numOfDaysRequested);
			rs = ps.executeQuery();
			while (rs.next()) {	
				StockHistory history = new StockHistory();
				double close = rs.getDouble("day_close");
				double high = rs.getDouble("day_high");
				double low = rs.getDouble("day_low");
				double open = rs.getDouble("day_open");
				Date date = rs.getDate("dt");
				int vol = rs.getInt("volume");
				history.setOpen(open);
				history.setClose(close);
				history.setHigh(high);
				history.setLow(low);
				history.setDate(date);
				history.setVolume(vol);
				result.add(history);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ps.close();
		
		}
		
		return result;		
	}
	
	
	
	//This method will return the stock history for a given stock for the last X days given (Example - 20 would give the last 20 days from today)
	public List<StockHistory> getStockHistoryForLastFewDaysReverseOrder(CommonStock stock, Connection conn, int numOfDaysRequested) throws ClassNotFoundException, SQLException{
		List<StockHistory> result = new ArrayList<StockHistory>();
		BatchUtil util = new BatchUtil();
		//Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_HISTORY_INFO_FOR_GIVEN_X_ORDER_BY_DSC);
		ResultSet rs = null;
		 		
		try{
//			String dateStart =  MonthUtil.getDateStart(numOfDaysRequested);
//			String dateEnd = MonthUtil.getDateEnd();
			ps.setString(1,stock.getStockSymbol());
			ps.setInt(2, numOfDaysRequested);
			rs = ps.executeQuery();
			while (rs.next()) {	
				StockHistory history = new StockHistory();
				double close = rs.getDouble("day_close");
				double high = rs.getDouble("day_high");
				double low = rs.getDouble("day_low");
				Date date = rs.getDate("dt");
				int vol = rs.getInt("volume");
				history.setClose(close);
				history.setHigh(high);
				history.setLow(low);
				history.setDate(date);
				history.setVolume(vol);
				result.add(history);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ps.close();
			//conn.close();
		}
		
		return result;		
	}
	
	//This method will return the stock history for a given stock for the last X days given (Example - 20 would give the last 20 days from today)
	public void populateMultipleStockHistoryForDateRange(List<CommonStock> stock, java.util.Date dateStart, java.util.Date dateEnd) throws ClassNotFoundException, SQLException{
		List<StockHistory> historyList = null;
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		String sql = GET_STOCK_HISTORY_FOR_MULTIPLE_STOCK_DATE_RANGE + inCondition(stock) + ORDER_BY_DATE_DESC;
			
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = null;
		StockHistory history = null;
		Map<String, List<StockHistory>> historyMap = new HashMap<String, List<StockHistory>>();	
			 		
		try{
			ps.setDate(1, new java.sql.Date(dateStart.getTime()));
			ps.setDate(2, new java.sql.Date(dateEnd.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {	
				history = new StockHistory();
				double close = rs.getDouble("day_close");
				double high = rs.getDouble("day_high");
				double low = rs.getDouble("day_low");
				Date date = rs.getDate("dt");
				int vol = rs.getInt("volume");
				String sym = rs.getString("stock_symbol");
				history.setClose(close);
				history.setHigh(high);
				history.setLow(low);
				history.setDate(date);
				history.setVolume(vol);
				if(!historyMap.containsKey(sym)){
					historyList = new LinkedList<StockHistory>();					
					historyMap.put(sym, historyList);
				}else{
					historyList = historyMap.get(sym);
				}
				historyList.add(history);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ps.close();
			conn.close();
		}
		
		populateHistoryForStock(stock, historyMap);
	}
		
	
	private void populateHistoryForStock(List<CommonStock> stock,
			Map<String, List<StockHistory>> historyMap) {
		// TODO Auto-generated method stub
		for(CommonStock s : stock){
			s.setHistoryList(historyMap.get(s.getStockSymbol()));
		}	
	}


	private Map<String, CommonStock> returnStockMap(List<CommonStock> stock) {
		// TODO Auto-generated method stub
		Map<String, CommonStock> map = new HashMap<String, CommonStock>();
		for(CommonStock s : stock){
			map.put(s.getStockSymbol(), s);
		}
		return map;
	}


	//Creates a String of "in ('T','GS',UNH')" - stock symbols for in condition in SQL 
	private String inCondition(List<CommonStock> stock) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" and MS.STOCK_SYMBOL in ( ");
		
		for(CommonStock s : stock){
			sb.append("'" + s.getStockSymbol() + "',");
		}
		
		sb.append(")");
		
		return sb.toString().replace(",)" , ")");
	}


	//This method will return the stock history for the year of 2011 of a given stock
	public List<Double> getStockHistoryFor2011(CommonStock stock, Connection conn) throws ClassNotFoundException, SQLException{
		List<Double> result = new ArrayList<Double>();
		BatchUtil util = new BatchUtil();
		//Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_HISTORY_FOR_2011);
		ResultSet rs = null;
		 		
		try{
			ps.setString(1,stock.getStockSymbol());
			rs = ps.executeQuery();
			while (rs.next()) {		
				double symbol = rs.getDouble("day_close");
				result.add(symbol);
			}
			
		}catch(Exception e){
			conn.rollback();
			e.printStackTrace();
		}finally{
			ps.close();
			//conn.close();
		}
		
		return result;		
	}
	
	//This method will return the stock history for the year of 2011 of a given stock
	public List<StockHistory> getStockHistoryInfoFor2011(CommonStock stock, Connection conn) throws ClassNotFoundException, SQLException{
		List<StockHistory> result = new ArrayList<StockHistory>();
		BatchUtil util = new BatchUtil();
		StockHistory history = null;
		//Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_HISTORY_INFO_FOR_2011);
		ResultSet rs = null;
		 		
		try{
			ps.setString(1,stock.getStockSymbol());
			rs = ps.executeQuery();
			
			while (rs.next()) {	
				history = new StockHistory();
				double close = rs.getDouble("day_close");
				Date date = rs.getDate("dt");
				int volume = rs.getInt("volume");
				history.setDate(date);
				history.setVolume(volume);
				history.setClose(close);				
				result.add(history);
			}
			
		}catch(Exception e){
			conn.rollback();
			e.printStackTrace();
		}finally{
			ps.close();
			//conn.close();
		}
		
		return result;		
	}
	
	
}
