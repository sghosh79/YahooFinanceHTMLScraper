package org.shu.main.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.util.BatchUtil;

public class StockDAO {
	
	static final String INITIAL_INSERT="INSERT INTO YHOOFINDB.MASTER_STOCK( STOCK_KEY, " +
			"STOCK_SYMBOL, UPDT_DTTM, STOCK_IND_KEY ) VALUES ( YHOOFINDB.MASTER_STOCK_SEQ.nextval, ?, SYSDATE, 0)"; 

	static final String INSERT_STOCK_HISTORY="INSERT INTO YHOOFINDB.STOCK_DAILY_HISTORY ( STOCK_KEY, DT, UPDT_DTTM, DAY_OPEN, DAY_HIGH, DAY_LOW, DAY_CLOSE, VOLUME )" +
		" VALUES(?, ?, SYSDATE, ?,?,?,?,?)";
	
	static final String GET_STOCK_SYMBOLS = "select stock_key, stock_symbol from YHOOFINDB.MASTER_STOCK";
	
	static final String GET_STOCK_SYMBOLS_OVER_1MIL = "select stock_symbol from YHOOFINDB.stock_daily_history sdh join YHOOFINDB.master_stock ms on sdh.stock_key = ms.stock_key where dt >= SYSDATE-2 and volume > 1000000";
	
	static final String TEST_STRING = "select * from YHOOFINDB.stock_daily_history sdh join YHOOFINDB.master_stock ms on sdh.stock_key = ms.stock_key where ms.stock_symbol = 'AON' and sdh.dt > to_date ('1-SEP-2012') order by dt asc";
	
	static final String UPDATE_MARKET_CAP = "UPDATE yhoofindb.master_stock SET market_cap=? WHERE stock_symbol=?";
	
	static final String GET_STOCK_SYMBOLS_OVER_10_B_MKT_CAP = "select * from yhoofindb.master_stock WHERE market_cap > 10000000000";
	
	static final String GET_STOCK_SYMBOLS_OVER_10_B_MKT_CAP_AND_1_M_VOL = "select distinct m.stock_symbol from yhoofindb.master_stock m join yhoofindb.stock_daily_history s on m.stock_key = s.stock_key"  +
		" WHERE market_cap > 10000000000 and volume > 1000000";
	
	final static String GET_HIGH_LOW_TODAY_FOR_STOCK = "{call yhoofindb.GET_HIGH_LOW_TODAY_FOR_SYMBOL(?,?)}";
	
	public static void main(String[] args){
		try {
			new StockDAO().getStockSymbolsOver10BMktCapAnd1MVolume();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public List<CommonStock> getStockSymbolsOver10BMktCapAnd1MVolume() throws ClassNotFoundException, SQLException{
			List<CommonStock> stocks = new ArrayList<CommonStock>();
			BatchUtil util = new BatchUtil();
			Connection conn = util.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_STOCK_SYMBOLS_OVER_10_B_MKT_CAP_AND_1_M_VOL);
			ResultSet rs = null;
			CommonStock stock = null;
			 		
			try{
				rs = ps.executeQuery();
				while (rs.next()) {				   
					String symbol = rs.getString("stock_symbol");
					if(symbol != null && symbol != ""){
						stock = new CommonStock();
						stock.setStockSymbol(symbol);
						stocks.add(stock);
					}
				}
				
			}catch(Exception e){
				conn.rollback();
				e.printStackTrace();
			}finally{
				ps.close();
				conn.close();
			}
			
			return stocks;		
		}
	
	public static void test() throws ClassNotFoundException, SQLException{
//		CommonStock stock = new CommonStock("A");
//		CommonStock stock2 = new CommonStock("AON");
//		Map<CommonStock, Long> stockMap = new HashMap<CommonStock,Long>();
//		StockDAO dao = new StockDAO();
//		
//		stockMap.put(stock, new Long("10000000"));
//		stockMap.put(stock2, new Long("12000000"));
//		dao.insertMarketCap(stockMap);
		
		
	}
	
	
	//This method is for inserting a list of symbols into the MASTER_STOCK table
	public boolean initialInsert(List<String> symbols) throws ClassNotFoundException, SQLException{
		boolean isSuccess = false;
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(INITIAL_INSERT);		
		 
		for (String symbol: symbols) {		 
		    ps.setString(1, symbol);
		    ps.addBatch();
		}
		
		try{
			ps.executeBatch();
			conn.commit();
		}catch(Exception e){
			conn.rollback();
		}finally{
			ps.close();
			conn.close();
		}
		
		return isSuccess;		
	}
	
	//This method will insert the history of a stock into the STOCK_DAILY_HISTORY table
	public boolean insertStockHistory(CommonStock stock,List<StockHistory> history, Connection conn) throws ClassNotFoundException, SQLException{
		boolean isSuccess = false;
		BatchUtil util = new BatchUtil();
		boolean connPassedIn = false;
		if(conn == null){
			conn = util.getConnection();
		}else{
			connPassedIn = true;
		}
			
		PreparedStatement ps = conn.prepareStatement(INSERT_STOCK_HISTORY);

		for (StockHistory hist: history) {
			ps.setInt(1, stock.getStockKey());
			ps.setDate(2, new Date(hist.getDate().getTime()));
			ps.setDouble(3, hist.getOpen());
			ps.setDouble(4, hist.getHigh());
			ps.setDouble(5, hist.getLow());
			ps.setDouble(6, hist.getClose());
			ps.setDouble(7, hist.getVolume()); 
		    ps.addBatch();
		}
				
		try{
			ps.executeBatch();
			conn.commit();
		}catch(Exception e){
			conn.rollback();
			e.printStackTrace();
		}finally{
			ps.close();
			if(!connPassedIn)
				conn.close();
		}
		
		return isSuccess;		
	}
	
	
	public List<CommonStock> getStockSymbols() throws ClassNotFoundException, SQLException{
		List<CommonStock> stocks = new ArrayList<CommonStock>();
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_SYMBOLS);
		ResultSet rs = null;
		CommonStock stock = null;
		 		
		try{
			rs = ps.executeQuery();
			while (rs.next()) {				   
				int key = rs.getInt("stock_key");
				String symbol = rs.getString("stock_symbol");
				if(symbol != null && symbol != ""){
					stock = new CommonStock();
					stock.setStockKey(key);
					stock.setStockSymbol(symbol);
					stocks.add(stock);
				}
			}
			
		}catch(Exception e){
			conn.rollback();
		}finally{
			ps.close();
			conn.close();
		}
		
		return stocks;		
	}
	
	public List<CommonStock> getStockSymbolsVolumeGreaterThanOneMillion() throws ClassNotFoundException, SQLException{
		List<CommonStock> stocks = new ArrayList<CommonStock>();
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_SYMBOLS_OVER_1MIL);
		ResultSet rs = null;
		CommonStock stock = null;
		 		
		try{
			rs = ps.executeQuery();
			while (rs.next()) {				 
				String symbol = rs.getString("stock_symbol");
				if(symbol != null && symbol != ""){
					stock = new CommonStock();
					stock.setStockSymbol(symbol);
					stocks.add(stock);
				}
			}
			
		}catch(Exception e){
			conn.rollback();
		}finally{
			ps.close();
			conn.close();
		}
		
		return stocks;		
	}
	
    @SuppressWarnings(value = { "rawtypes" })
	public void insertMarketCap(Map<CommonStock,Long> stockInfoMap){
		BatchUtil util = new BatchUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		int[] success;
		try{
			conn = util.getConnection();			
			
			ps = conn.prepareStatement(UPDATE_MARKET_CAP);
			
			Iterator it = stockInfoMap.entrySet().iterator();
			
		    while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
			        Long marketCap = (Long) pairs.getValue();
			        CommonStock stock = (CommonStock) pairs.getKey();
			        it.remove(); // avoids a ConcurrentModificationException
			        ps.setLong(1, marketCap);
			        ps.setString(2, stock.getStockSymbol());
			        ps.addBatch();
		    }		
				
		
			ps.executeBatch();
			conn.commit();
			//System.out.println("committed transaction");
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
	}
    
    public List<CommonStock> getStockSymbolsOver10BMktCap() throws ClassNotFoundException, SQLException{
		List<CommonStock> stocks = new ArrayList<CommonStock>();
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		PreparedStatement ps = conn.prepareStatement(GET_STOCK_SYMBOLS_OVER_10_B_MKT_CAP);
		ResultSet rs = null;
		CommonStock stock = null;
		 		
		try{
			rs = ps.executeQuery();
			while (rs.next()) {				   
				int key = rs.getInt("stock_key");
				String symbol = rs.getString("stock_symbol");
				if(symbol != null && symbol != ""){
					stock = new CommonStock();
					stock.setStockKey(key);
					stock.setStockSymbol(symbol);
					stocks.add(stock);
				}
			}
			
		}catch(Exception e){
			conn.rollback();
		}finally{
			ps.close();
			conn.close();
		}
		
		return stocks;		
	}
    
    public List<StockHistory> getHighLowTodayForStock(CommonStock stock) throws ClassNotFoundException, SQLException{
    	List<StockHistory> historys = new ArrayList<StockHistory>();
    	StockHistory history;
		BatchUtil util = new BatchUtil();
		Connection conn = util.getConnection();
		
		CallableStatement  cs = conn.prepareCall(GET_HIGH_LOW_TODAY_FOR_STOCK);
		cs.setString(1, stock.getStockSymbol());
		cs.registerOutParameter(2, OracleTypes.CURSOR);
		cs.executeUpdate();
		try{
		ResultSet rs = (ResultSet) cs.getObject(2);						 		
		
			while (rs.next()) {
				history = new StockHistory();
				history.setClose(rs.getDouble("DAY_CLOSE"));
				history.setOpen(rs.getDouble("DAY_OPEN"));
				history.setHigh(rs.getDouble("DAY_HIGH"));
				history.setLow(rs.getDouble("DAY_LOW"));
				history.setDate(rs.getDate("DT"));
				historys.add(history);
			}
		}
			
		catch(Exception e){
			e.printStackTrace();
		}finally{
			cs.close();
			conn.close();
		}
		
    	return historys;
    }	

}
