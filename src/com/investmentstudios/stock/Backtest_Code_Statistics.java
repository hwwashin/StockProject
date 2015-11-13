package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Backtest_Code_Statistics extends Backtest_Code_ExitCriteria {
	
	public static int $ACCOUNTSTART = 10000;
	public static double $ROUNDTRIPCOST = 20.0;
	
	public static String resultsstatsheader = new String();
	public static String[] SignalStatistics = new String[$COUNT];
	public static String[] stocknamestats = new String[$COUNT];
	
	public static int[] length = new int[$BTCOUNT];
	public static double[] trend = new double[$BTCOUNT];
	public static double[] percentrisk = new double[$BTCOUNT];
	public static double[] entryprice = new double[$BTCOUNT];
	public static double[] targetprice = new double[$BTCOUNT];
	public static double[] initialstopprice = new double[$BTCOUNT];
	public static double[] stopprice = new double[$BTCOUNT];
	public static double[] exitprice = new double[$BTCOUNT];
	public static double[] maxdrawdownarray = new double[$BTCOUNT];
	
	public static double totalreturns;
	public static double[] shares = new double[$BTCOUNT];
	public static double[] profit = new double[$BTCOUNT];
	public static double[] startval = new double[$BTCOUNT];
	public static double[] endval = new double[$BTCOUNT];
	
	public static String[] signaldate = new String[$BTCOUNT];
	public static String[] entrydate = new String[$BTCOUNT];
	public static String[] exitdate = new String[$BTCOUNT];
	public static String[] exitreason = new String[$BTCOUNT];
	public static String[] lengthstring = new String[$BTCOUNT];
	public static String[] trendstring = new String[$BTCOUNT];
	public static String[] percentriskstring = new String[$BTCOUNT];
	public static String[] entrypricestring = new String[$BTCOUNT];
	public static String[] targetpricestring = new String[$BTCOUNT];
	public static String[] initialstoppricestring = new String[$BTCOUNT];
	public static String[] stoppricestring = new String[$BTCOUNT];
	public static String[] exitpricestring = new String[$BTCOUNT];
	public static String[] maxdrawdownstring = new String[$BTCOUNT];
	
	public static double totalnetprofit = 0;
	public static double netprofit = 0;
	public static double grossprofit = 0;
	public static double grossloss = 0;
	public static int totalnumberoftrades = 0;
	public static double percentprofitable = 0;
	public static int winningtrades = 0;
	public static int losingtrades = 0;
	public static int falsesignals = 0;
	public static double falsesignalpercent = 0;
	public static double avgtradeprofit = 0;
	public static double avgwinningtrade = 0;
	public static double avglosingtrade = 0;
	public static double wintolossratio = 0;
	public static double largestwinningtrade = 0;
	public static double largestlosingtrade = 0;
	public static double largestwinnerpercentgrossprofit = 0;
	public static double largestloserpercentgrossprofit = 0;
	public static int maxconsecwinningtrades = 0;
	public static int maxconseclosingtrades = 0;
	public static int avgbarsintotaltrades = 0;
	public static int avgbarsinwinningtrades = 0;
	public static int avgbarsinlosingtrades = 0; 
	public static int maxsharesheld = 0;
	public static int totalsharesheld = 0;
	public static double returnoninitialcapital = 0;
	public static double largestconsecprofit = 0;
	public static double largestconsecloss = 0;
	public static double maxdrawdown = 0;
	
	public static void buildStatistics(String backtestresultsdir) throws IOException {

		File SignalResultsDirectory = new File(backtestresultsdir);
		File[] signalsnamearray = SignalResultsDirectory.listFiles();	
		
		for (int i = 0; i < signalsnamearray.length; i++) {
			resetStatistics();
			getStockName(SignalResultsDirectory, signalsnamearray[i]);
			BufferedReader datafile = new BufferedReader(new FileReader(signalsnamearray[i]));
			
			btresults[0]=datafile.readLine();
			entrycount = 0;
			String datarow = datafile.readLine();
			while (datarow != null) {
	//			System.out.println(Backtest.stockcount + " --> " + Backtest.stockfilename + " --> " + datarow);
				btresults[entrycount] = datarow;
				entrycount++;
				datarow = datafile.readLine();
			}	
			datafile.close();
			
			StrategyName[i] = stockfilename;
			
			createResultsVariables();
			calculateReturns();
			calculateStats();
			formatStats();
			captureStats(i);
		}
		
		saveStats(signalsnamearray.length);
	}
	
	public static void calculateReturns() {
		startval[entrycount-1] = $ACCOUNTSTART;
		for(int i=entrycount-1;i>=0;i--) {
			
//			System.out.println(i + "   %risk ->" + percentrisk[i] + "   startval->" +  startval[i] + "   entryprice->" + entryprice[i] + "  stopprice->" + stopprice[i]);
			
			if(length[i] == 0) {
				shares[i] = 0;
				profit[i] = 0;
				endval[i] = startval[i];
			}
			else {
// System.out.println(btresults[i]);
// System.out.println(i + "   entrydate-->" + entrydate[i] + "   %risk ->" + percentrisk[i] + "   startval->" +  startval[i] + "   entryprice->" + entryprice[i] + "  initialstopprice->" + initialstopprice[i] + "  exitprice-->" + exitprice[i]);
				double shares1 = (percentrisk[i] * startval[i]) / (entryprice[i] - initialstopprice[i]);
				double shares2 = startval[i] / entryprice[i];
				shares[i] = min(shares1,shares2);
				shares[i] = Math.round(shares[i]);
// System.out.println(shares[i]);
				profit[i] = (exitprice[i] - entryprice[i]) * shares[i];
// System.out.println(profit[i]);
				endval[i] = startval[i] + profit[i];
			}
			
			if(i>0) {
				startval[i-1] = endval[i];
			}
		}
	}
	
	public static void calculateStats() {
		
		int winningbarcount = 0;
		int losingbarcount = 0;
		int profittest = 1;
		
		double tempconsecwinningprofit = 0;
		int tempconsecwinningcount = 0;
		double tempconseclosingprofit = 0;
		int tempconseclosingcount = 0;
		
		totalnetprofit = endval[0] - startval[entrycount];
		for(int i=entrycount;i>0;i--) {
			
			if(maxdrawdownarray[i] > maxdrawdown) {
				maxdrawdown = maxdrawdownarray[i];
			}
			if(profit[i] > 0) {
				grossprofit += profit[i];
				totalnumberoftrades++;
				winningtrades++;
				if(profit[i]>largestwinningtrade) largestwinningtrade = profit[i];
				winningbarcount += length[i];

				if(profittest == 1) {
					tempconsecwinningcount++;
					tempconsecwinningprofit += profit[i];
					profittest = 1;
				}
				else if(profittest == 2) {
					if (tempconsecwinningcount > maxconsecwinningtrades) {
						maxconsecwinningtrades = tempconsecwinningcount;
						largestconsecprofit = tempconsecwinningprofit;
					}
					tempconsecwinningcount = 1;
					tempconsecwinningprofit = profit[i];
					profittest = 1;
				}
			}
			
			else if(profit[i] < 0) {
				grossloss += profit[i];
				totalnumberoftrades++;
				losingtrades++;
				if(profit[i]<largestlosingtrade) largestlosingtrade = profit[i];
				losingbarcount += length[i];

				if(profittest == 2) {
					tempconseclosingcount++;
					tempconseclosingprofit += profit[i];
					profittest = 2;
				}
				else if(profittest == 1) {
					if (tempconseclosingcount > maxconseclosingtrades) {
						maxconseclosingtrades = tempconseclosingcount;
						largestconsecloss = tempconseclosingprofit;
					}
					tempconseclosingcount = 1;
					tempconseclosingprofit = profit[i];
					profittest = 2;
				}
			}
			else {
				falsesignals++;
			}
			

			
			if(shares[i] > maxsharesheld) maxsharesheld = (int) shares[i];
			totalsharesheld += shares[i];
		}
		percentprofitable = (double) winningtrades / (double) totalnumberoftrades;
		avgwinningtrade = grossprofit / (double) winningtrades;
		avglosingtrade = grossloss / (double) losingtrades;
		avgtradeprofit = (grossprofit+grossloss)/ (double) winningtrades;
		wintolossratio = (double) winningtrades / (double) losingtrades;
		largestwinnerpercentgrossprofit = largestwinningtrade / grossprofit;
		largestloserpercentgrossprofit = largestlosingtrade / grossloss;
		avgbarsintotaltrades = (winningbarcount + losingbarcount) / totalnumberoftrades;
		netprofit = totalnetprofit - ( (double) totalnumberoftrades * $ROUNDTRIPCOST);
		
// System.out.println(winningtrades);
		
		avgbarsinwinningtrades = winningbarcount / winningtrades;
		avgbarsinlosingtrades = losingbarcount / losingtrades;
		returnoninitialcapital = (grossprofit - grossloss) / totalnetprofit;
		falsesignalpercent = (double) falsesignals/((double) falsesignals + (double) winningtrades + (double) losingtrades);
	}
	
	public static void captureStats(int pos) {
		
		SignalStatistics[pos] = StrategyName[pos] + ",";
		SignalStatistics[pos] += totalnetprofit + ",";
		SignalStatistics[pos] += netprofit + ",";
		SignalStatistics[pos] += grossprofit + ",";
		SignalStatistics[pos] += grossloss + ",";
		SignalStatistics[pos] += totalnumberoftrades + ",";
		SignalStatistics[pos] += percentprofitable + ",";
		SignalStatistics[pos] += winningtrades + ",";
		SignalStatistics[pos] += losingtrades + ",";
		SignalStatistics[pos] += falsesignals + ",";
		SignalStatistics[pos] += falsesignalpercent + ",";
		SignalStatistics[pos] += avgtradeprofit + ",";
		SignalStatistics[pos] += avgwinningtrade + ",";
		SignalStatistics[pos] += avglosingtrade + ",";
		SignalStatistics[pos] += wintolossratio + ",";
		SignalStatistics[pos] += largestwinningtrade + ",";
		SignalStatistics[pos] += largestlosingtrade + ",";
		SignalStatistics[pos] += largestwinnerpercentgrossprofit + ",";
		SignalStatistics[pos] += largestloserpercentgrossprofit + ",";
		SignalStatistics[pos] += maxconsecwinningtrades + ",";
		SignalStatistics[pos] += maxconseclosingtrades + ",";
		SignalStatistics[pos] += avgbarsintotaltrades + ",";
		SignalStatistics[pos] += avgbarsinwinningtrades + ",";
		SignalStatistics[pos] += avgbarsinlosingtrades + ",";
		SignalStatistics[pos] += maxsharesheld + ",";
		SignalStatistics[pos] += totalsharesheld + ",";
		SignalStatistics[pos] += returnoninitialcapital + ","; 
		SignalStatistics[pos] += largestconsecprofit + ",";
		SignalStatistics[pos] += largestconsecloss + ",";
		SignalStatistics[pos] += maxdrawdown + ",";
	}
	
	public static void createResultsVariables() {
		for(int i=0;i<entrycount;i++) {
			String[] temp = btresults[i].split(",");
			stocknamestats[i] = temp[0];
			signaldate[i] = temp[1];
			entrydate[i] = temp[2];
			exitdate[i] = temp[3];
			exitreason[i] = temp[4];
			lengthstring[i] = temp[5];
			trendstring[i] = temp[6];
			percentriskstring[i] = temp[7];
			entrypricestring[i] = temp[8];
			initialstoppricestring[i] = temp[9];
			stoppricestring[i] = temp[10];
			targetpricestring[i] = temp[11];
			exitpricestring[i] = temp[12];
			maxdrawdownstring[i] = temp[13];
			
			length[i] = Integer.parseInt(lengthstring[i]);
			trend[i] = Double.parseDouble(trendstring[i]);
			percentrisk[i] = Double.parseDouble(percentriskstring[i]);
			entryprice[i] = Double.parseDouble(entrypricestring[i]);
			targetprice[i] = Double.parseDouble(targetpricestring[i]);
			initialstopprice[i] = Double.parseDouble(initialstoppricestring[i]);
			stopprice[i] = Double.parseDouble(stoppricestring[i]);
			exitprice[i] = Double.parseDouble(exitpricestring[i]);
			maxdrawdownarray[i] = Double.parseDouble(maxdrawdownstring[i]);
		}
	}
	
	public static void formatStats() {
		totalnetprofit = roundToTwo(totalnetprofit);
		netprofit = roundToTwo(netprofit);
		grossprofit = roundToTwo(grossprofit);
		grossloss = roundToTwo(grossloss);
		avgtradeprofit = roundToTwo(avgtradeprofit);
		avgwinningtrade = roundToTwo(avgwinningtrade);
		avglosingtrade = roundToTwo(avglosingtrade);
		wintolossratio = roundToTwo(wintolossratio);
		largestwinningtrade = roundToTwo(largestwinningtrade);
		falsesignalpercent = roundToTwo(falsesignalpercent);
		largestlosingtrade = roundToTwo(largestlosingtrade);
		largestwinnerpercentgrossprofit = roundToTwo(largestwinnerpercentgrossprofit);
		largestloserpercentgrossprofit = roundToTwo(largestloserpercentgrossprofit);
		returnoninitialcapital = roundToTwo(returnoninitialcapital);
		largestconsecloss = roundToTwo(largestconsecloss);
		maxdrawdown = roundToTwo(maxdrawdown);
	}

	public static void printStatsToFile(String StrategyName) throws IOException {
		
		String outputfilename = "C:/Users/Willis/Documents/Investments/backtest/aaa_results.csv";
		
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
		resultsstatsheader = "strategy,totalnetprofit,netprofit,grossprofit,grossloss,totalnumberoftrades,percentprofitable,"
				+ "winningtrades,losingtrades,falsesignals,falsesignalpercent,avgtradeprofit,avgwinningtrade,"
				+ "avglosingtrade,wintolossratio,largestwinningtrade,largestlosingtrade,largestwinnerpercentgrossprofit,"
				+ "largestloserpercentgrossprofit,maxconsecwinningtrades,maxconseclosingtrades,avgbarsintotaltrades,"
				+ "avgbarsinwinningtrades,avgbarsinlosingtrades,maxsharesheld,totalsharesheld,returnoninitialcapital,"
				+ "largestconsecprofit,largestconsecloss,maxdrawdown";
			
		outputFile.write(resultsstatsheader);
		outputFile.newLine();
		outputFile.write(StrategyName + ",");
		outputFile.write(totalnetprofit + "," );
		outputFile.write(netprofit + ",");
		outputFile.write(grossprofit + "," );
		outputFile.write(grossloss + "," );
		outputFile.write(totalnumberoftrades + "," );
		outputFile.write(percentprofitable + "," );
		outputFile.write(winningtrades + "," );
		outputFile.write(losingtrades + "," );
		outputFile.write(falsesignals + "," );
		outputFile.write(falsesignalpercent + "," );
		outputFile.write(avgtradeprofit + "," );
		outputFile.write(avgwinningtrade + "," );
		outputFile.write(avglosingtrade + "," );
		outputFile.write(wintolossratio + "," );
		outputFile.write(largestwinningtrade + "," );
		outputFile.write(largestlosingtrade + "," );
		outputFile.write(largestwinnerpercentgrossprofit + "," );
		outputFile.write(largestloserpercentgrossprofit + "," );
		outputFile.write(maxconsecwinningtrades + "," );
		outputFile.write(maxconseclosingtrades + "," );
		outputFile.write(avgbarsintotaltrades + "," );
		outputFile.write(avgbarsinwinningtrades + "," );
		outputFile.write(avgbarsinlosingtrades + "," );
		outputFile.write(maxsharesheld + "," );
		outputFile.write(totalsharesheld + "," );
		outputFile.write(returnoninitialcapital + "," ); 
		outputFile.write(largestconsecprofit + "," );
		outputFile.write(largestconsecloss + "," );
		outputFile.write(maxdrawdown + ",");
		
		outputFile.close();
	}

	public static void printStatsToUniqueFile() throws IOException {
		
		String outputfilename = "C:/Users/Willis/Documents/Investments/backtest/etftt_pdbuy/results/aaa_results.csv";
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
		
		outputFile.write("totalnetprofit:," + totalnetprofit + System.lineSeparator() );
		outputFile.write("netprofit:," + netprofit + System.lineSeparator() );
		outputFile.write("grossprofit:," + grossprofit + System.lineSeparator() );
		outputFile.write("grossloss:," + grossloss + System.lineSeparator() );
		outputFile.write("totalnumberoftrades:," + totalnumberoftrades + System.lineSeparator() );
		outputFile.write("percentprofitable:," + percentprofitable + System.lineSeparator() );
		outputFile.write("winningtrades:," + winningtrades + System.lineSeparator() );
		outputFile.write("losingtrades:," + losingtrades + System.lineSeparator() );
		outputFile.write("falsesignals:," + falsesignals + System.lineSeparator() );
		outputFile.write("falsesignalpercent:," + falsesignalpercent + System.lineSeparator() );
		outputFile.write("avgtradeprofit:," + avgtradeprofit + System.lineSeparator() );
		outputFile.write("avgwinningtrade:," + avgwinningtrade + System.lineSeparator() );
		outputFile.write("avglosingtrade:," + avglosingtrade + System.lineSeparator() );
		outputFile.write("wintolossratio:," + wintolossratio + System.lineSeparator() );
		outputFile.write("largestwinningtrade:," + largestwinningtrade + System.lineSeparator() );
		outputFile.write("largestlosingtrade:," + largestlosingtrade + System.lineSeparator() );
		outputFile.write("largestwinnerpercentgrossprofit:," + largestwinnerpercentgrossprofit + System.lineSeparator() );
		outputFile.write("largestloserpercentgrossprofit:," + largestloserpercentgrossprofit + System.lineSeparator() );
		outputFile.write("maxconsecwinningtrades:," + maxconsecwinningtrades + System.lineSeparator() );
		outputFile.write("maxconseclosingtrades:," + maxconseclosingtrades + System.lineSeparator() );
		outputFile.write("avgbarsintotaltrades:," + avgbarsintotaltrades + System.lineSeparator() );
		outputFile.write("avgbarsinwinningtrades:," + avgbarsinwinningtrades + System.lineSeparator() );
		outputFile.write("avgbarsinlosingtrades:," +  avgbarsinlosingtrades + System.lineSeparator() );
		outputFile.write("maxsharesheld:," + maxsharesheld + System.lineSeparator() );
		outputFile.write("totalsharesheld:," + totalsharesheld + System.lineSeparator() );
		outputFile.write("returnoninitialcapital:," + returnoninitialcapital + System.lineSeparator() ); 
		outputFile.write("largestconsecprofit:," + largestconsecprofit + System.lineSeparator() );
		outputFile.write("largestconsecloss:," + largestconsecloss + System.lineSeparator() );
		outputFile.write("maxdrawdown:," + maxdrawdown + System.lineSeparator() );
		
		outputFile.close();
	}
	
	public static void resetStatistics() {
		totalnetprofit = 0;
		netprofit = 0;
		grossprofit = 0;
		grossloss = 0;
		totalnumberoftrades = 0;
		percentprofitable = 0;
		winningtrades = 0;
		losingtrades = 0;
		falsesignals = 0;
		falsesignalpercent = 0;
		avgtradeprofit = 0;
		avgwinningtrade = 0;
		avglosingtrade = 0;
		wintolossratio = 0;
		largestwinningtrade = 0;
		largestlosingtrade = 0;
		largestwinnerpercentgrossprofit = 0;
		largestloserpercentgrossprofit = 0;
		maxconsecwinningtrades = 0;
		maxconseclosingtrades = 0;
		avgbarsintotaltrades = 0;
		avgbarsinwinningtrades = 0;
		avgbarsinlosingtrades = 0; 
		maxsharesheld = 0;
		totalsharesheld = 0;
		returnoninitialcapital = 0;
		largestconsecprofit = 0;
		largestconsecloss = 0;
		maxdrawdown = 0;
	}

	public static void saveStats(int counter) throws IOException {
		String outputfilename = BacktestDirectory + "stats.csv";
		
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
		resultsstatsheader = "strategy,totalnetprofit,netprofit,grossprofit,grossloss,totalnumberoftrades,percentprofitable,"
				+ "winningtrades,losingtrades,falsesignals,falsesignalpercent,avgtradeprofit,avgwinningtrade,"
				+ "avglosingtrade,wintolossratio,largestwinningtrade,largestlosingtrade,largestwinnerpercentgrossprofit,"
				+ "largestloserpercentgrossprofit,maxconsecwinningtrades,maxconseclosingtrades,avgbarsintotaltrades,"
				+ "avgbarsinwinningtrades,avgbarsinlosingtrades,maxsharesheld,totalsharesheld,returnoninitialcapital,"
				+ "largestconsecprofit,largestconsecloss,maxdrawdown";
		
		outputFile.write(resultsstatsheader);
		outputFile.newLine();
		
//		System.out.println(counter);
//		System.out.println(SignalStatistics[0]);
//		System.out.println(SignalStatistics[1]);
		for(int i=0;i<counter;i++) {
			outputFile.write(SignalStatistics[i]);
			outputFile.newLine();
		}	
		outputFile.close();
	}
}
