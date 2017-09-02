package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Backtest_Code extends EOD_Code {

	public static int $BTCOUNT = 500000;
	
	public static Object mytest;
	
	public static int entrycount = 0;
	public static int btstockcount = 0;
	public static int screencount = 0;
	
	public static String BacktestDB = new String();
	public static String BacktestDirectory = new String();
	public static String BacktestSignalsDirectory = new String();
	public static String BacktestResultsDirectory = new String();
	
	
	public static String[] screendata = new String[$BTCOUNT];

	public static double[] btop = new double[$BTCOUNT];
	public static double[] bthi = new double[$BTCOUNT];
	public static double[] btlo = new double[$BTCOUNT];
	public static double[] btcl = new double[$BTCOUNT];
	public static double[] btvo = new double[$BTCOUNT];
	public static double[] btac = new double[$BTCOUNT];
	
	public static String[] btdate = new String[$BTCOUNT];
	public static String[] btopen = new String[$BTCOUNT];
	public static String[] bthigh = new String[$BTCOUNT];
	public static String[] btlow = new String[$BTCOUNT];
	public static String[] btclose = new String[$BTCOUNT];
	public static String[] btvolume = new String[$BTCOUNT];
	public static String[] btadjclose = new String[$BTCOUNT];
	public static String[] btstockname = new String[$BTCOUNT];
	
	public static String[] btstockdata = new String[$BTCOUNT];
	public static String[] btresults = new String[$BTCOUNT];
	
	public static void computeExitStats(String[] exits, String[] results1, String[] results2, String[] results3, String[] results4) {
		int btcounter = $TESTNUM;		
//		int btcounter = exits.length;
		
		for(int i=0;i<btcounter;i++) {
//			System.out.println(exits[i]);
		}		
		
		computeExitStats1(btcounter, "CLOSE", "CLOSE", exits, results1);
		computeExitStats1(btcounter, "CLOSE", "OPEN", exits, results2);
		computeExitStats1(btcounter, "OPEN", "CLOSE", exits, results3);
		computeExitStats1(btcounter, "OPEN", "OPEN", exits, results4);	
		
	}
	
	public static void computeExitStats1(int count, String openpoint, String closepoint, String[] exits, String[] results) {

		String title = "Average Trade, Average Length, Total Trades, Winning Trades, % Winners, Average Winner, Avg Winner Length, Losing Trades, % Losers, Average Loser, Avg Loser Length";
//		String title = "Total Trades, Winning Trades, Max Consec. Winners, % Winners, Average Winner, Avg Winner Length, Losing Trades, Max Consec. Losers, % Losers, Average Loser, Avg Loser Length, Max Drawdown, Average Trade";
		
		String[] exitstrategy = new String[count];
		String[] exitreason = new String[count];
		int[] entryloc = new int[count];
		int[] exitloc = new int[count];
		double[] entry = new double[count];
		double[] exit = new double[count];
		
		int totaltrades = 0;
		int winningtrades = 0;
		int losingtrades = 0;
		int avgwinnerlength = 0;
		int avgloserlength = 0;
		double avgpercentgain = 0;
		double avgpercentloss = 0;
		double percentwinners = 0;
		double percentlosers = 0;
		double averagewinner = 0;
		double averageloser = 0;
		double averagetrade = 0;
		double averagelength = 0;
		
		int maxconsecwinners = 0;
		int maxconseclosers = 0;
		double maxdrawdown = 0;
		
		for(int i=0;i<count;i++) {
			String[] temp = exits[i].split(",");
			
//			System.out.println(exits[i]);
			
			exitstrategy[i] = temp[0];
			exitreason[i] = temp[1];
			entryloc[i] = Integer.parseInt(temp[2]);
			exitloc[i] = Integer.parseInt(temp[3]);
			
			if(openpoint.equals("CLOSE")) {
				entry[i] = cl[entryloc[i]];
			}
			else {
				entry[i] = op[entryloc[i]-1];
			}
			
			if(closepoint.equals("Close")) {
				exit[i] = cl[exitloc[i]];
			}
			else {
				exit[i] = op[exitloc[i]-1];
			}
		}
		
		totaltrades = count;
		
		for(int i=0;i<count;i++) {

			if(entry[i] < exit[i]) {
				winningtrades++;
				avgwinnerlength += entryloc[i] - exitloc[i];
				averagewinner += exit[i] - entry[i];
				avgpercentgain += (exit[i] - entry[i]) / entry[i];
			}
			else {
				losingtrades++;
				avgloserlength += entryloc[i] - exitloc[i];
				averageloser += entry[i] - exit[i];
				avgpercentloss += (entry[i] - exit[i]) / entry[i];
			}
		}
		
		averagetrade = (averagewinner - averageloser) / count;
		averagelength = (avgwinnerlength + avgloserlength) / count;
		
		avgwinnerlength /= winningtrades;
		averagewinner /= winningtrades;
		avgpercentgain /= winningtrades;
		
		avgloserlength /= losingtrades;
		averageloser /= losingtrades;
		avgpercentloss /= losingtrades;
		
		percentwinners = winningtrades / count;
		percentlosers = losingtrades / count;
		
		results[0] = exitstrategy[0] + "-" + openpoint + "-" + closepoint;
		results[1] = "";
		results[2] = title;
		results[3] = count + "," + averagetrade + "," + averagelength + "," + winningtrades + "," + percentwinners + "," + averagewinner + "," + avgwinnerlength + "," + losingtrades + "," + percentlosers + "," + averageloser + "," + avgloserlength;
		results[4] = "";
		
		for(int i=0;i<count;i++) {
			results[i+5] = exitstrategy[i] + "," + exitreason[i] + "," + stockdata[entryloc[i]] + "," + stockdata[entryloc[i]-1] + "," + stockdata[exitloc[i]] + "," + stockdata[exitloc[i]-1];
//			System.out.println("output" + " - " + exit[i] + " - " + results[i]);
		}
	}
	
	public static int findBTPosition(int pos) {
		
		int returnval = -1;
		for(int i=0;i<stockcount;i++) {
			if(btdate[pos].equals(date[i])) {
				returnval = i;
				i=stockcount;
			}
		}
		return returnval;
	}
	
	public static void readBacktestFile(String dir, String fname) throws IOException {
//		System.out.println(dir);
//		System.out.println(fname);
		BufferedReader datafile = new BufferedReader(new FileReader(dir));
		stockfilename = fname;
		
		btstockcount = 0;
		btstockdata[btstockcount] = datafile.readLine();
		String datarow = datafile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount + " --> " + stockFileName + " --> " + datarow);
			btstockdata[btstockcount] = datarow;
			setBTStockData();
			btstockcount++;
			datarow = datafile.readLine();
		}	
		datafile.close();
//		System.out.println("Data Downloaded For " + stockFileName);
	}
	
	public static void runScreen(String methodname) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stockDBDirectory = new File(BacktestDB);
		System.out.println(stockDBDirectory);
		stocknamearray = stockDBDirectory.listFiles();
		screencount = 0;
		for (int i = 0; i < stocknamearray.length; i++) {
			getStockName(stockDBDirectory, stocknamearray[i]);
//			System.out.println(stockfilename + " --> " + stockDBDirectory);
//			if(i%1000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
//			if(i%25==0) System.out.print(".");
			readStockFile(BacktestDB + stockfilename, stockfilename);
			calculateETFTrend();
			for(int j=0;j<stockcount;j++) {
		        Class[] cArg = new Class[1];
		        cArg[0] = int.class;
				Method method = Backtest_Code_Screens.class.getDeclaredMethod(methodname, cArg);
				if((boolean) method.invoke(mytest, j)) {
					screendata[screencount] = stockdata[j] + "," + stockfilename;
//					System.out.println(screendata[screencount]);
					screencount++;
				}
			}
		}
		writeDataToFile(screencount, screendata, BacktestSignalsDirectory + "etftt_pdbuy_" + methodname + ".csv");
	}
	
	public static void runScreenResults(String methodname) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		File EntrySignalsDirectory = new File(BacktestSignalsDirectory);
		File SignalResultsDirectory = new File(BacktestResultsDirectory);
		File[] signalsnamearray = EntrySignalsDirectory.listFiles();	
		
		for (int a = 0; a < signalsnamearray.length; a++) {
		
			getStockName(EntrySignalsDirectory, signalsnamearray[a]);
			BufferedReader dataFile = new BufferedReader(new FileReader(signalsnamearray[a]));
			
			btstockcount = 0;
			String titleline = dataFile.readLine();
			String datarow = dataFile.readLine();
			while (datarow != null) {
				btstockdata[btstockcount] = datarow;
				setBTStockData();
				btstockcount++;
				datarow = dataFile.readLine();
			}	
			dataFile.close();
			String prevstockname = "NOOOOOO";
			String currstockname;
				
			for(int i=0;i<btstockcount;i++) {
				
				currstockname = btstockname[i];
				
				if(currstockname.length() < 5 || !currstockname.substring(currstockname.length() - 4).equals(".csv")) {
					currstockname = currstockname + ".csv";
					btstockname[i] = btstockname[i] + ".csv";
				}
				
				if(!(prevstockname.equals(currstockname))) {
//					System.out.println(stockDBDirectory);
					dataFile = new BufferedReader(new FileReader(stockDBDirectory + "/" + currstockname));
					
					stockcount = 0;
					stockdata[stockcount] = dataFile.readLine();
					datarow = dataFile.readLine();
					while (datarow != null) {
			//			System.out.println(Backtest.stockcount + " --> " + Backtest.stockfilename + " --> " + datarow);
						stockdata[stockcount] = datarow;
						setStockData();
						stockcount++;
						datarow = dataFile.readLine();
					}	
					dataFile.close();
				}
			
				int signalloc = 0;
				while(!(date[signalloc].equals(btdate[i]))) {
					signalloc++;
				}
				
		        Class[] cArg = new Class[1];
		        cArg[0] = int.class;
				Method method = Backtest_Code_Screens.class.getDeclaredMethod(methodname, cArg);
				
				btresults[i] = (String) method.invoke(mytest, signalloc);
				
	//			System.out.println(signalloc + "," + stockdata[signalloc]);
	//			System.out.println(Backtest.btstockdata[i]);
				
				prevstockname = currstockname;
			}
			
			titleline = "Stock, Signal Date,Entry Date,Exit Date,Exit Reason,# Bars,Trend,% Risk,Entry Price,Intial Stop,Stop Price,Target Price,Exit Price";

			String tempstockname = stockfilename.substring(0, stockfilename.length() - 4);
			String outputfilename = SignalResultsDirectory + "\\" + tempstockname + "_" + methodname + ".csv";
			System.out.println(outputfilename);
			
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
			outputFile.write(titleline);
			outputFile.newLine();
		   	for(int i=0;i<btstockcount;i++) {
		   		outputFile.write(btstockname[i] + ",");
		   		outputFile.write(btresults[i]);
				outputFile.newLine();
		   	}
			outputFile.close();
		}
	}
	
	// int profittargetstrategy, double greenpercentrisk, double graypercentrisk, double redpercentrisk, int trailingstopstrategy
	
	public static void runScreenResults(String methodname, String parameterlist) 
			throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		File EntrySignalsDirectory = new File(BacktestSignalsDirectory);
		File SignalResultsDirectory = new File(BacktestResultsDirectory);
		File[] signalsnamearray = EntrySignalsDirectory.listFiles();	
		
		for (int a = 0; a < signalsnamearray.length; a++) {
		
			getStockName(EntrySignalsDirectory, signalsnamearray[a]);
			BufferedReader dataFile = new BufferedReader(new FileReader(signalsnamearray[a]));
			
			btstockcount = 0;
			String titleline = dataFile.readLine();
			String datarow = dataFile.readLine();
			while (datarow != null) {
				btstockdata[btstockcount] = datarow;
				setBTStockData();
				btstockcount++;
				datarow = dataFile.readLine();
			}	
			dataFile.close();
			String prevstockname = "NOOOOOO";
			String currstockname;
/*				
			for(int i=0;i<btstockcount;i++) {
//				System.out.println(btstockdata[i]);
				System.out.println(btstockname[i]);
			}
*/			
			for(int i=0;i<btstockcount;i++) {
				
				currstockname = btstockname[i];
				
				if(!(prevstockname.equals(currstockname))) {
//					System.out.println(stockDBDirectory);
//					System.out.println(currstockname);
					
					dataFile = new BufferedReader(new FileReader(stockDBDirectory + "/" + currstockname));
					
					stockcount = 0;
					stockdata[stockcount] = dataFile.readLine();
					datarow = dataFile.readLine();
					while (datarow != null) {
//						System.out.println(btstockname[i] + " --> " + stockcount + " --> " + stockfilename + " --> " + datarow);
						stockdata[stockcount] = datarow;
						setStockData();
						stockcount++;
						datarow = dataFile.readLine();
					}	
					dataFile.close();
				}
			
				int signalloc = 5;
				while(!(date[signalloc].equals(btdate[i])) && signalloc < stockcount) {
//					System.out.println(signalloc + " --> " + date[signalloc] + " --> " + btdate[i]);
					signalloc++;
				}
				
				if(signalloc >= stockcount) {
					signalloc = 0;
				}
//				System.out.println(btstockdata[i]);
				
		        Class[] cArg = new Class[2];
		        cArg[0] = int.class;
		        cArg[1] = String.class;
				Method method = Backtest_Code_Screens.class.getDeclaredMethod(methodname, cArg);
				btresults[i] = (String) method.invoke(mytest, signalloc, parameterlist);
				
//	System.out.println(signalloc + "," + stockdata[signalloc]);
//	System.out.println(btresults[i]);
				
				prevstockname = currstockname;
			}
			
			titleline = "Stock, Signal Date,Entry Date,Exit Date,Exit Reason,# Bars,Trend,% Risk,Entry Price,Intial Stop,Stop Price,Target Price,Exit Price,Max Drawdown";

			String tempstockname = stockfilename.substring(0, stockfilename.length() - 4);
			String testname = parameterlist.replaceAll(",", "-");;
			String outputfilename = SignalResultsDirectory + "\\" + tempstockname + "_" + methodname + "-" + testname + ".csv";
			System.out.println(outputfilename);
			
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
			outputFile.write(titleline);
			outputFile.newLine();
		   	for(int i=0;i<btstockcount;i++) {
		   		outputFile.write(btstockname[i] + ",");
		   		outputFile.write(btresults[i]);
				outputFile.newLine();
		   	}
			outputFile.close();
		}
	}
	
	public static void setBTStockData() {
		String[] temp = btstockdata[btstockcount].split(",");
//		System.out.println(btstockdata[btstockcount]);
		
		btdate[btstockcount] = convertDate(temp[0]);
		
		btopen[btstockcount] = temp[1];
		bthigh[btstockcount] = temp[2];
		btlow[btstockcount] = temp[3];
		btclose[btstockcount] = temp[4];
		btvolume[btstockcount] = temp[5];
		
		btop[btstockcount] = Double.parseDouble(temp[1]);
		bthi[btstockcount] = Double.parseDouble(temp[2]);
		btlo[btstockcount] = Double.parseDouble(temp[3]);
		btcl[btstockcount] = Double.parseDouble(temp[4]);
		btvo[btstockcount] = Double.parseDouble(temp[5]);
		
		if(temp.length == 8) { 
//			btadjclose[btstockcount] = temp[6];
			btstockname[btstockcount] = temp[7];
		}
		else if(temp.length == 10) {
			btstockname[btstockcount] = temp[9];			
		}
		
//		btac[btstockcount] = Double.parseDouble(temp[6]);
	}
	
	public static void writeBTResults(String[] results) throws IOException {
		
		String outputFileName = "C:/Users/Willis/Documents/Investments/tempfile.csv";
		File oldFile = new File(outputFileName);
		oldFile.delete();

		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
		int i=0;
		while(results[i] != null) {
//	   		System.out.println(i + "  -  " + results[i]);
	   	    outputFile.write(results[i]);
//	   		outputFile.write(dailyData2[i]);
	   	   	outputFile.newLine();
	   	   	i++;
	   	}
		outputFile.close();

		File sourceFile = new File("C:/Users/Willis/Documents/Investments/tempfile.csv");
		File targetFile = new File($INVEST + results[0]);
		targetFile.delete();
		sourceFile.renameTo(targetFile);
	}
	
	public static void writeStockDataWithHeader(String[] results, String sname, int newcount) throws IOException {
		String outputfilename = "C:/Users/Willis/Documents/Investments/tempfile.csv";
		File oldfile = new File(outputfilename);
		oldfile.delete();

		BufferedWriter outputfile = new BufferedWriter(new FileWriter(outputfilename));
		outputfile.write(stockfileheader);
		outputfile.newLine();
		for(int i=0;i<newcount;i++) {
	   		System.out.println(i + "  -  " + results[i]);
	   	    outputfile.write(results[i]);
//	   		outputfile.write(dailyData2[i]);
	   	   	outputfile.newLine();
	   	}
		outputfile.close();

		File sourceFile = new File("C:/Users/Willis/Documents/Investments/tempfile.csv");
		File targetFile = new File($INVEST + "eoddatabase/" + sname);
		targetFile.delete();
		sourceFile.renameTo(targetFile);
	}
	
	
}
