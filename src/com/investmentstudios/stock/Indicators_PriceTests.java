package com.investmentstudios.stock;

public class Indicators_PriceTests extends Indicators_Stock {

	public static double avgVolume(int count, int pos) {
		double total = 0;
		for(int i=1;i<count+1;i++) {
			total+=vo[pos+i];
//			System.out.println(total + " --> " + vo[pos+i] + " --> " + date[pos+i] + " --> " + total/count);
		}
		return total/count;
	}
	
	public static int findLast52WeekHigh(int j) {
		boolean found = false;
		int position52 = stockcount;
		int k = j+1;
		while(!found) {
			if(is52WeekHigh(k)) {
				found = true;
				position52 = k;
			}
			else {
				k++;
			}
		}
		return position52;
	}
	
	public static double findPercentChange(int pos) {
		double percentchange;
		
		if(stockcount > 1) {
			percentchange = ((cl[pos] - cl[pos+1])/cl[pos+1])*100;
		}
		else {
			percentchange = 0;
		}
		return percentchange;
	}
	
	public static boolean is30Day52WeekHigh(int j) {
		boolean test = true;
		for(int k=j+1;k<j+31;k++) {
			if(is52WeekHigh(k)) {
				test = false;
			}
		}
//		if(test) System.out.println(stockdata[j]);
		return test;
	}
	
	public static boolean is52WeekHigh(int j) {
		boolean test = true;
		int time = 250;
		if(j+250>stockcount) time = stockcount-j-1;
		for(int k=j;k<j+time;k++) {
			if(cl[j] < cl[k]) {
				test = false;
			}
		}
		return test;
	}

	public static boolean is52WeekLow(int j) {
		boolean test = true;
		int time = 250;
		if(j+250>stockcount) time = stockcount-j-1;
		for(int k=j;k<j+time;k++) {
			if(cl[j] > cl[k] || cl[j] > op[k]) {
				test = false;
			}
		}
		return test;
	}
	
	public static boolean is52WeekLowOpen(int j) {
		boolean test = true;
		int time = 250;
		if(j+250>stockcount) time = stockcount-j-1;
		for(int k=j;k<j+time;k++) {
			if(op[j] > cl[k] || op[j] > op[k]) {
				test = false;
			}
		}
		return test;		
	}
	
	public static boolean isGapDown(int j) {
		if(op[j] < lo[j+1] && op[j] < hi[j+1]) return true;
		else return false;
	}
	
	public static boolean isHigh(int count, int pos) {
		double highVal = 0;

		for(int i=0;i<count;i++) {
			if(hi[pos+i+1] > highVal) highVal = hi[pos+i+1];
		} 
		
		if(hi[pos] >= highVal) return true;
		else return false;
	}
	
	public static double max(double var1, double var2) {
		if (var1 >= var2) return var1;
		else return var2;
	}
	
	public static double max(double var1, double var2, double var3) {
		double max1 = max(var1,var2);
		double max2 = max(var3,max1);
		return max2;
	}
	
	public static double min(double var1, double var2) {
		if (var1 <= var2) return var1;
		else return var2;
	}
	
	public static double min(double var1, double var2, double var3) {
		double min1 = min(var1,var2);
		double min2 = min(var3,min1);
		return min2;
	}
}