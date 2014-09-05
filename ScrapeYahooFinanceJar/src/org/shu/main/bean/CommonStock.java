package org.shu.main.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonStock {

	private String stockSymbol;
	private int stockKey;
	private List<StockHistory> historyList;
	private Set<Indicator> indicatorSet = new HashSet<Indicator>(); 
	private HashMap<String, List<CommonIndicators>> commonIndicatorMap = new HashMap<String, List<CommonIndicators>>();
	
	public CommonStock(){
		stockSymbol = "";
	}
	public CommonStock( String stockSymbol){
		this.stockSymbol = stockSymbol;
	}
	public CommonStock(int stockKey, String stockSymbol){
		this.stockSymbol = stockSymbol;
		this.stockKey = stockKey;
	}
	
	
	public int getStockKey() {
		return stockKey;
	}
	public void setStockKey(int stockKey) {
		this.stockKey = stockKey;
	}
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public List<StockHistory> getHistoryList() {
		return historyList;
	}
	public void setHistoryList(List<StockHistory> historyList) {
		this.historyList = historyList;
	}
	public Set<Indicator> getIndicatorSet() {
		return indicatorSet;
	}
	public void setIndicatorSet(Set<Indicator> indicatorSet) {
		this.indicatorSet = indicatorSet;
	}
	public void addIndicator(Indicator i){
		indicatorSet.add(i);
	}
	public void setCommonIndicatorMap(HashMap<String, List<CommonIndicators>> commonIndicatorMap) {
		this.commonIndicatorMap = commonIndicatorMap;
	}
	public HashMap<String, List<CommonIndicators>> getCommonIndicatorMap() {
		return commonIndicatorMap;
	}
	
	public void addCommonIndicator(String indicator, List<CommonIndicators> ci){
		commonIndicatorMap.put(indicator, ci);
	}
	
	
	
}
