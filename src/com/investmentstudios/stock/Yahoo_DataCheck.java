package com.investmentstudios.stock;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Yahoo_DataCheck extends Yahoo_InitialDownload{
	
	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		String yahooyear = "2016";
		String yahoomonth = "06";
		String yahooday = "24";
		
		setInvestmentDirectory();
//		$YAHOOEOD = yahooyear + yahoomonth + yahooday;
		$DATEAPPEND = yahooyear + "." + yahoomonth + "." + yahooday;
		setEODDirs();

		yahoostockdir = new File($YAHOODB);
    	yahoostocknamearray = yahoostockdir.listFiles();
    	
		getYahooZeroData();
		
		System.out.println();
		System.out.println();
		getYahooNullData();
		
		System.out.println();
		System.out.println();
		getYahooCrazyCloseData();
		
		System.out.println();
		System.out.println();
		getYahooAdjustedCloseData();
		
		saveYahooZeroData();
		saveYahooNullData();
		saveYahooCrazyCloseData();
		saveYahooAdjustedCloseData();
		
		System.out.println();
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
