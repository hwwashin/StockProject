package com.investmentstudios.stock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Backtest_ETFTT_PDBuy extends Backtest_Code_Statistics {

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		displayTime();
		System.out.println();
		setInvestmentDirectory();
		
		BacktestDirectory = $INVEST + "/backtest/etftt_pdbuy/";
		BacktestDB = $INVEST + "/backtest/etftt_pdbuy/testdata/";
		BacktestSignalsDirectory = $INVEST + "/backtest/etftt_pdbuy/signals/";
		BacktestResultsDirectory = $INVEST + "/backtest/etftt_pdbuy/results/";
		
		mytest = new Strategy_ETFTT();

		runScreen("backtestPDBuy001");
		runScreen("backtestPDBuy002");
		runScreen("backtestPDBuy003");

/*
		public static String findPDExit(int signalloc, int profittargetstrategy, double greenpercentrisk, double graypercentrisk, 
		double redpercentrisk, int trailingstopstrategy) {

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
		runScreenResults("backtestPDBuyExit", 21, 0.010, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.005, 0.005,  1);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.005, 0.005,  3);
		runScreenResults("backtestPDBuyExit",  3, 0.010, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.005, 0.005,  4);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.005, 0.005,  5);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.005, 0.005,  2);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000,  2);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000,  4);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000,  5);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.020, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.020, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.040, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.050, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.060, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.070, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.080, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.090, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.100, 0.000, 0.000,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.000, 0.000, 0.005,  3);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000, 11);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000,  6);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000,  8);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000, 10);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000, 16);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000, 18);
		runScreenResults("backtestPDBuyExit",  0, 0.030, 0.000, 0.000, 20);
		runScreenResults("backtestPDBuyExit",  3, 0.030, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit",  2, 0.030, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit",  1, 0.030, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit", 11, 0.030, 0.005, 0.005,  0);
		runScreenResults("backtestPDBuyExit",  0, 0.020, 0.000, 0.000, 18);
		runScreenResults("backtestPDBuyExit",  0, 0.010, 0.000, 0.000, 18);
/*		
		runScreenResults("backtestPDBuyExit001");
		runScreenResults("backtestPDBuyExit002");
		runScreenResults("backtestPDBuyExit003");
		runScreenResults("backtestPDBuyExit004");
		runScreenResults("backtestPDBuyExit005");
		runScreenResults("backtestPDBuyExit006");
		runScreenResults("backtestPDBuyExit007");
		runScreenResults("backtestPDBuyExit008");
		runScreenResults("backtestPDBuyExit009");
		runScreenResults("backtestPDBuyExit010");
		runScreenResults("backtestPDBuyExit011");
		runScreenResults("backtestPDBuyExit012");
		runScreenResults("backtestPDBuyExit013");
		runScreenResults("backtestPDBuyExit014");
		runScreenResults("backtestPDBuyExit015");
		runScreenResults("backtestPDBuyExit016");
		runScreenResults("backtestPDBuyExit017");
		runScreenResults("backtestPDBuyExit018");
		runScreenResults("backtestPDBuyExit019");
		runScreenResults("backtestPDBuyExit020");
		runScreenResults("backtestPDBuyExit021");
		runScreenResults("backtestPDBuyExit022");
		runScreenResults("backtestPDBuyExit023");
		runScreenResults("backtestPDBuyExit024");
		runScreenResults("backtestPDBuyExit025");
		runScreenResults("backtestPDBuyExit026");
		runScreenResults("backtestPDBuyExit027");
		runScreenResults("backtestPDBuyExit028");
		runScreenResults("backtestPDBuyExit029");
		runScreenResults("backtestPDBuyExit030");
		runScreenResults("backtestPDBuyExit031");
		runScreenResults("backtestPDBuyExit032");
		runScreenResults("backtestPDBuyExit033");
		runScreenResults("backtestPDBuyExit034");
		runScreenResults("backtestPDBuyExit035");
		runScreenResults("backtestPDBuyExit036");
*/
		
		
		buildStatistics(BacktestResultsDirectory);
		
		System.out.println();
		displayTime();
	}
}
