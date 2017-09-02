package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Yahoo_Code extends EOD_Code{

	public static int $ERRORCOUNT = 500000;
	public static int $CRAZYCLOSECOUNT = 1000000;
	public static int $ADJUSTEDCLOSECOUNT = 1000000;
	
	public static String $DATEAPPEND;
	public static String $YAHOOEOD;
	
	public static String $YAHOODIR;
	public static String $YAHOOLIST;
	public static String $YAHOODB;
	public static String $YAHOOFAILEDDOWNLOADS;
	public static String $YAHOOERROROUTPUT;
	public static String $YAHOOCRAZYCLOSEOUTPUT;
	public static String $YAHOOADJUSTEDCLOSEOUTPUT;
	public static String $YAHOOZEROCLOSEOUTPUT;
	public static String $YAHOOZERODATAOUTPUT;
	public static String $YAHOOMERGEDIR;
	
	public static Path target;
	public static int stockfailcount;
	public static int zerocount;
	public static int nullcount;
	public static int crazyclosecount;
	public static int adjustedclosecount;
	
	public static String yahooyear;
	public static String yahoomonth;
	public static String yahooday;
	public static String yahoodayoweek;
	
	public static String targetname = new String();
	public static String datarow = new String();
	public static String[] stockarray = new String[$COUNT];
	public static String[] stockfailarray = new String[$COUNT];
	public static String[] zerodata = new String[$ERRORCOUNT];
	public static String[] nulldata = new String[$ERRORCOUNT];
	public static String[] crazyclosedata = new String[$CRAZYCLOSECOUNT];
	public static String[] adjustedclosedata = new String[$ADJUSTEDCLOSECOUNT];
	
	public static File yahoostockdir;
	public static File[] yahoostocknamearray;
	
	public Yahoo_Code() { }
	
	public static void downloadYahooData() throws IOException {
	/*	
		BufferedReader stockFile = new BufferedReader(new FileReader($YAHOOLIST));
		stockcount = 0;
		datarow = stockFile.readLine();
		while (datarow != null && stockcount < 25000) {
			stockarray[stockcount] = datarow;
			datarow = stockFile.readLine();
			stockcount++;
		}
		stockFile.close();
	*/
		
		File file=new File($YAHOODB);
		boolean exists = file.exists();
		if (exists) {}
		else{
			new File($YAHOODB).mkdir();
		}

		System.out.println("Starting the stock downloads");
		stockfailcount=0;
		for(int i=0;i<stockcount;i++) {
			if(stockarray[i].equals("PRN")) {
				targetname = $YAHOODB + stockarray[i] + stockarray[i] + ".csv";
			}
			else {
				targetname = $YAHOODB + stockarray[i] + ".csv";				
			}
			URL website = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + stockarray[i] + "&a=0&b=1&c=1900&d=12&e=31&f=2100&g=d&ignore=.csv");
			target = Paths.get(targetname);
			if (i%500==0) { System.out.println();System.out.print("stock download --> " + stockarray[i]); }
			else if(i%10==0) System.out.print(".");
			try {
				Files.copy(website.openStream(), target, StandardCopyOption.REPLACE_EXISTING);
			}
			catch  (FileNotFoundException ex) {
				stockfailarray[stockfailcount] = stockarray[i];
//				System.out.println("this failed --> " + stockfailarray[stockfailcount]);
				stockfailcount++;
			}
		}
		
    	String outputfilename = $YAHOOFAILEDDOWNLOADS;
    	BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<stockfailcount;i++) {
	   		outputFile.write(stockfailarray[i]);
			outputFile.newLine();
	   	}   	
		outputFile.close();
	}

	public static void getYahooAdjustedCloseData() throws IOException {
    	adjustedclosecount = 0;
    	System.out.println("Getting Questionable Adjusted Close Data");
    	for (int i = 0; i < yahoostocknamearray.length; i++) {
    		BufferedReader datafile = new BufferedReader(new FileReader(yahoostocknamearray[i]));
			stockfilename = yahoostocknamearray[i].toString().substring(yahoostockdir.toString().length()+1);		
			if(i%2000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
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
				if(j<stockcount+1 && adjustedclosecount < $ADJUSTEDCLOSECOUNT) {
					double ratio1 = cl[j]/ac[j];
					double ratio2 = cl[j+1]/ac[j+1];
//					System.out.println("ratio1-->" + ratio1 + "  ratio2-->" + ratio2 + "  cl[j]-->" + cl[j] + "  ac[j]-->" + ac[j] + "  cl[j+1]-->" + cl[j+1] + "  ac[j+1]-->" + ac[j+1]);
					if((ratio1 > (ratio2 * 1.05)) || (ratio1 < 	(ratio2 *.95))) {
						adjustedclosedata[adjustedclosecount] = stockfilename + "," + stockdata[j] + ",," + stockdata[j+1];
						adjustedclosecount++;
					}
				}
			}
//			System.out.println(zerocount);
    	}
	}
	
	public static void getYahooCrazyCloseData() throws IOException {
    	crazyclosecount = 0;
    	System.out.println("Getting Crazy Close Close Data");
    	for (int i = 0; i < yahoostocknamearray.length; i++) {
    		BufferedReader datafile = new BufferedReader(new FileReader(yahoostocknamearray[i]));
			stockfilename = yahoostocknamearray[i].toString().substring(yahoostockdir.toString().length()+1);		
			if(i%2000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
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

				if((cl[j] > cl[j+1]*1.2 || cl[j] < cl[j+1]*0.8) && crazyclosecount < $CRAZYCLOSECOUNT) {
					crazyclosedata[crazyclosecount] = stockfilename + "," + stockdata[j] + ",," + stockdata[j+1];
					crazyclosecount++;
				}
			}
//			System.out.println(zerocount);
			
    	}
	}
	
	public static void getYahooNullData() throws IOException {
    	nullcount=0;
    	System.out.println("Getting Null Data");
    	for (int i = 0; i < yahoostocknamearray.length; i++) {
    		BufferedReader datafile = new BufferedReader(new FileReader(yahoostocknamearray[i]));
			stockfilename = yahoostocknamearray[i].toString().substring(yahoostockdir.toString().length()+1);		
			if(i%2000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
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
				if( open[j] == "" || high[j] == "" || low[j] == "" || close[j] == "" ) {
					nulldata[nullcount] = stockfilename + "," + stockdata[j];
//					System.out.println(stockfilename + " --> " + stockdata[j]);
//					if(screencount%100 == 0) System.out.println(screendata[screencount]);
					nullcount++;
				}
			}
//			System.out.println(zerocount);
			
    	}
	}
	
	public static void getYahooZeroData() throws IOException {
    	zerocount=0;
    	System.out.println("Getting Zero Data");
    	for (int i = 0; i < yahoostocknamearray.length; i++) {
    		BufferedReader datafile = new BufferedReader(new FileReader(yahoostocknamearray[i]));
//			System.out.println(yahoostocknamearray[i]);
    		stockfilename = yahoostocknamearray[i].toString().substring(yahoostockdir.toString().length()+1);	
			if(i%2000==0) { System.out.println();System.out.print(i + "  " + stockfilename); }
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
				if( op[j] == 0 || hi[j] == 0 || lo[j] == 0 || cl[j] == 0 ) {
					zerodata[zerocount] = stockfilename + "," + stockdata[j];
//					System.out.println(stockfilename + " --> " + stockdata[j]);
//					if(screencount%100 == 0) System.out.println(screendata[screencount]);
					zerocount++;
				}
			}
//			System.out.println(zerocount);
			
    	}
	}	
	
	public static void saveYahooZeroData() throws IOException{
		String outputfilename = $YAHOOZERODATAOUTPUT;
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<zerocount;i++) {
	   		outputFile.write(zerodata[i]);
			outputFile.newLine();
	   	}  	
		outputFile.close();
	}
	
	public static void saveYahooNullData() throws IOException{
		String outputfilename = $YAHOOERROROUTPUT;
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<nullcount;i++) {
	   		outputFile.write(nulldata[i]);
			outputFile.newLine();
	   	}	   	
		outputFile.close();
	}
	
	public static void saveYahooCrazyCloseData() throws IOException{
		String outputfilename = $YAHOOCRAZYCLOSEOUTPUT;
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<crazyclosecount;i++) {
	   		outputFile.write(crazyclosedata[i]);
			outputFile.newLine();
	   	}   	
		outputFile.close();
	}
	
	public static void saveYahooAdjustedCloseData() throws IOException{
		String outputfilename = $YAHOOADJUSTEDCLOSEOUTPUT;
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<adjustedclosecount;i++) {
	   		outputFile.write(adjustedclosedata[i]);
			outputFile.newLine();
	   	}   	
		outputFile.close();
	}
	
	public static void setYahooDirs() {
		$YAHOODIR = $INVEST + "yahoodb/";
		
		$YAHOOLIST = $YAHOODIR + $DATEAPPEND + ".yahoodownloadlist.csv";
		$YAHOOFAILEDDOWNLOADS = $YAHOODIR + $DATEAPPEND + ".yahoofaileddownloads.csv";
		$YAHOODB = $YAHOODIR + $DATEAPPEND + ".YahooDownload/";
		$YAHOOERROROUTPUT = $YAHOODIR + $DATEAPPEND + ".yahooerroroutput.csv";
		$YAHOOCRAZYCLOSEOUTPUT = $YAHOODIR + $DATEAPPEND + ".yahoocrazycloseoutput.csv";
		$YAHOOADJUSTEDCLOSEOUTPUT = $YAHOODIR + $DATEAPPEND + ".yahooadjustedcloseoutput.csv";
		$YAHOOZERODATAOUTPUT  = $YAHOODIR + $DATEAPPEND + ".yahoozerodataoutput.csv";
	}
	
	public static void insertSplits() {
		
		
		
		
	}
	
}
