package com.investmentstudios.stock;

import java.io.IOException;

public class EOD_DailyStockUpdate extends EOD_Code {

	public EOD_DailyStockUpdate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException {
		displayTime();
		
		setInvestmentDirectory();
		setDayInfo(args, "31", "08", "2015", "Monday");  // Day  Month  Year  DayOWeek
		setEODDirectories();
		
		loadData(amexPath, "amex"); /* populate the individual arrays here - including the exchange */
		loadData(nysePath, "nyse");
		loadData(nasdaqPath, "nasdaq");
//		LoadData(indexPath, "index");
//		LoadData(otcbbPath, "otcbb");
		
		sortEODScreenData(eodcount, eodstockinput);
		
		buildEODScreenData();
		
		System.out.print("Finished loading sorting & building, now updating stock DB");
		
		updateEODStockData();	
		
		displayTime();
	}

}
