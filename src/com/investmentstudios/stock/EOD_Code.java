package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EOD_Code extends Strategy_StrangeOptionActivity {
	
	public static int $EODARRAYSIZE = 500000;
	public static int $STOCKARRAYSIZE = 50000;
	
    public static String day;
	public static String month;
	public static String year;
	public static String dayoweek;
	
	public static int eodcount;
	public static int stockcount;
	
	public static String titlestring = new String();
	
	public static String amexPath = new String();;
	public static String nysePath = new String();;
	public static String nasdaqPath = new String();
	public static String indexPath = new String();;
	public static String otcbbPath = new String();;
	
	public static String[] eodstockinput = new String[$EODARRAYSIZE];
	public static String[] eoddate = new String[$EODARRAYSIZE];
	public static String[] eodopen = new String[$EODARRAYSIZE];
	public static String[] eodhigh = new String[$EODARRAYSIZE];
	public static String[] eodlow = new String[$EODARRAYSIZE];
	public static String[] eodclose = new String[$EODARRAYSIZE];
	public static String[] eodvolume = new String[$EODARRAYSIZE];
	public static String[] eodexchange = new String[$EODARRAYSIZE];
	public static String[] eodstockname = new String[$EODARRAYSIZE];
	public static String[] eodstockoutput = new String[$EODARRAYSIZE];
	public static String[] eodscreened = new String[$EODARRAYSIZE];
	public static String[] stockdata = new String[$EODARRAYSIZE];
	
	public static double[] eoddatenum = new double[$EODARRAYSIZE];
	public static double[] stockdatenum = new double[$EODARRAYSIZE];

	public static void buildEODScreenData() throws IOException {
		for(int i=0;i<eodcount;i++) {
			String[] dataArray = eodstockinput[i].split(",");
			eodstockname[i] = dataArray[0];
			eoddate[i] = createEODDate(eodstockinput[i], i);
			eodopen[i] = dataArray[2];
			eodhigh[i] = dataArray[3];
			eodlow[i] = dataArray[4];
			eodclose[i] = dataArray[5];
			eodvolume[i] = dataArray[6];
			eodexchange[i] = dataArray[7];
			if(eodstockname[i].equals("PRN")) {
				eodstockname[i] = "PRNPRN";
			}
		}		
		for(int i=0;i<eodcount;i++) {
			eodstockoutput[i] = eoddate[i] + "," + eodopen[i] + "," + eodhigh[i] + "," + eodlow[i] + "," + eodclose[i] + "," + eodvolume[i] + "," + "-" + "," + eodstockname[i] + "," + eodexchange[i] + "," + "0" + "," + "1";
		}
	}
	
	public static String createEODDate(String eodstockarray, int pos) {
		String[] dataArray = eodstockarray.split(",");
	    String[] dateparts = dataArray[1].split("\\s");
	    String stockday = dateparts[0];
		String stockmonth = dateparts[1];
		String stockyear = dateparts[2];
		    	
		if(stockmonth.equals("Jan")) {
			String month = "01";
			stockmonth = month;
		}
		else if(stockmonth.equals("Feb")) {
			String month = "02";
			stockmonth = month;
		}
		else if(stockmonth.equals("Mar")) {
			String month = "03";
			stockmonth = month;
		}
		else if(stockmonth.equals("Apr")) {
			String month = "04";
			stockmonth = month;
		}
		else if(stockmonth.equals("May")) {
			String month = "05";
			stockmonth = month;
		}
		else if(stockmonth.equals("Jun")) {
			String month = "06";
			stockmonth = month;
		}
		else if(stockmonth.equals("Jul")) {
			String month = "07";
			stockmonth = month;
		}
		else if(stockmonth.equals("Aug")) {
			String month = "08";
			stockmonth = month;
		}
		else if(stockmonth.equals("Sep")) {
			String month = "09";
			stockmonth = month;
		}
		else if(stockmonth.equals("Oct")) {
			String month = "10";
			stockmonth = month;
		}
		else if(stockmonth.equals("Nov")) {
			String month = "11";
			stockmonth = month;
		}
		else if(stockmonth.equals("Dec")) {
			String month = "12";
			stockmonth = month;
		}
		eoddatenum[pos] = convertDateToDouble(stockyear + stockmonth + stockday);
		return stockmonth + "/" + stockday + "/" + stockyear;
	}
	
	public static void findEODSymbolChanges() throws IOException {

		File stockDir1 = new File($INVEST + "eodscreens");
    	File[] stockNameArray = stockDir1.listFiles();
    	
//    	System.out.println("The length of the stock name array is: " + stockNameArray.length);
    	
    	int prevcount = 0;
    	int currcount = 0;
    	int changecount = 0;
    	int curr = stockNameArray.length-1;
    	int prev = stockNameArray.length-2;
    	
 //   	System.out.println("curr = " + curr + "  and prev = " + prev);
 //   	System.out.println("stocknamearray[prev] = " + stockNameArray[prev] + "  stocknamearray[curr] = " + stockNameArray[curr]);
    	
    	String dataRow;
    	String[] prevstocks = new String[20000];
    	String[] prevsource = new String[20000];
    	String[] currstocks = new String[20000];
    	String[] currsource = new String[20000];
    	String[] changestocks = new String[20000];
    	
	    BufferedReader prevstockfile = new BufferedReader(new FileReader(stockNameArray[prev]));
	    BufferedReader currstockfile = new BufferedReader(new FileReader(stockNameArray[curr]));
//		String filename1[i] = stockNameArray[i].toString().substring(prevstockfile.toString().length()+1);

	    prevstockfile.readLine();
	    dataRow = prevstockfile.readLine();
		while (dataRow != null && prevcount < 15000) {
			stockdata[prevcount] = dataRow;
			String[] dataArray = dataRow.split(",");
			prevstocks[prevcount] = dataArray[7];
			prevsource[prevcount] = dataArray[8];
			prevcount = prevcount + 1;
			dataRow = prevstockfile.readLine();
		}
		prevstockfile.close();
	
		currstockfile.readLine();
	    dataRow = currstockfile.readLine();
		while (dataRow != null && currcount < 15000) {
			stockdata[currcount] = dataRow;
			String[] dataArray = dataRow.split(",");
			currstocks[currcount] = dataArray[7];
			currsource[currcount] = dataArray[8];
			currcount = currcount + 1;
			dataRow = currstockfile.readLine();
		}
		currstockfile.close();
	
		for(int i=0;i<currcount;i++) {
			Boolean found = false;
			for(int j=0;j<prevcount;j++) {
				if(currstocks[i].equals(prevstocks[j])) {
					found = true;
					j=currcount;
				}
			}
			if(found==false) {
				changestocks[changecount] = "added," + currstocks[i] + "," + currsource[i];
				changecount++;
			}
		}
		
		for(int i=0;i<prevcount;i++) {
			Boolean found = false;
			for(int j=0;j<currcount;j++) {
				if(prevstocks[i].equals(currstocks[j])) {
					found = true;
					j=currcount;
				}
			}
			if(found==false) {
				changestocks[changecount] = "dropped," + prevstocks[i] + "," + prevsource[i];
				changecount++;
			}
		}
		
		String outputFileName =$INVEST + "tempfile.csv";
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
	   	
	   	for(int i=0;i<changecount;i++) {
			outputFile.write(changestocks[i]);
				outputFile.newLine();
	   	}
	   	
		outputFile.close();
		
		File sourceFile = new File($INVEST + "tempfile.csv");
		File targetFile = new File($INVEST + "eodsymbolchanges/" + year + "." + month + "." + day + "." + dayoweek + ".csv");
		targetFile.delete();
		sourceFile.renameTo(targetFile);
		
		for(int i=0;i<changecount;i++) {
			
		}
	}	
	
	public static int getDatePosition (String datestring) {
		boolean found = false;
		int counter = 0;
		int datepos = 0;
		while(found == false && counter < stockcount) {
			if(datestring.equals(date[counter])) {
				found = true;
				datepos = counter;
			}
			else counter++;
		}
		return datepos;
	}
	
	public static void loadData(String filename, String exchangeName) throws IOException {
		BufferedReader inputFile = new BufferedReader(new FileReader(filename));
		String dataRow = inputFile.readLine();
// System.out.println("Loading updated daily stock file data for " + exchangeName);
		while (dataRow != null && eodcount < 15000) {
			eodstockinput[eodcount] = dataRow + "," + exchangeName;
			eodcount++;
			dataRow = inputFile.readLine();
		}
		inputFile.close();
	}
	
	public static void readEODStockFile(String fname, String dirname, String titlestring, String firstline) throws IOException {
		
		String inputStockFile = "C:/Users/Chris Scott Miller/Documents/Investments/eoddatabase/" + fname + ".csv";

		if(new File(inputStockFile).isFile()) { 
//			System.out.println("Screening at stock " + stockName[i]);
		}
		else {
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(inputStockFile));
			outputFile.write(titlestring);
			outputFile.close();				
		}
		stockcount = 0;

		BufferedReader inputFile = new BufferedReader(new FileReader(inputStockFile));
		String datarow = inputFile.readLine();
		datarow = firstline;

		while (datarow != null && stockcount < 15000) {
			setEODStockData(datarow);
			stockcount++;
			datarow = inputFile.readLine();
		}
		inputFile.close();
	}
	
	public static void saveEODScreenData() throws IOException {
		// create new file and upload entriesNew[i] into this new file
		String outputFileName = $INVEST + "tempfile.csv";

		File oldFile = new File(outputFileName);
		oldFile.delete();

		BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
	   	outputFile.write(titlestring + "\r\n");
	   	for(int i=0;i<eodcount;i++) {
	   	    outputFile.write(eodscreened[i]);
//	   		outputFile.write(dailyData2[i]);
	   	   	outputFile.newLine();
	   	}
		outputFile.close();
		
		File sourceFile = new File($INVEST + "tempfile.csv");
		File targetFile = new File($INVEST + "eodscreens/" + year + "." + month + "." + day + "." + dayoweek + ".csv");
		targetFile.delete();
		sourceFile.renameTo(targetFile);
	}
	
	public static void setDayInfo(String args[], String dayvar, String monthvar, String yearvar, String dowvar) {
		if(args.length > 0) {
			day = args[0];
			month = args[1];
			year = args[2];
			dayoweek = args[3];
		}
		else {
			day = dayvar;
			month = monthvar;
			year = yearvar;
			dayoweek = dowvar;
		}
	}
	
	public static void setEODDirectories () {
		amexPath = $INVEST + "eoddailydownloads/AMEX/AMEX_" + year + month + day + ".csv";
		nysePath = $INVEST + "eoddailydownloads/NYSE/NYSE_" + year + month + day + ".csv";
  		nasdaqPath = $INVEST + "eoddailydownloads/NASDAQ/NASDAQ_" + year + month + day + ".csv";	
		indexPath = $INVEST + "eoddailydownloads//INDEX/INDEX_" + year + month + day + ".csv";
		otcbbPath = $INVEST + "eoddailydownloads/OTCBB/OTCBB_" + year + month + day + ".csv";
	}
	
	public static void setEODStockData(String stockdata2) {
		stockdata[stockcount] = stockdata2;
		
		String[] temp = stockdata2.split(",");
		
		date[stockcount] = convertDate(temp[0]);
		
		open[stockcount] = temp[1];
		high[stockcount] = temp[2];
		low[stockcount] = temp[3];
		close[stockcount] = temp[4];
		volume[stockcount] = temp[5];
		
		op[stockcount] = Double.parseDouble(temp[1]);
		hi[stockcount] = Double.parseDouble(temp[2]);
		lo[stockcount] = Double.parseDouble(temp[3]);
		cl[stockcount] = Double.parseDouble(temp[4]);
		vo[stockcount] = Double.parseDouble(temp[5]);
	}
	
	public static void sortEODScreenData(int numberofItems, String list[])
	{
		boolean exchangeMade;
		int pass = 0;
		String temp;
		exchangeMade = true;

		while((pass < numberofItems - 1) && exchangeMade == true) {
			exchangeMade = false;
			pass++;

			for(int index = 0; index < (numberofItems - pass); index++) {
				if(list[index].compareTo(list[index + 1]) > 0) {
					temp = list[index];
					list[index] = list[index + 1];
					list[index + 1] = temp;
					exchangeMade = true;
				}
			}
		}
	}
	
	public static void sortStockList(int numberofItems, String list[])
	{
		boolean exchangeMade;
		int pass = 0;
		String temp;
		exchangeMade = true;

		while((pass < numberofItems - 1) && exchangeMade == true) {
			exchangeMade = false;
			pass++;

			for(int index = 0; index < (numberofItems - pass); index++) {
				if(list[index].compareTo(list[index + 1]) > 0) {
					temp = list[index];
					list[index] = list[index + 1];
					list[index + 1] = temp;
					exchangeMade = true;
				}
			}
		}
	}
	
	public static void updateEODStockData() throws IOException {
		for (int i=0;i<eodcount;i++) {
//		for (int i=0;i<1;i++) {
			
			String inputStockFile = new String();
			if(i%2000 == 0) { System.out.println();System.out.print("Screening --> " + eodstockname[i]); }
			if(i%25 == 0) { System.out.print("."); }
//			System.out.println("Screening --> " + stockName[i]);
			
			inputStockFile = $INVEST + "eoddatabase/" + eodstockname[i] + ".csv";

			if(new File(inputStockFile).isFile()) { 
//				System.out.println("Screening at stock " + stockName[i]);
			}
			else {
				BufferedWriter outputFile = new BufferedWriter(new FileWriter(inputStockFile));
				outputFile.write(titlestring);
				outputFile.close();				
			}
			stockcount = 0;

			BufferedReader inputFile = new BufferedReader(new FileReader(inputStockFile));
			String dataRow = inputFile.readLine();
			dataRow = inputFile.readLine();

			while (dataRow != null && stockcount < 15000) {
//				System.out.println(dataRow);
				stockdata[stockcount] = dataRow;
				String[] tempstring = dataRow.split(",");
				stockdata[stockcount] = convertDate(tempstring[0]);
//				System.out.println(stockdata[stockcount]);
				String[] tempdate = stockdata[stockcount].split("/");
				String tempdate2 = tempdate[2] + tempdate[0] + tempdate[1];
				stockdatenum[stockcount] = convertDateToDouble(tempdate2);
//				System.out.println(date[i] + "   " + stockdatenum[lineCount2] + "   " + stockdata[lineCount2] + "   " + tempdate2 + "   " + eoddatenum[i]);
				stockcount++;
				dataRow = inputFile.readLine();
			}
			inputFile.close();
			
			for(int j=0;j<stockcount;j++) {
//				System.out.println(eoddatenum[i] + "   " + stockdatenum[j]);
			}
		
			int insertposition = -1;
			for(int j=0;j<stockcount;j++) {
				if(eoddatenum[i] == stockdatenum[j]) {
//					System.out.println(eoddatenum[i] + "  " + stockdatenum[j]);
//					System.out.println("THIS VALUE ALREADY EXISTS");
					j=stockcount;
				}
				
				else if(eoddatenum[i] > stockdatenum[j]) {
					insertposition = j;
					j=stockcount;
				}
			}
		
			// create new file and upload into this new file

			String outputFileName = $INVEST + "tempfile.csv";
			File oldFile = new File(outputFileName);
			oldFile.delete();
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
		   	outputFile.write(titlestring + "\r\n");
		   
//		   	System.out.println(insertposition);
		   	
		   	if(insertposition == -1) {		   	
			   	for(int j=0;j<stockcount;j++) {
					outputFile.write(stockdata[j]);
	   				outputFile.newLine();
			   	}
			}
			
			else {
				for(int j=0;j<stockcount;j++) {
					if(j==insertposition) {
					   	outputFile.write(eodstockoutput[i]);
					   	outputFile.newLine();
					}
					outputFile.write(stockdata[j]);
	   				outputFile.newLine();
				}
			}
		   		   	
			outputFile.close();
			
			File sourceFile = new File($INVEST + "tempfile.csv");
			File targetFile = new File( $INVEST + "eoddatabase/" + eodstockname[i] + ".csv");
			targetFile.delete();
			sourceFile.renameTo(targetFile);
			
		}
	}

}
