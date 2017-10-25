package com.investmentstudios.stock;

public class Indicators_PriceTests extends Indicators_Stock {

	public static double ATR(int loc, int count) {
		double atr = 0;
		for(int i=loc;i<loc+count;i++) {
			double highest = max(cl[i+1],hi[i]);
			double lowest = min(cl[i+1],lo[i]);
			atr += highest - lowest;
		}
		return atr / (double) count;
	}
	
	public static double avgVolume(int count, int pos) {
		double total = 0;
		for(int i=pos+1;i<count+pos+1;i++) {
//			System.out.println(vo[i]);
			total+=vo[i];
//			System.out.println(total + " --> " + vo[pos+i] + " --> " + date[pos+i] + " --> " + total/count);
		}
		return total/count;
	}
	
	public static double findCandleSize(int pos) {
		return hi[pos] - lo[pos];
	}
	
	public static double findCloseHigh(int pos, int count) {
		int counter = count+pos;		
		double hival = 0;
		if(pos+count-1>stockcount) {
			counter = stockcount;
		}
		for(int i=pos;i<counter;i++) {
			if(hival < cl[i]) {
				hival = cl[i];
			}
		}
		return hival;
	}

	public static double findCloseLow(int pos, int count) {
		int counter = count+pos;
		double lowval = 1000000000000000.0;
		if(pos+count-1>stockcount) {
			counter = stockcount;
		}
		for(int i=pos;i<counter;i++) {
			if(lowval > cl[i]) {
				lowval = cl[i];
			}
		}
		return lowval;
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
//		System.out.println("stockcount --> " + stockcount + "   current close --> " + cl[pos] + "   previous close --> " + cl[pos+1]);
		if(stockcount > 1) {
			percentchange = ((cl[pos] - cl[pos+1])/cl[pos+1])*100;
		}
		else {
			percentchange = 0;
		}
		
//		System.out.println(percentchange);
		return percentchange;
	}
	
	public static double fiveDayLow(int loc) {
		return min(lo[loc], lo[loc+1], lo[loc+2], lo[loc+3], lo[loc+4]);
	}
	
	public static double fourDayLow(int loc) {
		return min(lo[loc], lo[loc+1], lo[loc+2], lo[loc+3]);
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
		if(stockcount<20) test = false;
		if(cl[j] < op[j]) test = false;
		if(j+250>stockcount) time = stockcount-j-1;
		for(int k=j+1;k<j+time;k++) {
			if(cl[j] <= cl[k]) {
				test = false;
			}
			
		}
		return test;
	}

	public static boolean is52WeekLow(int j) {
		boolean test = true;
		int time = 250;
		if(j+250>stockcount) time = stockcount-j-1 ;
		if(stockcount<20) test = false;
		if(cl[j] > op[j]) test = false;
		for(int k=j+1;k<j+time;k++) {
//			System.out.println("cl[j] = " + cl[j] + " cl[k] = " + cl[k] + " op[k] = " + op[k] + " test -> " + test);
			if(cl[j] >= cl[k] || cl[j] >= op[k]) {
				test = false;
			}
		}
		return test;
	}
	
	public static boolean is52WeekLowOpen(int j) {
		boolean test = true;
		int time = 250;
		if(j+250>stockcount) time = stockcount-j-1;
		if(stockcount < 20) test = false;
		if(cl[j] > op[j]) test = false;
		for(int k=j+1;k<j+time;k++) {
			if(op[j] >= cl[k] || op[j] >= op[k]) {
				test = false;
			}
		}
		return test;		
	}
	
	public static boolean isClosedAboveLevel(int pos, int testlevel) {
		double height = hi[pos] - lo[pos];
		double closeratio = cl[pos] - lo[pos];
		double closelevel = closeratio / height;
		closelevel *= 100;
		
		if(closelevel < testlevel) return false;
		else return true;
	}
	
	public static boolean isClosedInUpperHalf(int j) {
		if(cl[j] < (((hi[j]-lo[j])/2)+lo[j])) return false;
		else return true;
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
	
	public static boolean isInsideDay(int pos) {
		boolean inside = true;
		if(hi[pos+1] < hi[pos]) inside = false;
		if(lo[pos+1] > lo[pos]) inside = false;
		return inside;
	}
	
	public static boolean isLifetimeHigh(int j) {
		boolean test = true;
		for(int k=j;k<stockcount;k++) {
			if(cl[j] < cl[k]) {
				test = false;
			}
		}
		return test;
	}
	
	public static boolean isLifetimeLow(int j) {
		boolean test = true;
		for(int k=j;k<stockcount;k++) {
			if(cl[j] > cl[k]) {
				test = false;
			}
		}
		return test;
	}
	
	public static boolean isLowerLows(int pos, int counter) {
		boolean result = true;
		for(int i=pos;i<counter+pos;i++) {
			if(lo[i] > lo[i+1]) {
				result = false;
			}
		}
		return result;
	}
	
	public static boolean isLowestLow(int pos, int counter) {
		boolean lowestlow = true;
		for(int i=pos-1;i<pos-counter-1;i--) {
			if(lo[pos] > lo[i]) lowestlow = false;
		}
		for(int i=pos+1;i>pos+counter+1;i++) {
			if(lo[pos] > lo[i]) lowestlow = false;
		}
		return lowestlow;
	}
	
	public static boolean isLowestLow(int pos, int leftcount, int rightcount) {
		boolean lowestlow = true;
		for(int i=pos-1;i<pos-rightcount-1;i--) {
			if(lo[pos] > lo[i]) lowestlow = false;
		}
		for(int i=pos+1;i>pos+leftcount+1;i++) {
			if(lo[pos] > lo[i]) lowestlow = false;
		}
		return lowestlow;
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
	
	public static int min(int var1, int var2) {
		if (var1 <= var2) return var1;
		else return var2;
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
	
	public static double min(double var1, double var2, double var3, double var4) {
		double min1 = min(var1, var2);
		double min2 = min(var3, var4);
		double min3 = min(min1, min2);
		return min3;
	}
	
	public static double min(double var1, double var2, double var3, double var4, double var5) {
		double min1 = min(var1, var2);
		double min2 = min(var3, var4);
		double min3 = min(min1, min2);
		double min4 = min(min3, var5);
		return min4;
	}
	
	public static double roundToTwo(double num) {
		return Math.round(num*100.0) / 100.0;
	}
	
	public static double threeDayLow(int loc) {
		return min(lo[loc], lo[loc+1], lo[loc+2]);
	}

	public static double twoDayLow(int loc) {
		return min(lo[loc], lo[loc+1]);
	}
	
	public static double tenDayATR(int loc) {
		return ATR(loc,10);
	}
	
}