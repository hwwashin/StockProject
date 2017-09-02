package com.investmentstudios.stock;

import java.io.IOException;

public class EOD_DailyStockScreener extends EOD_Code {
	
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
		setDayInfo(args, "12", "05", "2016", "Thursday");  // Day  Month  Year  DayOWeek
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
		
//		runEODScreens();
		for (int i=0;i<eodcount;i++) {
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
    		
			screenposition++;
//			System.out.println(screenposition);
			
//			System.out.print("stock --> " + eodstockname[i]+ "   ");
			percentChange[i] = findPercentChange(screenposition);
			pdBuyArray[i] = isPDBuy001(screenposition);
			idealSetupArray[i] = isIdealSetup(screenposition);
			formerDarlingArray[i] = isFormerDarling(screenposition);
			demiseArray[i] = isDemiseSunrise(screenposition);
//			IPOArray[i] = isIPO(screenposition);
			IPOIdealSetupArray[i] = isIPOIdealSetup(screenposition);
			FBPBreakoutBuy[i] = is5BPBreakoutBuy(screenposition);
			delphicPhenomenonBuy[i] = isDelphicPhenomenonBuy(screenposition);
			delphicPhenomenonSell[i] = isDelphicPhenomenonSell(screenposition);
			maConvergenceBuy[i] = isMAConvergenceBuy(screenposition);
			maConvergenceSell[i] = isMAConvergenceSell(screenposition);
			systemFailureBuy[i] = isSystemFailureBuy(screenposition);
			systemFailureSell[i] = isSystemFailureSell(screenposition);
			
			eodscreened[i] = eodstockoutput[i] + "," + percentChange[i] + "," + pdBuyArray[i] + "," + idealSetupArray[i] + "," + formerDarlingArray[i] + "," + demiseArray[i] + "," + IPOArray[i] + "," + IPOIdealSetupArray[i] + "," + FBPBreakoutBuy[i] + "," + delphicPhenomenonBuy[i] + "," + delphicPhenomenonSell[i] + "," + maConvergenceBuy[i] + "," + maConvergenceSell[i] + "," + systemFailureBuy[i] + "," + systemFailureSell[i];			

//			Stocks.updateStockFile(eodscreened[i], titlestring, eodstockname[i]);
		}
		
		titlestring = "Date,Open,High,Low,Close,Volume,AdjClose,Stock,Exchange,Dividend,Split,PercentChange,"
				+ "PDBuy,IdealSetup,FormerDarling,DemiseSunrise,IsIPO,IPOIdealSetup,FiveBarBuy,DelphicBuy,DelphicSell,"
				+ "MAConvBuy,MAConvSell,SysFailBuy,SysFailSell";
				
		saveEODScreenData(); 
//		findEODSymbolChanges();
		
		System.out.println();
		displayTime();
		
	}
	
	public static void runEODScreens() throws IOException {
		for (int i=0;i<eodcount;i++) {
			
			if(i%2000 == 0) { System.out.println();System.out.print("Screening --> " + eodstockname[i]); }
			if(i%25 == 0) { System.out.print("."); }

    		readEODStockFile(eodstockname[i], $INVEST + "/eoddailydownload", titlestring, eodstockoutput[i]);
			
    		int screenposition = getDatePosition(eoddate[i]);
    		
			percentChange[i] = findPercentChange(screenposition);
			pdBuyArray[i] = isPDBuy001(screenposition);
			idealSetupArray[i] = isIdealSetup(screenposition);
			formerDarlingArray[i] = isFormerDarling(screenposition);
			demiseArray[i] = isDemiseSunrise(screenposition);
//			IPOArray[i] = isIPO(screenposition);
			IPOIdealSetupArray[i] = isIPOIdealSetup(screenposition);
			FBPBreakoutBuy[i] = is5BPBreakoutBuy(screenposition);
			delphicPhenomenonBuy[i] = isDelphicPhenomenonBuy(screenposition);
			delphicPhenomenonSell[i] = isDelphicPhenomenonSell(screenposition);
			maConvergenceBuy[i] = isMAConvergenceBuy(screenposition);
			maConvergenceSell[i] = isMAConvergenceSell(screenposition);
			systemFailureBuy[i] = isSystemFailureBuy(screenposition);
			systemFailureSell[i] = isSystemFailureSell(screenposition);
			
			eodscreened[i] = eodstockoutput[i] + "," + "0" + "," + "1" + "," + percentChange[i] + "," + pdBuyArray[i] + "," + idealSetupArray[i] + "," + formerDarlingArray[i] + "," + demiseArray[i] + "," + IPOArray[i] + "," + IPOIdealSetupArray[i] + "," + FBPBreakoutBuy[i] + "," + delphicPhenomenonBuy[i] + "," + delphicPhenomenonSell[i] + "," + maConvergenceBuy[i] + "," + maConvergenceSell[i] + "," + systemFailureBuy[i] + "," + systemFailureSell[i];			

//			Stocks.updateStockFile(eodscreened[i], titlestring, eodstockname[i]);
		}
	}
}
