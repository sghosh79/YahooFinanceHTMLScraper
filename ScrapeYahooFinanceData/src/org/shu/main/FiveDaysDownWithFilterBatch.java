package org.shu.main;

import java.util.List;

import org.shu.mail.EmailSender;
import org.shu.main.bean.CommonStock;
import org.shu.main.service.MultiDayDownService;
import org.shu.util.PropertyUtil;

public class FiveDaysDownWithFilterBatch {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmailSender sender = new EmailSender();
		MultiDayDownService service = new MultiDayDownService();
		List<CommonStock> stocks = service.getStocksGoingDown(5, true);
		StringBuilder content = new StringBuilder();
		for(CommonStock s : stocks){
			content.append(s.getStockSymbol() + "\n\r");
		}
	
		sender.sendEmail(PropertyUtil.getEmail(), "Five Days Down and within 40 percent of 52 week low", content.toString());	
	}
	

}
