package com.investmentstudios.stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tester extends Backtest_Code {

	public Tester() {	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader dataFile = new BufferedReader(new FileReader("C:/Users/Willis/Documents/Investments/backtest/etftt_pdbuy/etftt_pdbuy.csv"));
		stockfilename = "USO.csv";
		
		stockcount = 0;
		stockdata[stockcount] = dataFile.readLine();
		String datarow = dataFile.readLine();
		while (datarow != null) {
			System.out.println(stockcount + " --> " + stockfilename + " --> " + datarow);
			stockdata[stockcount] = datarow;
			setStockData();
			stockcount++;
			datarow = dataFile.readLine();
		}	
		dataFile.close();
	}
}
