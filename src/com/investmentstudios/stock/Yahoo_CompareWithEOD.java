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

public class Yahoo_CompareWithEOD extends Yahoo_MergeYahooAndEOD {

	public Yahoo_CompareWithEOD() { }

	public static String $EODDB;
	public static int $YAHOOCOUNT = 50000;
	
	public static int yahoofilecount = 0;
	public static int mergecount = 0;
	
	public static String targetdate = new String();
	public static String filedate = new String();
	public static String titleline = new String();
	
	public static String[] yahoostockdata = new String[$YAHOOCOUNT];
	public static String[] mergedata = new String[$YAHOOCOUNT];
	public static String[] datelist = new String[$YAHOOCOUNT];
	
	public static void main(String[] args) throws IOException {
		
		displayTime();
		
		setInvestmentDirectory();
		
		yahooyear = "2016";
		yahoomonth = "06";
		yahooday = "24";
		yahoodayoweek = "Friday";
		
		$YAHOOEOD = yahooyear + yahoomonth + yahooday;
		$DATEAPPEND = yahooyear + "." + yahoomonth + "." + yahooday;
		targetdate = yahoomonth + "/" + yahooday + "/" + yahooyear;
		
		titleline = "open diff,high diff,low diff,close diff,vol diff,stock,eod date,eod open,eod high,eod low,eod close,eod volume,exchange,yahoo date,yahoo open,yahoo high,yahoo low,yahoo close,yahoo volume,yahoo adj close";
		
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
			setEODStockData3(eodstockinput[i], i);
//			System.out.println(eodhi[i]);
//			System.out.println(tempyahoo[1]);
//			datelist[i] = createEODDate(eodstockinput[i], i);
//			System.out.println(stockarray[i]);
//			System.out.println(eodstockinput[i]);
//			System.out.println(datelist[i]);
		}
		
		System.out.println("About to download the yahoo data");
//		downloadYahooData();
	
		
		for(int i=0;i<eodcount;i++) {
//			System.out.println($YAHOODB);
//			System.out.println(stockarray[i]);
//			System.out.println($YAHOODB + stockarray[i] + ".csv");
			String yahoofilename = $YAHOODB + stockarray[i] + ".csv";
			if(i%2000==0) { 
				if(i>0) {System.out.println();System.out.print(i + " " + stockarray[i]); }
				else {System.out.println();System.out.print("0000 " + stockarray[i]);}
			}
			if(i%25==0) System.out.print(".");
			if(new File(yahoofilename).isFile()) {
		   		BufferedReader datafile = new BufferedReader(new FileReader(yahoofilename));
	//			stockfilename = yahoostocknamearray[i].toString().substring(yahoostockdir.toString().length()+1);		
//				System.out.println(stockfilename);
				
				stockcount = 0;
				stockdata[stockcount] = datafile.readLine();
				String datarow = datafile.readLine();
				while (datarow != null) {
//	System.out.println(stockarray[i] + "," + datarow);
					stockdata[stockcount] = datarow;
					setYahooStockData();
					stockcount++;
					datarow = datafile.readLine();
				}	
				datafile.close();
				for(int j=0;j<stockcount;j++) {
//					System.out.println(date[j] + "  -->  " + targetdate);
					if(date[j].equals(targetdate)) {
//						System.out.println(eodstockinput[i] + "," + stockdata[j]);
						double mergeop = eodop[i] - op[j];
						double mergehi = eodhi[i] - hi[j];
						double mergelo = eodlo[i] - lo[j];
						double mergecl = eodcl[i] - cl[j];
						double mergevo = eodvo[i] - vo[j];
						String diffdata = mergeop + "," + mergehi + "," + mergelo + "," + mergecl + "," + mergevo;
//						System.out.println(diffdata);
						mergedata[mergecount] = diffdata + "," + eodstockinput[i] + "," + stockdata[j];
						mergecount++;
					}
				}
			}
		}
		
		for(int i=0;i<mergecount;i++) {
			
		}

    	String outputfilename = $INVEST + $DATEAPPEND + ".mergedata.csv";
    	BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputfilename));
    	outputFile.write(titleline);
    	outputFile.newLine();
	   	for(int i=0;i<mergecount;i++) {
//	   		System.out.println(mergedata[i]);
	   		outputFile.write(mergedata[i]);
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
