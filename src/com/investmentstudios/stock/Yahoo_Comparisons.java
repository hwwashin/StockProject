package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Yahoo_Comparisons extends Yahoo_CompareWithEOD {

	public static String $YAHOOTEST = "C:/Users/Engineering1/Documents/Investments/Yahoo Research/";
	public static String[] yahootestdata = new String[1000000];
	public static String[][] yahooclosearray = new String[25000][22];
	public static int yahootestcount;
	public static int yahooclosearraycount;
	
	public Yahoo_Comparisons() { }

	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		setInvestmentDirectory();
		
		String[] yahoofilename = new String[21];
		yahoofilename[0] = $YAHOOTEST + "2008.10.17 Yahoo Data/";  // 20081016
		yahoofilename[1] = $YAHOOTEST + "2009.07.10 Yahoo Data/";  // 
		yahoofilename[2] = $YAHOOTEST + "2009.09.29 Yahoo Data/";  // 
		yahoofilename[3] = $YAHOOTEST + "2009.11.24 Yahoo Data/";  // 
		yahoofilename[4] = $YAHOOTEST + "2010.01.22 Yahoo Data/";  // 
		yahoofilename[5] = $YAHOOTEST + "2010.04.08 Yahoo Data/";  // 
		yahoofilename[6] = $YAHOOTEST + "2010.08.05 Yahoo Data/";  // 
		yahoofilename[7] = $YAHOOTEST + "2011.05.13 Yahoo Data/";  // 
		yahoofilename[8] = $YAHOOTEST + "2011.08.02 Yahoo Data/";  // 
		yahoofilename[9] = $YAHOOTEST + "2012.10.23 Yahoo Data/";  // 
		yahoofilename[10] = $YAHOOTEST + "2013.03.20 Yahoo Data/"; // 
		yahoofilename[11] = $YAHOOTEST + "2013.04.23 Yahoo Data/"; // 
		yahoofilename[12] = $YAHOOTEST + "2013.06.25 Yahoo Data/"; // 
		yahoofilename[13] = $YAHOOTEST + "2015.04.02 Yahoo Data/"; // 
		yahoofilename[14] = $YAHOOTEST + "2016.01.11 Yahoo Data/"; // 
		yahoofilename[15] = $YAHOOTEST + "2016.01.22 Yahoo Data/"; // 
		yahoofilename[16] = $YAHOOTEST + "2016.02.02 Yahoo Data/"; // 
		yahoofilename[17] = $YAHOOTEST + "2016.05.20 Yahoo Data/"; // 
		yahoofilename[18] = $YAHOOTEST + "2016.05.23 Yahoo Data/"; // 
		yahoofilename[19] = $YAHOOTEST + "2016.06.24 Yahoo Data/"; // 
		yahoofilename[20] = $YAHOOTEST + "2016.07.29 Yahoo Data/"; // 	
		
		String[] yahoofname = new String[21];
		yahoofname[0] = "2008.10.17";
		yahoofname[1] = "2009.07.10";
		yahoofname[2] = "2009.09.29";
		yahoofname[3] = "2009.11.24";
		yahoofname[4] = "2010.01.22";
		yahoofname[5] = "2010.04.08";
		yahoofname[6] = "2010.08.05";
		yahoofname[7] = "2011.05.13";
		yahoofname[8] = "2011.08.02";
		yahoofname[9] = "2012.10.23";
		yahoofname[10] = "2013.03.20";
		yahoofname[11] = "2013.04.23";
		yahoofname[12] = "2013.06.25";
		yahoofname[13] = "2015.04.02";
		yahoofname[14] = "2016.01.11";
		yahoofname[15] = "2016.01.22";
		yahoofname[16] = "2016.02.02";
		yahoofname[17] = "2016.05.20";
		yahoofname[18] = "2016.05.23";
		yahoofname[19] = "2016.06.24";
		yahoofname[20] = "2016.07.29";
		
		yahootestcount = 0;
		
		for(int i=0;i<21;i++) {
//		for(int i=0;i<1;i++) {
			System.out.println();
			System.out.println();
			System.out.println(yahoofilename[i]);
			
			stockDBDirectory = new File(yahoofilename[i]);
			stocknamearray = stockDBDirectory.listFiles();
			
			for(int j=0;j<stocknamearray.length;j++) {
//			for(int j=0;j<1;j++) {
				
				yahoostocklist[j] = stocknamearray[j].toString().substring(stockDBDirectory.toString().length()+1);

				if(j%2000 == 0) { System.out.println();System.out.print(yahoostocklist[j] + " --> "); }
				if(j%25 == 0) { System.out.print("."); }
				
		   		BufferedReader datafile = new BufferedReader(new FileReader(yahoofilename[i] + yahoostocklist[j]));
				
				stockcount = 0;
				stockdata[stockcount] = datafile.readLine();
				String datarow = datafile.readLine();
				while (datarow != null) {
					stockdata[stockcount] = datarow;
					setYahooStockData();
//					System.out.println(datarow);
					stockcount++;
					datarow = datafile.readLine();
				}
				datafile.close();
/*
				System.out.println(yahoofilename[i]);
				System.out.println(yahoostocklist[j]);
				System.out.println(stockdata[1]);
				System.out.println(stockdata[stockcount]);
*/				
//				System.out.println(date[1]);
//				System.out.println(date[stockcount-1]);
				
				String[] tempdate = date[1].split("/");
				if(tempdate.length > 1) date[1] = tempdate[2] + tempdate[0] + tempdate[1];
				tempdate = date[stockcount-1].split("/");
				if(tempdate.length > 1) date[stockcount-1] = tempdate[2] + tempdate[0] + tempdate[1];
				
//				System.out.println(date[1]);
//				System.out.println(date[stockcount-1]);
				
				yahootestdata[yahootestcount] = yahoofname[i] + ",";
				yahootestdata[yahootestcount] += yahoostocklist[j] + ",";
				yahootestdata[yahootestcount] += date[1] + "," + open[1] + "," + high[1] + "," + low[1] + "," + close[1] + "," + 
						volume[1] + "," + adjclose[1] + ",";
				yahootestdata[yahootestcount] += yahoostocklist[j] + ",";
				yahootestdata[yahootestcount] += date[stockcount-1] + "," + open[stockcount-1] + "," + high[stockcount-1] + "," + 
						low[stockcount-1] + "," + close[stockcount-1] + "," + volume[stockcount-1] + "," + adjclose[stockcount-1] + ",";
				yahootestcount++;
			}	
			System.out.println();
			System.out.println();
			System.out.println("i = " + i + ", stocknamearray.length = " + stocknamearray.length + ", and yahootestcount = " + yahootestcount);
		}
		
    	String outputfilename = $INVEST + "yahoodata.csv";
    	BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
    	outputFile.newLine();
	   	for(int i=0;i<yahootestcount;i++) {
//	   		System.out.println(yahootestdata[i]);
	   		outputFile.write(yahootestdata[i]);
			outputFile.newLine();
	   	}   	
		outputFile.close();

		System.out.println("About to initialize the yahooclosearray, and the stocknamearray.length is " + stocknamearray.length);
		
		for(int i=0;i<stocknamearray.length;i++) {
			for(int j=1;j<22;j++) {
				yahooclosearray[i][j] = "N/A";
			}
		}
		
		for(int i=0;i<stocknamearray.length;i++) {
			yahooclosearray[i][0] = yahoostocklist[i];
		}
		
		System.out.println("About to set up the yahooclosearray, and the stocknamearray.length is " + stocknamearray.length);
		
		for(int i=0;i<stocknamearray.length;i++) {
			for(int j=0;j<yahootestcount;j++) {
				String[] tempdata = yahootestdata[j].split(",");
				if(yahooclosearray[i][0].equals(tempdata[1])) {
					if(tempdata[0].equals(yahoofname[0])) yahooclosearray[i][1] = tempdata[10];
					if(tempdata[0].equals(yahoofname[1])) yahooclosearray[i][2] = tempdata[10];
					if(tempdata[0].equals(yahoofname[2])) yahooclosearray[i][3] = tempdata[10];
					if(tempdata[0].equals(yahoofname[3])) yahooclosearray[i][4] = tempdata[10];
					if(tempdata[0].equals(yahoofname[4])) yahooclosearray[i][5] = tempdata[10];
					if(tempdata[0].equals(yahoofname[5])) yahooclosearray[i][6] = tempdata[10];
					if(tempdata[0].equals(yahoofname[6])) yahooclosearray[i][7] = tempdata[10];
					if(tempdata[0].equals(yahoofname[7])) yahooclosearray[i][8] = tempdata[10];
					if(tempdata[0].equals(yahoofname[8])) yahooclosearray[i][9] = tempdata[10];
					if(tempdata[0].equals(yahoofname[9])) yahooclosearray[i][10] = tempdata[10];
					if(tempdata[0].equals(yahoofname[10])) yahooclosearray[i][11] = tempdata[10];
					if(tempdata[0].equals(yahoofname[11])) yahooclosearray[i][12] = tempdata[10];
					if(tempdata[0].equals(yahoofname[12])) yahooclosearray[i][13] = tempdata[10];
					if(tempdata[0].equals(yahoofname[13])) yahooclosearray[i][14] = tempdata[10];
					if(tempdata[0].equals(yahoofname[14])) yahooclosearray[i][15] = tempdata[10];
					if(tempdata[0].equals(yahoofname[15])) yahooclosearray[i][16] = tempdata[10];
					if(tempdata[0].equals(yahoofname[16])) yahooclosearray[i][17] = tempdata[10];
					if(tempdata[0].equals(yahoofname[17])) yahooclosearray[i][18] = tempdata[10];
					if(tempdata[0].equals(yahoofname[18])) yahooclosearray[i][19] = tempdata[10];
					if(tempdata[0].equals(yahoofname[19])) yahooclosearray[i][20] = tempdata[10];
					if(tempdata[0].equals(yahoofname[20])) yahooclosearray[i][21] = tempdata[10];
				}
			}
		}
	
    	outputfilename = $INVEST + "yahooclosedata.csv";
    	outputFile = new BufferedWriter(new FileWriter(outputfilename));
    	outputFile.newLine();
    	
    	System.out.println("The stocknamearray.length is " + stocknamearray.length);
  	
	   	for(int i=0;i<stocknamearray.length;i++) {
	   		for(int j=0;j<22;j++) {
//	   			System.out.println("From a total stockcount of " + stockcount  + " the yahooclosearray[" + i + "][" + j + "] = " + yahooclosearray[i][j]);
		   		outputFile.write(yahooclosearray[i][j]);
				outputFile.write(",");
	   		}
	   		outputFile.newLine();
	   	}   	
		outputFile.close();		
		
		System.out.println();
		displayTime();
		
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File($INVEST + "go.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } 
	    catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	   	JOptionPane optionPane = new JOptionPane();
	   	JDialog dialog = optionPane.createDialog("Run is complete - but check for errors!!");
	   	dialog.setAlwaysOnTop(true);
	   	dialog.setVisible(true);
	}
}
