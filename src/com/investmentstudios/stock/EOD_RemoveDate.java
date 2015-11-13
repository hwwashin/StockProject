package com.investmentstudios.stock;

import java.io.File;
import java.io.IOException;

public class EOD_RemoveDate extends EOD_Code {
	
	public static int newstockcount;
	public static String[] newstockdata = new String[$COUNT];
	
	public static void main(String[] args) throws IOException {
		
		displayTime();
		setDayInfo(args, "31", "08", "2015", "Monday");  // Day  Month  Year  DayOWeek
		setInvestmentDirectory();

		String dateteststring = month + "/" + day + "/" + year;
		
		File stockdir = new File($INVEST + "eoddatabase/");
    	File[] stocknamearray = stockdir.listFiles();
    	
    	for(int i=0;i<stocknamearray.length;i++) {
//    	for(int i=0;i<5;i++) {
    		getStockName(stockdir, stocknamearray[i]);
    		System.out.println(stockfilename);
    		System.out.println(dateteststring);
    		readStockFileWithHeader($INVEST + "eoddatabase/" + stockfilename, stockfilename);
    		
    		newstockcount = 0;
    		System.out.println("stockcount is " + stockcount);
    		for(int j=0;j<stockcount;j++) {
 //   			System.out.println(date[j] + " --> " + dateteststring);
    			if(!(date[j].equals(dateteststring))) {
    				newstockdata[newstockcount] = stockdata[j];
    				newstockcount++;
    			}
    		}
    		
    		for(int j=0;j<newstockcount;j++) {
    			System.out.println(j + " --> " + newstockdata[j]);
    		}
    		
    		writeDataToFile(newstockcount, newstockdata, stockfilename);
    	}
	}
}
