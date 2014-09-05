package org.shu.main.service.calc;

import java.util.List;

import org.shu.main.bean.CommonStockSignalPrice;

public interface IDailySMACalcService {
	
	public List<CommonStockSignalPrice> getStockSignals();

}
