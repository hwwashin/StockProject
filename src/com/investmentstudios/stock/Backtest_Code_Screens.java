package com.investmentstudios.stock;

public class Backtest_Code_Screens extends Backtest_Code {

	
//	public static boolean backtestPDBuy001(int pos) { return isPDBuy001(pos); }
//	public static boolean backtestPDBuy002(int pos) { return isPDBuy002(pos); }
//	public static boolean backtestPDBuy003(int pos) { return isPDBuy003(pos); }
	
	public static String backtestPDBuyExit(int pos, String parameterstring) { 
		return findPDExit(pos, parameterstring); 
	}
	
}

