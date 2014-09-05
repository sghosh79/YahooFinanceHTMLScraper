package org.shu.main;

import java.sql.SQLException;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockDAO;

public class GetListOfAllStocks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockDAO dao = new StockDAO();
		try {
			List<CommonStock> stocks = dao.getStockSymbols();
			for(CommonStock stock : stocks){
				System.out.println(stock.getStockSymbol());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
