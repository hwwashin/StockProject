package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Indicators_Stock {
	
	public static String hostname = new String();
	public static String $INVEST = new String();
	public static String $INVEST2 = new String();
	
	public static int $TESTNUM = 5000;
	public static int $COUNT = 25000;
	
	public static int filecount;
	public static int stockcount;
	
	public static String stockfileheader = new String();
	public static String stockfilename = new String();
	public static String[] StrategyName = new String[$COUNT];
	
	public static File stockDBDirectory;
	public static File[] stocknamearray;
	
	public static double[] op = new double[$COUNT];
	public static double[] hi = new double[$COUNT];
	public static double[] lo = new double[$COUNT];
	public static double[] cl = new double[$COUNT];
	public static double[] vo = new double[$COUNT];
	public static double[] ac = new double[$COUNT];
	
	public static String[] date = new String[$COUNT];
	public static String[] open = new String[$COUNT];
	public static String[] high = new String[$COUNT];
	public static String[] low = new String[$COUNT];
	public static String[] close = new String[$COUNT];
	public static String[] volume = new String[$COUNT];
	public static String[] adjclose = new String[$COUNT];
	
	public static String[] stockdata = new String[$COUNT];
	
	public static String convertDate(String date) {
		String[] dateparts1 = date.split("/");
		if(dateparts1.length == 3) {
			if(dateparts1[0].length() == 1) {
				dateparts1[0] = "0" + dateparts1[0];
			}
			if(dateparts1[1].length() == 1) {
				dateparts1[1] = "0" + dateparts1[1];
			}
			return dateparts1[0] + "/" + dateparts1[1] + "/" + dateparts1[2];
		}
		String[] dateparts2 = date.split("-");
		if(dateparts2.length == 3) {
			return dateparts2[1] + "/" + dateparts2[2] + "/" + dateparts2[0];
		}
		return date;
	}
	
	public static double convertDateToDouble(String dateval) {
		double value = Double.parseDouble(dateval);
		return value;
	}
	
	public static void displayTime() {
    	Date dNow = new Date( );
    	SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    	System.out.println("Current Date: " + ft.format(dNow));
	}
	
	public static void getStockName(File fname, File sname) {
		stockfilename = sname.toString().substring(fname.toString().length()+1);
	}
	
	public static void initializeDatabaseDirectory() {
		stockDBDirectory = new File($INVEST + "/eoddatabase/");
		stocknamearray = stockDBDirectory.listFiles();
	}

	public static void initializeDownloadDirectory(String dir) {
		stockDBDirectory = new File(dir);
		stocknamearray = stockDBDirectory.listFiles();
	}
	
	public static void readStockFile(String dir, String fname) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader(dir));
		stockfilename = fname;
		
		stockcount = 0;
		stockdata[stockcount] = dataFile.readLine();
		String datarow = dataFile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount + " --> " + stockFileName + " --> " + datarow);
			stockdata[stockcount] = datarow;
			setStockData();
			stockcount++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
//		System.out.println("Data Downloaded For " + stockFileName);
	}
	
	public static void readStockFileWithHeader(String dir, String fname) throws IOException {
		BufferedReader stockfile = new BufferedReader(new FileReader(dir));
		stockfilename = fname;
		
		stockfileheader = stockfile.readLine();
		stockcount = 0;
		String datarow = stockfile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount + " --> " + stockFileName + " --> " + datarow);
			stockdata[stockcount] = datarow;
			setStockData();
			stockcount++;
			datarow = stockfile.readLine();
		}	
		stockfile.close();
//		System.out.println("Data Downloaded For " + stockFileName);
	}
	
	public static void setInvestmentDirectory() throws IOException {

		Process proc = Runtime.getRuntime().exec("hostname");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		hostname = stdInput.readLine();
//		System.out.println(hostname);
		
		if(hostname.equalsIgnoreCase("SouthernEng-PC")) {
			$INVEST = "E:/Investments/";
//			$INVEST = "C:/Users/Chris Scott Miller/Documents/Investments/";
		}
		
		else if(hostname.equalsIgnoreCase("Engineering2")) {
			$INVEST = "C:/Users/Engineering1/Documents/Investments/";
		}

		else if(hostname.equalsIgnoreCase("Willis")) {
//			$INVEST = "F:/Investments/";
			$INVEST = "C:/Users/Will/Documents/Investments/";
		}
		
		else if(hostname.equalsIgnoreCase("willis2013")) {
			$INVEST = "C:/Users/Willis/Documents/Investments/";
		}
		
		else if(hostname.equalsIgnoreCase("Dell-PC")) {
			$INVEST = "E:/Investments/";
		}
		else if(hostname.equalsIgnoreCase("William-PC")) {
			$INVEST = "C:/Users/William/Documents/Investments/";
		}
		else if(hostname.equalsIgnoreCase("Desktop2017")) {
			$INVEST = "F:/";
		}
	}
	
	public static void setStockData() {
		String[] temp = stockdata[stockcount].split(",");
//		System.out.println(stockdata[stockcount]);
		
		date[stockcount] = convertDate(temp[0]);
		
		open[stockcount] = temp[1];
		high[stockcount] = temp[2];
		low[stockcount] = temp[3];
		close[stockcount] = temp[4];
		volume[stockcount] = temp[5];
//		adjclose[stockcount] = temp[6];
		
		op[stockcount] = Double.parseDouble(temp[1]);
		hi[stockcount] = Double.parseDouble(temp[2]);
		lo[stockcount] = Double.parseDouble(temp[3]);
		cl[stockcount] = Double.parseDouble(temp[4]);
		vo[stockcount] = Double.parseDouble(temp[5]);
//		ac[stockcount] = Double.parseDouble(temp[6]);
	}
	
	public static void setYahooStockData() {
		String[] temp = stockdata[stockcount].split(",");
//		System.out.println(stockdata[stockcount]);
		
		date[stockcount] = convertDate(temp[0]);
		
		open[stockcount] = temp[1];
		high[stockcount] = temp[2];
		low[stockcount] = temp[3];
		close[stockcount] = temp[4];
		volume[stockcount] = temp[5];
		adjclose[stockcount] = temp[6];
		
		op[stockcount] = Double.parseDouble(temp[1]);
		hi[stockcount] = Double.parseDouble(temp[2]);
		lo[stockcount] = Double.parseDouble(temp[3]);
		cl[stockcount] = Double.parseDouble(temp[4]);
		vo[stockcount] = Double.parseDouble(temp[5]);
		ac[stockcount] = Double.parseDouble(temp[6]);
	}
	
	public static void writeDataToFile(int count, String[] data, String fname) throws IOException {
		String outputfilename = $INVEST + "/tempfile.csv";
		File oldfile = new File(outputfilename);
		oldfile.delete();

		BufferedWriter outputfile = new BufferedWriter(new FileWriter(outputfilename));
		outputfile.write(stockfileheader);
		outputfile.newLine();
		for(int i=0;i<count;i++) {
//	   		System.out.println(i + "  -  " + data[i]);
	   	    outputfile.write(data[i]);
//	   		outputfile.write(dailyData2[i]);
	   	   	outputfile.newLine();
	   	}
		outputfile.close();

		File sourceFile = new File($INVEST + "/tempfile.csv");
		File targetFile = new File(fname);
		targetFile.delete();
		sourceFile.renameTo(targetFile);
	}

}
