package org.shu.test;

import java.sql.SQLException;

import org.shu.main.bean.CommonStock;
import org.shu.main.dao.StockDAO;

public class StockDAOTest {
	
	public static void main(String[] s) throws ClassNotFoundException, SQLException{
		StockDAO dao = new StockDAO();
		dao.getHighLowTodayForStock(new CommonStock());
	}

}
