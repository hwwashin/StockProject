package com.investmentstudios.stock;

public class Strategy_OnePercent extends Strategy_MMA {
	
	public static boolean isDemiseSunrise(int j) {
		boolean test = true;
		
		if(!is52WeekLow(j)) test = false;
		if(!test || !is52WeekLowOpen(j)) test = false;
		if(!test || !isGapDown(j)) test = false;
		if(!test || !(vo[j] > avgVolume(j,20)*1.5)) test = false;
		
		return test;
	}
	
	public static boolean isDouble200DMA(int j) {
		int time1 = 50;
		int time2 = 200;
		if(j+time1>stockcount) time1 = stockcount-j-1;
		if(j+time2>stockcount) time2 = stockcount-j-1;
		for(int k=j;k<j+time1;k++) {
			if(cl[k] > MA(k,time2)*2) {
				return true;
			}
		}		
		return false;
	}

	public static boolean isFormerDarling(int j) {
		boolean test = true;
		
		if(!(stockcount>51)) test = false;
		if(!test || op[j] < MA(j,50)) test = false;
		if(!test || cl[j] > MA(j,50)) test = false;
		if(!test || op[j+1] < MA(j+1,50)) test = false;
		if(!test || cl[j+1] < MA(j+1,50)) test = false;
		if(!test || !(vo[j] > avgVolume(j,20)*1.5)) test = false;
		
		if(!isDouble200DMA(j)) test = false;

		return test;
	}
	
	public static boolean is50MARetrace(int j) {
		boolean test = false;
		boolean goodstart = true;
		int prev52 = findLast52WeekHigh(j);
		int k = prev52;
		double first50 = MA(prev52,50);
		double first100 = MA(prev52,100);
		
		if(first50<first100) goodstart = false;
		
		while(k>j && !test && goodstart) {
			double next50 = MA(k,50);
			double next100 = MA(k,100);
			if(next50 < next100) {
				test = true;
			}
			k--;
		}
		
		boolean finaltest;
		if(test && goodstart) finaltest = true;
		else finaltest = false;
		return finaltest;
	}
	
	public static boolean isIdealSetup(int j) {
		boolean test = true;
		
		double dma50 = MA(j,50);
		double dma100 = MA(j,100);
		double dma200 = MA(j,200);
		
		double dma50a = MA(j+1,50);
		double dma100a = MA(j+1,100);
		double dma200a = MA(j+1,200);
		
		if(!is52WeekHigh(j)) test = false;
		if(!test || !is30Day52WeekHigh(j)) test = false;
		
		if(!test || !is50MARetrace(j)) test = false;
		
		if(!test || cl[j]<dma50 || dma50<dma100 || dma100<dma200) test = false;
		if(!test || dma50<dma50a || dma100<dma100a || dma200<dma200a) test = false;
		
		return test;
				
		/*  need a gap up on higher volume on the breakout day or during the retracement  */
	}
	
	public static boolean isIdealSetup001(int j) {
		boolean test = true;
		
		double dma50 = MA(j,50);
		double dma100 = MA(j,100);
		double dma200 = MA(j,200);
		
		double dma50a = MA(j+1,50);
		double dma100a = MA(j+1,100);
		double dma200a = MA(j+1,200);
		
		if(!is52WeekHigh(j)) test = false;
		if(!test || !is30Day52WeekHigh(j)) test = false;
		
		if(!test || !is50MARetrace(j)) test = false;
		
		if(!test || cl[j]<dma50 || dma50<dma100 || dma100<dma200) test = false;
		if(!test || dma50<dma50a || dma100<dma100a || dma200<dma200a) test = false;
		
		return test;
				
		/*  need a gap up on higher volume on the breakout day or during the retracement  */
	}
	
	public static boolean isIPO() {
		if (stockcount > 500) return false;
		else return true;
	}
	
	public static boolean isIPOIdealSetup(int pos) {
		
		int count = 0;
		count = (int) min(stockcount,25);
		
		if(isHigh(count, pos) && op[pos] > hi[pos+1] && vo[pos] > avgVolume(10,pos+1) && isIPO()) {
			return true;
		}
		else return false;
		
/*
 * 1) new high after at least some retracement, 
 * 2) heavy volume for new high - 
 * IPOs are tracked for 2 yrs then become normal stocks - anything with less than 500 lines of data is considered an IPO
 */
	
	}
	
}
