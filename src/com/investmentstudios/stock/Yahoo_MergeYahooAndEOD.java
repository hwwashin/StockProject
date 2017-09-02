package com.investmentstudios.stock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Yahoo_MergeYahooAndEOD extends Yahoo_InitialDownload {

	public static String $EODDB;
	public static int $YAHOOCOUNT = 50000;
	
	public static int yahoofilecount = 0;
	public static int mergecount = 0;
	
	public static String targetdate = new String();
	
	public static String[] yahoostocklist = new String[$YAHOOCOUNT];
	public static String[] mergestockdata = new String[$YAHOOCOUNT];

	public static void main(String[] args) throws IOException {
		
		displayTime();
		
/* set up the date I want to check */
		yahooday = "23";
		yahoomonth = "05";
		yahooyear = "2016";
		
/* First read in all the yahoo files I want to test - create that string of stock files*/		
/* select the directory and create an array of the stocks */
		
		targetdate = yahoomonth + "/" + yahooday + "/" + yahooyear;
		
		System.out.println("Comparing Yahoo & EOD Data for " + targetdate);
		setInvestmentDirectory();
		$YAHOODB = $INVEST + "yahoodb/2016.05.23.YahooDownload/";
		$EODDB = $INVEST + "eoddatabase/";
		
//		System.out.println($INVEST);
//		System.out.println($YAHOODB);
//		System.out.println($EODDB);
		
		stockDBDirectory = new File($YAHOODB);
		stocknamearray = stockDBDirectory.listFiles();
		
		for(int i=0;i<stocknamearray.length;i++) {
			yahoostocklist[i] = stocknamearray[i].toString().substring(stockDBDirectory.toString().length()+1);
			yahoofilecount++;
		}
	
		for(int i=0;i<yahoofilecount;i++) {
//			System.out.println(yahoostocklist[i]);
		}
		
// Next read in both the yahoo stock data file and the EOD stock data file 
// yahoo - stockdata and stockcount - then use setstockdata 
		
		for(int i=0;i<yahoofilecount;i++) {
//		for(int i=0;i<36;i++) {
			File file = new File($EODDB + yahoostocklist[i]);
			boolean exists = file.exists();
			if (exists) {
				
//				System.out.println("Starting with stock " + yahoostocklist[i]);
//				System.out.println(yahoostocklist[i]);
				
				if(i%2000 == 0) { System.out.println();System.out.print("Gathering Stock Data --> " + yahoostocklist[i]); }
				if(i%25 == 0) { System.out.print("."); }

				boolean yahoofound=false;
				boolean eodfound=false;
				
				readStockFileWithHeader($YAHOODB + yahoostocklist[i], yahoostocklist[i]);	
				for(int j=0;j<stockcount;j++) {
//					System.out.println("STOCK DATA -- Target Date --> " + targetdate + "   Stock Date --> " + date[j]);
					if(date[j].equals(targetdate)) {
						mergestockdata[mergecount] = yahoostocklist[i] + ",Yahoo Data," + stockdata[j];
						yahoofound = true;
//						System.out.println("Matching Stock Data --> " + stockdata[j]);
						j = stockcount;
					}
				}
				if(yahoofound == false) {
					mergestockdata[mergecount] = yahoostocklist[i] + ",Yahoo Data,NOTHING,";
				}
				
				readEODStockFile($EODDB + yahoostocklist[i], yahoostocklist[i]);
				for(int j=0;j<eodcount;j++) {
//					System.out.println("EOD DATA -- Count --> " + j + "  Target Date --> " + targetdate + "   EOD Date --> " + eoddate[j]);
					if(eoddate[j].equals(targetdate)) {
						mergestockdata[mergecount] += ",EOD Data," + eodstockdata[j];
						eodfound = true;
//						System.out.println("Matching EOD Data --> " + eodstockdata[j]);
						j = eodcount;
					}
				}
				if(eodfound == false) {
					mergestockdata[mergecount] += ",EOD Data,NOTHING,";
				}

//				System.out.println("Merge Data --> " + mergestockdata[mergecount]);;
				mergecount++;					
			}	
			
		}
		
		String outputfilename = $INVEST + "/tempfile.csv";
		File oldfile = new File(outputfilename);
		oldfile.delete();

		BufferedWriter outputfile = new BufferedWriter(new FileWriter(outputfilename));
		for(int i=0;i<mergecount;i++) {
	   	    outputfile.write(mergestockdata[i]);
	   	   	outputfile.newLine();
	   	}
		outputfile.close();

		File sourceFile = new File($INVEST + "/tempfile.csv");
		File targetFile = new File($INVEST + "yahoodb/" + yahooyear + "." + yahoomonth + "." + yahooday + ".yahooeodmergetest.csv");
		targetFile.delete();
//		System.out.println(targetFile);
		sourceFile.renameTo(targetFile);
		
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
