package org.shu.main.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.shu.main.bean.CommonStock;
import org.shu.main.bean.CommonStockCollection;

public interface StockServiceInt {



	public abstract void scrapeAndInsertMultipleStockHistoriesByDateMT(Date dateStart,
			Date dateEnd, List<CommonStock> list)
			throws ClassNotFoundException, SQLException;

	public abstract void scrapeAndInsertStockHistoryForToday(
			List<CommonStock> list);

	public abstract void scrapeAndInsertMultipleStockHistories(Date dateStart,
			Date dateEnd, List<CommonStock> list) throws ClassNotFoundException, SQLException;

	public void scrapeAndInsertStockHistoryForTodayMT(List<CommonStock> list) throws ClassNotFoundException, SQLException;
}