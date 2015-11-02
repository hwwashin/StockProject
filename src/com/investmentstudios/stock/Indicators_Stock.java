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
	
	public static int $TESTNUM = 50;
	public static int $COUNT = 25000;
	
	public static int filecount;
	public static int stockcount;
	
	public static String stockfileheader = new String();
	public static String stockfilename = new String();
	
	public static double[] op = new double[$COUNT];
	public static double[] hi = new double[$COUNT];
	public static double[] lo = new double[$COUNT];
	public static double[] cl = new double[$COUNT];
	public static double[] vo = new double[$COUNT];
	public static double[] ac = new double[$COUNT];
	public static double[] divergence = new double[$COUNT];
	
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
			$INVEST = "C:/Users/Chris Scott Miller/Documents/Investments/";
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
