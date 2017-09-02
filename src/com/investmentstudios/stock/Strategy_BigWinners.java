package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Strategy_BigWinners extends Indicators_StockSignals {

	public static int $TIMETOHIT = 10;
	public static int $RETURNPERCENT = 100;
	
	public static String $DATEAPPEND = "2016.05.12";
	public static String $EODDB = "E:/Investments/eoddatabase/";
	// public static String $YAHOO = "E:/Investments/eoddatabase/";
	// public static String $YAHOOLIST = "C:/Users/Willis/Documents/Investments/YahooDB/" + $DATEAPPEND + ".yahoodownloadlist.txt";
	// public static String $YAHOODB = "C:/Users/Willis/Documents/Investments/YahooDB/" + $DATEAPPEND + ".YahooDownload/";
	public static String $BIGWINNEROUTPUTFILE = "E:/Investments/" + $DATEAPPEND + ".backtest.csv";
	public static String $BIGWINNERFINALOUTPUTFILE = "E:/Investments/" + $DATEAPPEND + ".backtestfinal.csv";
	public static int $BIGWINNERCOUNT = 500000;
	
	public static int bigwinnercount;
	public static int bigwinnerfinalcount;
	
	public static String stockfilename;
	public static String[] bigwinnerdata = new String[$BIGWINNERCOUNT];
	public static String[] bigwinnerfinaldata = new String[$BIGWINNERCOUNT];
	
	public Strategy_BigWinners() { }

	public static void main(String[] args)  throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
    	displayTime();
		
		File stockDir = new File($EODDB);
    	File[] stocknamearray = stockDir.listFiles();
    	
//    	for(int i=0;i<300;i++) {
    	for (int i = 0; i < stocknamearray.length; i++) {
    		BufferedReader datafile = new BufferedReader(new FileReader(stocknamearray[i]));
			stockfilename = stocknamearray[i].toString().substring(stockDir.toString().length()+1);		

			if(i%1000==0) { 
				System.out.println();
				if(i==0) System.out.print("   ");
				System.out.print(i + "  " + stockfilename); }
			if(i%25==0) System.out.print(".");

//			System.out.println(stockfilename);
			
			stockcount = 0;
			stockdata[stockcount] = datafile.readLine();
			String datarow = datafile.readLine();
			while (datarow != null) {
//				System.out.println(Backtest.stockcount + " --> " + Backtest.stockfilename + " --> " + datarow);
				stockdata[stockcount] = datarow;
				setStockData();
				stockcount++;
				datarow = datafile.readLine();
			}	
			datafile.close();
			
			for(int j=0;j<stockcount;j++) {
//				System.out.println("First position");
				int gain = $RETURNPERCENT;
				int timeframe = $TIMETOHIT;
				
//				if(bigwinner(j,gain,timeframe) && cl[j] > 20) {
				if(bigwinner(j,gain,timeframe) && bigwinnercount < $BIGWINNERCOUNT) {
					bigwinnerdata[bigwinnercount] = stockfilename + "," + stockdata[j] + "," + cl[(int) max(10,j-timeframe)];
//					System.out.println(stockfilename + " --> " + stockdata[j]);
//					if(screencount%100 == 0) System.out.println(screendata[screencount]);
					bigwinnercount++;
				}
				else if(bigwinnercount >= $BIGWINNERCOUNT) {
					j = stockcount;
					i = stocknamearray.length;
				}
			}
    	}
    	System.out.println();System.out.println();
	
    	bigwinnerfinaldata[0] = bigwinnerdata[0];
    	bigwinnerfinalcount = 1;
    	for(int i=bigwinnercount-1;i>1;i--) {  		
			int timediff = 0;
			System.out.println(bigwinnerdata[i]);
			String[] currbigwinner = bigwinnerdata[i].split(",");
			stockfilename = currbigwinner[0];
//			System.out.println(currbigwinner[1]);
			String date1 = convertDate(currbigwinner[1]);
			String[] prevbigwinner = bigwinnerdata[i-1].split(",");
			String stockname2 = prevbigwinner[0];
			String date0 = convertDate(prevbigwinner[1]);
//			System.out.println("Current name --> " + stockfilename + "   Previous Name --> " + stockname2);
//			System.out.println("The current winner stock data-->" + bigwinnerdata[i] + "    and the previous winner stock data-->" + bigwinnerdata[i-1]);
			if(stockname2.equals(stockfilename)) {
//				System.out.println("About to go into datediff  date0--> " + date0 + "  date1-->" + date1);
				timediff = datediff(date1, date0);
//				System.out.println("The time difference is --> " + timediff + "  for -->  " + date0 + " and " + date1);
			}
			if(timediff == 0 || timediff > 20) {
				bigwinnerfinaldata[bigwinnerfinalcount] = bigwinnerdata[i];
				bigwinnerfinalcount++;
			}
    	}			
    	
		String outputfilename = $BIGWINNEROUTPUTFILE;
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<bigwinnercount;i++) {
	   		outputFile.write(bigwinnerdata[i]);
			outputFile.newLine();
	   	}
		outputFile.close();
		
		outputfilename = $BIGWINNERFINALOUTPUTFILE;
		outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<bigwinnerfinalcount;i++) {
	   		outputFile.write(bigwinnerfinaldata[i]);
			outputFile.newLine();
	   	}
		outputFile.close();
		
    	
		System.out.println();
		System.out.println();
		displayTime();
	}
	
	public static Boolean bigwinner(int pos, double gain, int timeframe) {
		int target = pos - timeframe;
		if(target < 1) target = 1;
		
		gain = gain / 100;
		double returns = (cl[target] - cl[pos])/cl[pos];

		boolean bigwin;
		if(returns >= gain) bigwin = true;
		else bigwin = false;
		
		return bigwin;
	}
	
	public static int datediff (String prevdate, String currdate) {
		String[] tempprevdate = prevdate.split("/");
//		System.out.println("previous data-->" + prevdate);
		String prevday = tempprevdate[1];
		String prevmonth = tempprevdate[0];
		String prevyear = tempprevdate[2];

//		System.out.println("prevday-->" + prevday + "   prevmonth-->" + prevmonth + "   prevyear-->" + prevyear);
		
		int pd = Integer.parseInt(prevday);
		int pm = Integer.parseInt(prevmonth);
		int py = Integer.parseInt(prevyear);
		
		String[] tempcurrdate = currdate.split("/");
		String currday = tempcurrdate[1];
		String currmonth = tempcurrdate[0];
		String curryear = tempcurrdate[2];
		
		int cd = Integer.parseInt(currday);
		int cm = Integer.parseInt(currmonth);
		int cy = Integer.parseInt(curryear);
		
		int finaldiff = ((cy-py) * 250) + ((cm-pm) * 31) + (cd-pd);
		
//		System.out.println("finaldiff for " + prevdate + " and " + currdate + " is -->" + finaldiff);
		
		return finaldiff;
		
	}
}
