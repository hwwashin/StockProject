package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EOD_DataSync extends EOD_Code {
	
	public static int stockcount0 = 0;
	public static int stockcount1 = 0;
	public static int stockcount2 = 0;
	public static int stockcount3 = 0;
	
	public static int $SYNCCOUNT = 1304;
	public static String $INVEST = "C:/Users/willw/Documents/Investments/TESTDATA/";
	public static String[] filedata = new String[$SYNCCOUNT];
	
	public static String[] info1 = new String[50000];
	public static String[] info2 = new String[50000];
	public static String[] info3 = new String[50000];
	
	public static String[] out1 = new String[1000000];
	
	public EOD_DataSync() {
		
	}

	public static void main(String[] args) throws IOException {
	
	    readSyncDataFile($INVEST + "amex.csv");	
	    
	    for(int i=0;i<$SYNCCOUNT;i++) {
	    	System.out.println(i + " --> " + filedata[i]);
/*
	    	readInfo1($INVEST + "DOWNLOADS/AMEX/AMEX_" + filedata[i] + ".csv");
	    	readInfo2($INVEST + "HISTORICAL/AMEX/AMEX_" + filedata[i] + ".csv");
	    	
	    	if(stockcount1 != stockcount2) {
	    		System.out.println("At location " + i + " the stock counts in the files are not equal");
	    		System.out.println("The count for the download file is --> " + stockcount1);
	    		System.out.println("The count for the historical file is --> " + stockcount2);
	    	}
	    	*/
	    	
	    	if(i>523 && i<910) {	
	    		readInfo1($INVEST + "DOWNLOADS/AMEX/AMEX_" + filedata[i] + ".csv");
	    		readInfo3($INVEST + "ORIGINAL/AMEX/AMEX_" + filedata[i] + ".csv");
	    		
	    		if(stockcount1 != stockcount3) {
		    		System.out.println("At location " + i + " the stock counts in the files are not equal");
		    		System.out.println("The count for the download file is --> " + stockcount1);
		    		System.out.println("The count for the historical file is --> " + stockcount3);
	    		}
	        }
	        
	    	
	    
	    }
		
	}

	public static void readSyncDataFile(String dir) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader(dir));
		stockcount0 = 0;
		String datarow = dataFile.readLine();
		while (datarow != null) {
			System.out.println(stockcount0 + " --> " + datarow);
			filedata[stockcount0] = datarow;
			stockcount0++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
	}
	
	public static void readInfo1(String dir) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader(dir));
		stockcount1 = 0;
		String datarow = dataFile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount1 + " --> " + datarow);
			info1[stockcount1] = datarow;
			stockcount1++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
	}

	public static void readInfo2(String dir) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader(dir));
		stockcount2 = 0;
		String datarow = dataFile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount2 + " --> " + datarow);
			info2[stockcount2] = datarow;
			stockcount2++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
	}
	
	public static void readInfo3(String dir) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader(dir));
		stockcount3 = 0;
		String datarow = dataFile.readLine();
		while (datarow != null) {
//			System.out.println(stockcount3 + " --> " + datarow);
			info3[stockcount3] = datarow;
			stockcount3++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
	}
	
}

// First, I will create a text file that has all the dates I want listed - and I'm going to read that in
// Second, I will go through each of those and construct the filenames I want to read in
// Third, I will read in those filenames depending on where I am - 2 or 3 datasets
// Fourth, I will create an array and just save any datasets that don't match
// Last, I will to write all the unmatching datasets out into a file
// DONE 