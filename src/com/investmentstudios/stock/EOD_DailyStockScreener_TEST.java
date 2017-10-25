package com.investmentstudios.stock;

import java.io.IOException;

public class EOD_DailyStockScreener_TEST extends EOD_Code {

	public static int place = 5741;
	
	public static boolean[] idealsetup = new boolean[$COUNT];
	
	public static boolean[] formerdarling = new boolean[$COUNT];
	
	public static boolean[] yearlow = new boolean[$COUNT];
	public static boolean[] yearlowopen = new boolean[$COUNT];
	public static boolean[] gapdown = new boolean[$COUNT];
	public static boolean[] hivol = new boolean[$COUNT];
	
	public static double[] percentChange = new double[$COUNT];
	public static boolean[] pdBuyArray = new boolean[$COUNT];
	public static boolean[] idealSetupArray = new boolean[$COUNT];
	public static boolean[] formerDarlingArray = new boolean[$COUNT];
	public static boolean[] demiseArray = new boolean[$COUNT];
	public static boolean[] IPOArray = new boolean[$COUNT];
	public static boolean[] IPOIdealSetupArray = new boolean[$COUNT];
	public static boolean[] FBPBreakoutBuy = new boolean[$COUNT];
	public static boolean[] delphicPhenomenonBuy = new boolean[$COUNT];
	public static boolean[] delphicPhenomenonSell = new boolean[$COUNT];
	public static boolean[] maConvergenceBuy = new boolean[$COUNT];
	public static boolean[] maConvergenceSell = new boolean[$COUNT];
	public static boolean[] systemFailureBuy = new boolean[$COUNT];
	public static boolean[] systemFailureSell = new boolean[$COUNT];

	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		setInvestmentDirectory();
		setDayInfo(args, "21", "09", "2017", "Thursday");  // !!!! DAY  MONTH  Year  DayOWeek
		setEODDirectories();
		
		loadData(amexPath, "amex"); /* populate the individual arrays here - including the exchange */
		loadData(nysePath, "nyse");
		loadData(nasdaqPath, "nasdaq");
//		LoadData(indexPath, "index");
//		LoadData(otcbbPath, "otcbb");

		sortEODScreenData(eodcount, eodstockinput);
		
		buildEODScreenData();
		
		System.out.println();
		System.out.println("EOD Count is " + eodcount);
		

//		for (int i=0;i<eodcount;i++) {
	    for (int i=place;i<place+1;i++) {
			String[] eodtemp = eodstockinput[i].split(",");
			eodstockname[i] = eodtemp[0];
			
			if(eodstockname[i].equals("PRN")) {
				eodstockname[i] = "PRNPRN";
			}
			
			if(i%2000 == 0) { System.out.println();System.out.print("Screening --> " + eodstockname[i]); }
			if(i%25 == 0) { System.out.print("."); }
//			System.out.println("Screening --> " + eodstockname[i]);
//			System.out.println("Stock Data --> " + eodstockinput[i]);
//    		readEODStockFile(eodstockname[i], $INVEST + "/eoddailydownload", titlestring, eodstockoutput[i]);
			
			readStockFile($INVEST + "/eoddatabase/" + eodstockname[i] + ".csv", eodstockname[i] + ".csv");
			int screenposition = getDatePosition(eoddate[i]);
    		
//			screenposition++;
//			System.out.println(screenposition);
			System.out.println(stockdata[0]);
//			System.out.println("INPUT --> " + eodstockoutput[i]);
//			System.out.println("Screenpos --> " + stockdata[screenposition]);
			
//			System.out.print("stock --> " + eodstockname[i]+ "   ");
			
			/*
			yearlow[i] = is52WeekLow(screenposition);
			yearlowopen[i] = is52WeekLowOpen(screenposition);
			gapdown[i] = isGapDown(screenposition);
			if(vo[screenposition] > avgVolume(20,screenposition)*2.0) hivol[i] = true;
			else hivol[i] = false;
			demiseArray[i] = isDemiseSunrise(screenposition);
			*/
			
//			formerdarling[i] = isFormerDarling(screenposition);
			
			idealsetup[i] = isIdealSetup(screenposition);

			eodscreened[i] = eodstockoutput[i] + "," + stockcount + "," + idealsetup[i];	
//			Stocks.updateStockFile(eodscreened[i], titlestring, eodstockname[i]);
		}

		titlestring = "Date,Open,High,Low,Close,Volume,AdjClose,Stock,Exchange,Dividend,Split,Stockcount,";
//		saveEODScreenData(); 
//		findEODSymbolChanges();
		
//		System.out.println();
		System.out.println(eodscreened[place]);
		displayTime();
		
	}
	

}
