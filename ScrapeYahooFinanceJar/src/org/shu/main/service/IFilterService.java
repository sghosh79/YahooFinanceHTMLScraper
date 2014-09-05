package org.shu.main.service;

import java.util.List;

import org.shu.main.bean.CommonStock;

public interface IFilterService {
	
	public List<CommonStock> run(List<CommonStock> stock);
	
	public boolean requiresPeriod();
	

}
