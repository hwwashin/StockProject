package com.investmentstudios.stock;

import java.io.IOException;

public class EOD_DailyStockUpdate extends EOD_Code {

	public EOD_DailyStockUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException {
		displayTime();
		
		setInvestmentDirectory();
		setDayInfo(args, "11", "05", "2016", "Wednesday");  // Day  Month  Year  DayOWeek
		setEODDirectories();
		
		loadData(amexPath, "amex"); /* populate the individual arrays here - including the exchange */
		loadData(nysePath, "nyse");
		loadData(nasdaqPath, "nasdaq");
//		LoadData(indexPath, "index");
//		LoadData(otcbbPath, "otcbb");
		
		sortEODScreenData(eodcount, eodstockinput);
		
		buildEODScreenData();
		
		System.out.print("Finished loading sorting & building, now updating stock DB");
		
		titlestring="Date,Open,High,Low,Close,Volume,AdjClose,Stock,Exchange,Dividend,Split";
		updateEODStockData();	
		
		System.out.println();
		displayTime();
		System.out.println();
	}

}
