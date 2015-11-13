package com.investmentstudios.stock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Backtest_OP_IdealSetup extends Backtest_Code_Statistics {

	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		displayTime();
		System.out.println();
		setInvestmentDirectory();
		
		BacktestDirectory = $INVEST + "/backtest/op_idealsetup/";
		BacktestDB = $INVEST + "/backtest/op_idealsetup/testdata/";
		BacktestSignalsDirectory = $INVEST + "/backtest/op_idealsetup/signals/";
		BacktestResultsDirectory = $INVEST + "/backtest/op_idealsetup/results/";
		
		mytest = new Strategy_ETFTT();
		
		runScreen("backtestIdealSetup001");
//		runScreen("backtestIdealSetup002");
		
		runScreenResults("backtestPDBuyExit001");
		runScreenResults("backtestPDBuyExit002");
		runScreenResults("backtestPDBuyExit003");
		runScreenResults("backtestPDBuyExit004");
/*		
		runScreenResults("backtestIdealSetupExit001");
		runScreenResults("backtestIdealSetupExit002");
		runScreenResults("backtestIdealSetupExit003");
		runScreenResults("backtestIdealSetupExit004");
*/		
		buildStatistics(BacktestResultsDirectory);
		
		System.out.println();
		displayTime();
	}

}
