package com.investmentstudios.stock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Backtest_ETFTT_PDBuy extends Backtest_Code_Statistics {

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		boolean writetofile = false;
		boolean fullbacktest = false;
		
		displayTime();
		System.out.println();
		setInvestmentDirectory();
		
		BacktestDirectory = $INVEST + "/backtest/etftt_pdbuy/";
		BacktestSignalsDirectory = $INVEST + "/backtest/etftt_pdbuy/signals/";
		BacktestResultsDirectory = $INVEST + "/backtest/etftt_pdbuy/results/";
		
		if(fullbacktest) {
			BacktestDB = $INVEST + "/eoddatabase";
		}
		else {
			BacktestDB = $INVEST + "/backtest/etftt_pdbuy/testdata/";
		}
		
		mytest = new Strategy_ETFTT();

		if(writetofile) {
			File file = new File($INVEST + "/BIG_BACKTEST.csv");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);
		}

		
// 		isPDBuyMain(candlesize, bodysize, lowerlows, percentcloselevel, below3mas, 
//					divergencevalue1, divergencevalue2, channellocation, rrrvalue)
		
		runPDScreen("isPDBuyMain", "0,0,2,50,1,40,0,0,0.5");
		runPDScreen("isPDBuyMain", "0,0,2,50,1,15,0,0,0.5");
		runPDScreen("isPDBuyMain", "0,0,2,50,0,15,0,0,0.5");

/*
		public static String findPDExit(signalloc, profittargetstrategy, greenpercentrisk, graypercentrisk, 
		redpercentrisk, trailingstopstrategy) {

		if(profittargetstrategy == 0) profittarget = 0;
		else if(profittargetstrategy == 1) profittarget = entrytarget * 1.01;
		else if(profittargetstrategy == 2) profittarget = entrytarget * 1.02;
		else if(profittargetstrategy == 3) profittarget = entrytarget * 1.03;
		else if(profittargetstrategy == 4) profittarget = entrytarget * 1.04;
		else if(profittargetstrategy == 5) profittarget = entrytarget * 1.05;
		else if(profittargetstrategy == 6) profittarget = entrytarget * 1.06;
		else if(profittargetstrategy == 7) profittarget = entrytarget * 1.07;
		else if(profittargetstrategy == 8) profittarget = entrytarget * 1.08;
		else if(profittargetstrategy == 9) profittarget = entrytarget * 1.09;
		else if(profittargetstrategy == 10) profittarget = entrytarget * 1.10;
		else if(profittargetstrategy == 11) profittarget = entrytarget * 1.005;
		else if(profittargetstrategy == 12) profittarget = entrytarget * 1.015;
		else if(profittargetstrategy == 13) profittarget = entrytarget * 1.025;
		else if(profittargetstrategy == 14) profittarget = entrytarget * 1.035;
		else if(profittargetstrategy == 15) profittarget = entrytarget * 1.045;
		else if(profittargetstrategy == 16) profittarget = entrytarget * 1.055;
		else if(profittargetstrategy == 17) profittarget = entrytarget * 1.065;
		else if(profittargetstrategy == 18) profittarget = entrytarget * 1.075;
		else if(profittargetstrategy == 19) profittarget = entrytarget * 1.085;
		else if(profittargetstrategy == 20) profittarget = entrytarget * 1.095;
		else if(profittargetstrategy == 21) profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);

			if(trailingstopstrategy == 0) stoptarget = stoptarget;
			else if (trailingstopstrategy == 1) stoptarget = lo[loc+1] - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 2) stoptarget = twoDayLow(loc+1) - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 3) stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 4) stoptarget = fourDayLow(loc+1) - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 5) stoptarget = fiveDayLow(loc+1) - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 6) stoptarget = lo[loc+1] - tenDayATR(loc+1) - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 7) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.25 - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 8) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.5 - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 9) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.75 - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 10) stoptarget = lo[loc+1] - tenDayATR(loc+1)*2.0 - determineAFC(lo[loc+1]);
			else if (trailingstopstrategy == 11) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 12) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.25) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 13) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.5) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 14) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.75) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 15) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*2.0) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 16) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1) - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 17) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.25 - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 18) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5 - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 19) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.75 - determineAFC(lo[loc+1]));
			else if (trailingstopstrategy == 20) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*2.0 - determineAFC(lo[loc+1]));
		
		
		public static String findPDExit(int signalloc, int profittargetstrategy, double greenpercentrisk, double graypercentrisk, 
		double redpercentrisk, int trailingstopstrategy) {
*/		

		
		/*
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.015,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.025,0.000,0.000,16");
	`	*/
		
		
		
		/*
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,18");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,18");
		runScreenResults("backtestPDBuyExit", "21,0.010,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.005,0.005,1");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.005,0.005,3");
		runScreenResults("backtestPDBuyExit", "3,0.010,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.005,0.005,4");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.005,0.005,5");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.005,0.005,2");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,2");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,4");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,5");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.040,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.050,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.060,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.070,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.080,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.090,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.100,0.000,0.000,3");
		runScreenResults("backtestPDBuyExit", "0,0.000,0.000,0.005,3");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,11");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,6");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,8");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,10");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,16");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,18");
		runScreenResults("backtestPDBuyExit", "0,0.030,0.000,0.000,20");
		runScreenResults("backtestPDBuyExit", "3,0.030,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "2,0.030,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "1,0.030,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "11,0.030,0.005,0.005,0");
		runScreenResults("backtestPDBuyExit", "0,0.020,0.000,0.000,18");
		runScreenResults("backtestPDBuyExit", "0,0.010,0.000,0.000,18");
*/
		
		buildStatistics(BacktestResultsDirectory);
		
		if(writetofile) {
			PrintStream console = System.out;
			System.setOut(console);
		}
		
		System.out.println();
		displayTime();
	}
	
	public static void runPDScreen(String methodname, String ParameterString) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stockDBDirectory = new File(BacktestDB);
		System.out.println(stockDBDirectory);
		stocknamearray = stockDBDirectory.listFiles();
		screencount = 0;
		for (int i = 0; i < stocknamearray.length; i++) {
			getStockName(stockDBDirectory, stocknamearray[i]);
	//		System.out.println(stockfilename + " --> " + stockDBDirectory);
	//		if(i%1000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
	//		if(i%25==0) System.out.print(".");
			readStockFile(BacktestDB + stockfilename, stockfilename);
			calculateETFTrend();
			for(int j=0;j<stockcount;j++) {
		        Class[] cArg = new Class[2];
		        cArg[0] = int.class;
		        cArg[1] = String.class;
				Method method = Strategy_ETFTT.class.getDeclaredMethod(methodname, cArg);
				if((boolean) method.invoke(mytest, j, ParameterString)) {
					screendata[screencount] = stockdata[j] + "," + stockfilename;
	//				System.out.println(screendata[screencount]);
					screencount++;
				}
			}
		}
		String parameterstring2 = ParameterString.replaceAll(",", "-");
		writeDataToFile(screencount, screendata, BacktestSignalsDirectory + "etftt_pdbuy-" + methodname + "-" + parameterstring2 + ".csv");
	}
}