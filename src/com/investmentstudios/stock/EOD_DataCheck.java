package com.investmentstudios.stock;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EOD_DataCheck extends EOD_Code {

	public EOD_DataCheck() { }

	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		String eodyear = "2016";
		String eodmonth = "06";
		String eodday = "24";
		
		setInvestmentDirectory();

		$EODDATEAPPEND = eodyear + "." + eodmonth + "." + eodday;
		setEODDirs();

		eodstockdir = new File($EODDB);
    	eodstocknamearray = eodstockdir.listFiles();
    	
		getEODZeroData();
		
		System.out.println();
		System.out.println();
		getEODNullData();
		
		System.out.println();
		System.out.println();
		getEODCrazyCloseData();

		saveEODZeroData();
		saveEODNullData();
		saveEODCrazyCloseData();
		
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
