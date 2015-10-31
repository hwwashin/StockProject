package com.investmentstudios.stock;

public class Strategy_ETFTT extends Indicators_StockSignals {

	public static int[] etftttrend = new int[$COUNT];
	
	public static boolean is5BPBreakoutBuy(int pos) {

		if( ( etftttrend[pos] == 1 || etftttrend[pos] == 0 ) && isHigh(100,pos) && !isHigh(99,pos+1) && !isHigh(98,pos+2)) {
//				System.out.println(dates[pos] + " , " + isHigh(100,pos) + " , " + isHigh(99,pos+1) + " , " + isHigh(98,pos+2));
			return true;
		}
		else return false;
/* 
 * Find the high from the last 100 days (3-4 months)
 * 
 * 
 */
	
	}
	
	public static boolean isPDBuy(int pos) {
//		System.out.println("date is:" + dates[pos] + "  high1 is:" + high[pos] + "  low1 is:" + low[pos] + "  low2 is:" + low[pos+1] + "  low3 is:" + low[pos+2] + "  close is:" + close[pos] + "  midpoint is:" + ((high[pos]-low[pos])/2)+low[pos] + "  ma13 is:" + ma(13,pos) + "  ma8 is:" + ma(8,pos) + "  ma5 is:" + ma(5,pos));
		if(high[pos] == low[pos]) return false;
		if(lo[pos] > lo[pos+1] || lo[pos+1] > lo[pos+2]) return false;
//		if( not sure how to measure the angles yet - going to leave this out for now - this is rule 2)
		if(cl[pos] < (((hi[pos]-lo[pos])/2)+lo[pos])) return false;
		if(hi[pos] > MA(13,pos) || hi[pos] > MA(8,pos) || hi[pos] > MA(5,pos)) return false;
		return true;
	}

}
