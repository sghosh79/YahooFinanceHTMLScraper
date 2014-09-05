package org.shu.main.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.shu.main.bean.CommonIndicators;
import org.shu.main.bean.CommonStock;
import org.shu.main.bean.StockHistory;
import org.shu.util.DateUtil;

public class EMAService extends AbstractStockService{
	
	public static void main(String[] args) throws Exception{
		EMAService service = getInstance();
		List<CommonStock> s = new ArrayList<CommonStock>();
		s.add(new CommonStock("UNH"));
		service.populateStockHistoryForDateRange(s, DateUtil.getOneYearAgoForStartDate(), DateUtil.getDateEnd());
		service.setPeriod(10);
		service.calculateAndSetEMAValues(s.get(0));
		
		List<CommonIndicators> values = s.get(0).getCommonIndicatorMap().get("EMA");
		for(CommonIndicators ev : values){
			EMAValue v = (EMAValue) ev;
			System.out.println(v.getDate() + " " + v.getValue());
		}
		
	}
	

	private double k;
	
	
	public static EMAService service = null;
	
	private EMAService(){
		
	}
	
	@Override
	public void setPeriod(int period) {
		this.period = period;
		k = 2.0 / (period + 1);
	}
	
	public static synchronized EMAService getInstance(){		
		if(service == null){
				service = new EMAService();						
		}		
		return service;		
	}
	
	@Override
	public List<CommonStock> run(List<CommonStock> stock) {
		return null;
	}
	
	public void calculateAndSetEMAValues(CommonStock stock) throws Exception{
		List<CommonIndicators> value = new LinkedList<CommonIndicators>();	
		LinkedList<Double> valueQueue = new LinkedList<Double>();
		List<StockHistory> history = stock.getHistoryList();
		Collections.sort(history);
		double prevEMA = 0;
		
		if(period < 1){
			throw new Exception("Need to set period");
		}
		
		
		for(StockHistory h : history){
			if(valueQueue.size() < period){
				valueQueue.add(h.getClose());
			}
			else
			{
				valueQueue.add(h.getClose());
				prevEMA = getEMAValue(valueQueue, prevEMA, h.getClose());
				valueQueue.remove();
				valueQueue.add(h.getClose());
				value.add(new EMAValue(h.getDate(), prevEMA));
			}				
		}	
		valueQueue = null;
		
		stock.addCommonIndicator("EMA",value);
	}
	
	protected double getEMAValue(LinkedList<Double> vq, double prevEMA, double currentPrice) {
		double ema = 0;
		if(prevEMA == 0){
			for(Double d : vq){
				ema += d;
			}
			ema = (ema/period);
			
		}else{
			ema = (vq.getLast() * this.k) + (prevEMA * (1 - this.k));
		}
		
		return ema;
		
	}
	
	protected class EMAValue implements CommonIndicators{
		
		private Date date;
		private double value;
		
		public EMAValue(Date date, Double value){
			this.value = value;
			this.date = date;
		}
		
		public Date getDate() {
			return date;
		}
		public double getValue() {
			return value;
		}
		
	}

	@Override
	public boolean requiresPeriod() {
		// TODO Auto-generated method stub
		return true;
	}

}
