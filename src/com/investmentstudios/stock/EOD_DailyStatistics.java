package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

// Still need to add trin & hi/lo line where there are 100 more highs or lows
// Then need to read in everything and compute whatever else - trin 10dma, 10 day hi/lo line, etc

public class EOD_DailyStatistics extends EOD_Code {
	
	public static int $EXCHANGECOUNT = 3;
	
	public static int targetposition;
	public static int indicatorcount;
	public static int insertposition;
	
	public static String targetdate;
	public static String outputstring;
	
	public static double[] advancecount = new double[$EXCHANGECOUNT];
	public static double[] declinecount = new double[$EXCHANGECOUNT];
	public static double[] unchangedcount = new double[$EXCHANGECOUNT];
	public static double[] advancevolume = new double[$EXCHANGECOUNT];
	public static double[] declinevolume = new double[$EXCHANGECOUNT];
	public static double[] unchangedvolume = new double[$EXCHANGECOUNT];
	public static double[] trin = new double[$EXCHANGECOUNT];
	public static double[] trin10dma = new double[$EXCHANGECOUNT];
	public static double[] yearhicount = new double[$EXCHANGECOUNT];
	public static double[] yearlocount = new double[$EXCHANGECOUNT];
	public static double[] newhicount = new double[$EXCHANGECOUNT];
	public static double[] newlocount = new double[$EXCHANGECOUNT];
	public static double[] hilo = new double[$EXCHANGECOUNT];
	public static double[] hilo10daycount = new double[$EXCHANGECOUNT];
	public static double[] over200dmacount = new double[$EXCHANGECOUNT];
	public static double[] over200dmapercent = new double[$EXCHANGECOUNT];
	
	public static double[][] pasttrin = new double[$EXCHANGECOUNT][$EODARRAYSIZE];
	public static double[][] pasthilo = new double[$EXCHANGECOUNT][$EODARRAYSIZE];

	public static String[] exchangelist = new String[$EXCHANGECOUNT];
	public static String[] eodstocklist = new String[$EODARRAYSIZE];
	public static String[] indicatordata = new String[$EODARRAYSIZE];
	public static String[] indicatordata2 = new String[$EODARRAYSIZE];
	
	public static void main(String[] args) throws IOException {
		// read in the EOD file for each exchange - AMEX, NASDAQ, NYSE - for the day I want to compute
		// then read in each individual file - and find the date I'm working on
		// count (adv, decline, unch) + volumes x3, 52 wk hi, 52 wk lo, new high, new low, >200 DMA
		// calculate trin, trin 100dma, 10 day count hi / lo
		// finally - figure out how to do the grades
		
		// and I want to be able to do this for any day and insert the data into my data file - first create the data
		
		displayTime();
		
		setInvestmentDirectory();
		setDayInfo(args, "08", "08", "2016", "Monday");  // Day  Month  Year  DayOWeek - change these before running if so desired
		setEODDirectories();
		
		targetdate = month + "/" + day + "/" + year;
		exchangelist[0] = nysePath;
		exchangelist[1] = nasdaqPath;
		exchangelist[2] = amexPath;
		
		for(int i=0;i<$EXCHANGECOUNT;i++) {
			eodcount = 0;
			System.out.println();
			System.out.println();
			System.out.println("Loading and analyzing files in --> " + exchangelist[i]);
			
			// Load in the EOD datafile for each corresponding predefined exchange
			loadData(exchangelist[i]);
			
			// Split out the stock list for each exchange EOD datafile
			for(int j=0;j<eodcount;j++) {
//				System.out.println(eodstockdata[j] + "   " + exchangelist[i]);
				String[] temp = eodstockdata[j].split(",");
				eodstocklist[j] = temp[0];
//				System.out.println(eodstocklist[j]);
			}

			// Next load in the stock data for each stock list and compute the various statistics
			for(int j=0;j<eodcount;j++) {
				
				if(eodstocklist[j].equals("PRN")) {
					eodstocklist[j] = "PRNPRN";
				}
				
				if(j%1000 == 0) { System.out.println();System.out.print("Updating Stock Data --> " + eodstocklist[j]); }
				if(j%10 == 0) { System.out.print("."); }
				
				readStockFile($INVEST + "eoddatabase/" + eodstocklist[j] + ".csv", eodstocklist[j]);
				targetposition = getDatePosition(targetdate);
				int tp = targetposition;

				// take care of the advances, declines, unchanged and the corresponding volumes
				if(cl[tp] > cl[tp+1]) {
					advancecount[i]++;
					advancevolume[i] += vo[tp];
				}
				else if(cl[tp] < cl[tp+1]) {
					declinecount[i]++;
					declinevolume[i] += vo[tp];
				}
				else {
					unchangedcount[i]++;
					unchangedvolume[i] += vo[tp];
				}
				
				// handle the various high and low counts
				if(is52WeekHigh(tp)) yearhicount[i]++;
				if(is52WeekLow(tp)) yearlocount[i]++;
				if(isLifetimeHigh(tp)) newhicount[i]++;
				if(isLifetimeLow(tp)) newlocount[i]++;
				
				if(cl[tp] > MA(200,tp)) over200dmacount[i]++;
			}
			
			trin[i] = (advancecount[i]/declinecount[i])/(advancevolume[i]/declinevolume[i]);
//			System.out.println("Trin of " + i+1 + "  " + trin[i]);
			
			// trin 10dma is more difficult - I have to go back and do this TEN TIMES!!!  I'm going to have to think about this one
			// this may be something dependent on previous trin data versus trying to recreate this each time
			
			if(yearhicount[i] > (yearlocount[i] + 99)) hilo[i] = 1;
			else if(yearlocount[i] > (yearhicount[i] + 99)) hilo[i] = -1;
			
			over200dmapercent[i] = over200dmacount[i] / eodcount;

		}
		
		System.out.println();
		System.out.println();
		
//		printResults();
		
		loadCurrentIndicatorData();
		insertposition = findInsertPosition(indicatordata, indicatorcount);
		calculateTrin10DMA();
		calculateHiLo10Count();
			
		saveResults();
		
		displayTime();
		System.out.println();
		System.out.println();
		/*
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
	   	*/
	}
	
	public static void calculateHiLo10Count(){
		double value = 0;
		int startposition = insertposition;
		int lastposition = 0;
		if(indicatorcount < 12) lastposition = indicatorcount;
		else lastposition = insertposition + 9;
		System.out.println("Start Position = " + startposition);
		System.out.println("Last Position = " + lastposition);
		if(startposition < lastposition) {
			for(int i=0;i<$EXCHANGECOUNT;i++) {
				value = hilo[i];
				for(int j=startposition;j<lastposition;j++) {
					value += pasthilo[i][j];
				}
				hilo10daycount[i] = value;
				System.out.println("Hi Lo 10 Day Count = " + hilo10daycount[i]);
			}
		}
		else 
			for(int i=0;i<$EXCHANGECOUNT;i++) 
				hilo10daycount[i] = hilo[i];
	}
	
	public static void calculateTrin10DMA(){
		double value;
		int startposition = insertposition;
		int lastposition = 0;
		if(indicatorcount < 12) lastposition = indicatorcount;
		else lastposition = insertposition + 9;
		if(startposition < lastposition) {
			for(int i=0;i<$EXCHANGECOUNT;i++) {
				value = trin[i];
				for(int j=startposition;j<lastposition;j++) {
					value += pasttrin[i][j];
				}
				trin10dma[i] = value / (lastposition - startposition + 1);
				System.out.println("10 DMA Trin = " + trin10dma[i]);
			}
		}
		else 
			for(int i=0;i<$EXCHANGECOUNT;i++)
				trin10dma[i] = trin[i];
	}
	
	public static int findInsertPosition(String[] indicatordata, int indicatorcount) {
		int position = indicatorcount;
		int[] datearray = new int[$EODARRAYSIZE];
		
		// trin10dma[i] = 0;
		// hilo10daycount[i] = 0;
		
		String tempstring = year + month + day;
		int datetarget = Integer.parseInt(tempstring);
		System.out.println();
		
		System.out.println("Indicator Count = " + indicatorcount);
		if(indicatorcount > 2) {
			for(int i=2;i<indicatorcount;i++) {
//				System.out.println("Indicator Data of " + i + " = " + indicatordata[i]);
				String[] temp = indicatordata[i].split(",");
				String[] temp2 = temp[0].split("/");
				tempstring = temp2[2] + temp2[0] + temp2[1];
				datearray[i] = Integer.parseInt(tempstring);			
			}
		}
		
		if(indicatorcount==2)
			position = 2;
		
		for(int i=2;i<indicatorcount;i++) {
//			System.out.println(datetarget);
//			System.out.println(datearray[i]);
//			if(datetarget == 20160526) System.out.println("TRUE!!!!!!!!");
//			if(datearray[i] == 20160526) System.out.println("WTF??????");
			if(i==2 && datetarget > datearray[i]) {
				position = 2;
				i = indicatorcount;
			}
			if(datetarget == datearray[i]) {
				position = -1;
				i = indicatorcount;
			}
			if(i < indicatorcount-1 && datetarget < datearray[i] && datetarget > datearray[i+1]) {
				position = i;
				i = indicatorcount;
			}
		}
		System.out.println("Insert Postion = " + position);
		return position;
	}
	
	public static void loadCurrentIndicatorData() throws IOException {
		indicatorcount = 0;
		BufferedReader inputFile = new BufferedReader(new FileReader($INVEST + "eodindicators.csv"));
		String dataRow = inputFile.readLine();
		while (dataRow != null && indicatorcount < $EODARRAYSIZE) {
			indicatordata[indicatorcount] = dataRow;
//			System.out.println(indicatordata[indicatorcount]);
			indicatorcount++;
			dataRow = inputFile.readLine();
		}
		inputFile.close();
		
		for(int i=0;i<$EXCHANGECOUNT;i++) {
			for(int j=2;j<indicatorcount;j++) {
				String[] temp = indicatordata[j].split(",");
//				System.out.println(indicatordata[j]);
//				System.out.println(temp[(i*16)+11]);
				pasthilo[i][j] = Double.parseDouble(temp[(i*16)+11]);
//				System.out.println(temp[(i*15)+13]);
				pasttrin[i][j] = Double.parseDouble(temp[(i*16)+13]);
			}
		}
	}
	
	public static void printResults() {
		for(int i=0;i<$EXCHANGECOUNT;i++) {
			System.out.println();
			System.out.println();
			System.out.println("Looking at the exchange for " + exchangelist[i] + ":");
			System.out.println("\tThe advancing stocks count is --> " + advancecount[i]);
			System.out.println("\tThe declining stocks count is --> " + declinecount[i]);
			System.out.println("\tThe unchanged stocks count is --> " + unchangedcount[i]);
			System.out.println("\tThe the advancing stock volume is --> " + advancevolume[i]);
			System.out.println("\tThe declining stock volume is --> " + declinevolume[i]);
			System.out.println("\tThe unchanged stock volume is --> " + unchangedvolume[i]);
			System.out.println("\tThe trin is --> " + trin[i]);
			System.out.println("\tThe uncalculated trin 10 day moving average is --> " + trin10dma[i]);
			System.out.println("\tThe 52 week high count is --> " + yearhicount[i]);
			System.out.println("\tThe 52 week low count is --> " + yearlocount[i]);
			System.out.println("\tThe new high count is --> " + newhicount[i]);
			System.out.println("\tThe new low count is --> " + newlocount[i]);
			System.out.println("\tThe new highs versus new lows indicator value is --> " + hilo[i]);
			System.out.println("\tThe 10 day sum of the new highs versus new lows indicator is --> " + hilo10daycount[i]);
			System.out.println("\tThe number of stocks over their 200DMA is --> " + over200dmacount[i]);
			System.out.println("\tThe percent of stocks over their 200DMA is --> " + over200dmapercent[i]);
		}
	}
	
	public static void saveResults() throws IOException {
		
		outputstring = targetdate + ",";
		for(int i=0;i<$EXCHANGECOUNT;i++) {
			outputstring += advancecount[i] + ",";
			outputstring += declinecount[i] + ",";
			outputstring += unchangedcount[i] + ",";
			outputstring += advancevolume[i] + ",";
			outputstring += declinevolume[i] + ",";
			outputstring += unchangedvolume[i] + ",";
			outputstring += yearhicount[i] + ",";
			outputstring += yearlocount[i] + ",";
			outputstring += newhicount[i] + ",";
			outputstring += newlocount[i] + ",";
			outputstring += hilo[i] + ",";
			outputstring += hilo10daycount[i] + ",";
			outputstring += trin[i] + ",";
			outputstring += trin10dma[i] + ",";
			outputstring += over200dmacount[i] + ",";
			outputstring += over200dmapercent[i] + ",";
		}
		
		int counter = 0;
		System.out.println("Insert Position --> " + insertposition);
		
		if(insertposition > 0) {
			while(counter < insertposition) {
				indicatordata2[counter] = indicatordata[counter];
				counter++;
			}
			System.out.println("Output String --> " + outputstring);
			System.out.println("Counter --> " + counter);
			indicatordata2[counter] = outputstring;
			counter++;
			System.out.println("Counter = " + counter);
			
			while(counter < indicatorcount+1) {
				indicatordata2[counter] = indicatordata[counter-1];
				counter++;
			}
		}
		else {
			for(int i=0;i<indicatorcount;i++) {
				indicatordata2[i] = indicatordata[i];
			}
		}
		
		if(insertposition > 0) {
			indicatorcount++;
		}
		
	   	for(int i=0;i<indicatorcount;i++) {
//	   		System.out.println("I1 = " + indicatordata[i]);	   			
//	   		System.out.println("I2 = " + indicatordata2[i]);
	   	}
		
		String outputFileName = $INVEST + "tempfile.csv";
		File oldFile = new File(outputFileName);
		oldFile.delete();
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
	   	for(int i=0;i<indicatorcount;i++) {
//	   		System.out.println("I1 = " + indicatordata[i]);	   			
//	   		System.out.println("I2 = " + indicatordata2[i]);
			outputFile.write(indicatordata2[i]);
			outputFile.newLine();
	   	}	
		outputFile.close();
		
		File sourceFile = new File($INVEST + "tempfile.csv");
		File targetFile = new File( $INVEST + "eodindicators.csv");
		targetFile.delete();
		Files.copy(sourceFile.toPath(), targetFile.toPath());
		
		sourceFile = new File($INVEST + "tempfile.csv");
		targetFile = new File( $INVEST + "eodstatistics/" + year + "." + month + "." + day + "." + dayoweek + "." + "eodstatistics.csv");
		targetFile.delete();
		sourceFile.renameTo(targetFile);
	}
	
}
