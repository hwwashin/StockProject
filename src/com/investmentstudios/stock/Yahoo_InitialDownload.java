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

public class Yahoo_InitialDownload extends Yahoo_Code {

	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		setInvestmentDirectory();
		
		yahooyear = "2016";
		yahoomonth = "07";
		yahooday = "29";
		yahoodayoweek = "Friday";
		
		$YAHOOEOD = yahooyear + yahoomonth + yahooday;
		$DATEAPPEND = yahooyear + "." + yahoomonth + "." + yahooday;
		
		setDayInfo(args, yahooday, yahoomonth, yahooyear, yahoodayoweek);  // Day  Month  Year  DayOWeek
		setEODDirectories();
		
		System.out.println(amexPath);
		
		loadData(amexPath, "amex"); /* populate the individual arrays here - including the exchange */
		loadData(nysePath, "nyse");
		loadData(nasdaqPath, "nasdaq");
		
		sortEODScreenData(eodcount, eodstockinput);
		
		setYahooDirs();
		
		System.out.println();
				
		for(int i=0;i<eodcount;i++) {
			String[] tempyahoo = eodstockinput[i].split(",");
			stockarray[i] = tempyahoo[0];
		}
		stockcount = eodcount;
		
		downloadYahooData();
		
		yahoostockdir = new File($YAHOODB);
    	yahoostocknamearray = yahoostockdir.listFiles();
		
		getYahooZeroData();
		getYahooNullData();
		getYahooCrazyCloseData();
		getYahooAdjustedCloseData();
		
		saveYahooZeroData();
		saveYahooNullData();
		saveYahooCrazyCloseData();
		saveYahooAdjustedCloseData();
   	
    	String outputfilename = $YAHOOFAILEDDOWNLOADS;
    	BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
	   	for(int i=0;i<stockfailcount;i++) {
	   		outputFile.write(stockfailarray[i]);
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
