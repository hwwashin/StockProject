package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EOD_DailyStockUpdateInitialBuild extends EOD_Code {

	public static int $DATECOUNT = 50000;
	
	public static String $EODDATELIST = "E:/Investments/eoddatelisting.csv";
	
	public static int eoddatecount;
	
	public static String[] dayarray = new String[$DATECOUNT];
	public static String[] montharray = new String[$DATECOUNT];
	public static String[] yeararray = new String[$DATECOUNT];
	public static String[] dayoweekarray = new String[$DATECOUNT];
	
	public EOD_DailyStockUpdateInitialBuild() { }

	public static void main(String[] args) throws IOException {
		displayTime();
		System.out.println();
		
		loadDateData();
		formatDateData();
		
//		for(int i=0;i<eoddatecount;i++) {
		for(int i=1088;i<eoddatecount;i++) {
		
			setInvestmentDirectory();
			System.out.println("The data about to be processed is for the date --> " + montharray[i] + "/" + dayarray[i] + "/" + yeararray[i] + " --> " + dayoweekarray[i]);
			setDayInfo(args, dayarray[i], montharray[i], yeararray[i], dayoweekarray[i]);  // Day  Month  Year  DayOWeek
			setEODDirectories();
			
			eodcount = 0;
//			System.out.println(amexPath);
			loadData(amexPath, "amex"); /* populate the individual arrays here - including the exchange */
			loadData(nysePath, "nyse");
			loadData(nasdaqPath, "nasdaq");
	//		LoadData(indexPath, "index");
	//		LoadData(otcbbPath, "otcbb");
			
			sortEODScreenData(eodcount, eodstockinput);
			
			buildEODScreenData();
			
			for (int j=0;j<eodcount;j++) {
	//			if(j%100==0) System.out.println(eodstockoutput[j]);
			}
			
			System.out.print("Finished loading sorting & building, now updating stock DB");
			
			titlestring="Date,Open,High,Low,Close,Volume,AdjClose,Stock,Exchange,Div,Split";
			updateEODStockData();
			System.out.println();
		}
		
		System.out.println();
		displayTime();
	}
	
	public static void loadDateData() throws IOException {
		
		BufferedReader stockFile = new BufferedReader(new FileReader($EODDATELIST));
		eoddatecount = 0;
		String datarow = stockFile.readLine();
//		datarow = stockFile.readLine();
		while (datarow != null && eoddatecount < 25000) {
//			System.out.println(datarow);
			String tempdatearray[] = datarow.split(",");
			dayarray[eoddatecount] = tempdatearray[1];
			montharray[eoddatecount] = tempdatearray[2];
			yeararray[eoddatecount] = tempdatearray[3];
			dayoweekarray[eoddatecount] = tempdatearray[4];
			eoddatecount++;
			datarow = stockFile.readLine();
		}
		stockFile.close();
	}
	
	public static void formatDateData() {
		for(int i=0;i<eoddatecount;i++) {
			if(dayarray[i].length() == 1) {
				dayarray[i] = "0" + dayarray[i];
			}
			if(montharray[i].length() == 1) {
				montharray[i] = "0" + montharray[i];
			}
		}
	}

}
