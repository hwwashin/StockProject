package com.investmentstudios.stock;

public class Strategy_MMA extends Strategy_ETFTT {
	
	public static boolean isConvergingofMAs(int pos) {
		
		double range = .01 * cl[pos];
		double maxval = max(MA(4,pos), MA(18,pos), MA(40,pos));
		double minval = min(MA(4,pos), MA(18,pos), MA(40,pos));
		
		if(range >= maxval - minval) return true;
		else return false;
	}
	
	public static boolean isDelphicPhenomenonBuy(int pos) {
		
		int count = 30;
		int ma1 = 18;
		int ma2 = 40;
		
		boolean macross = false;
		int crosspointma = 0;
		
		boolean pricecross = false;
		int crosspointprice = 0;
		
		boolean pricebelowma = true;
//			System.out.println(dates[pos]);
// if(dates[pos].equals("9/26/2012") || dates[pos].equals("9/27/2012") || dates[pos].equals("9/28/2012")) {
//		System.out.println("ma18 = " + MA(ma1,pos) + "  ma40 = " + MA(ma2,pos) + "  low = " + low[pos]);
//}
		if(MA(ma1,pos) > MA(ma2,pos) && lo[pos] < MA(ma1,pos)) {
			for(int i=0;i<count;i++)
				if(MA(ma1,pos+i) < MA(ma2,pos+i)) {
					if(crosspointma == 0) {
						crosspointma = i;
						macross = true;
/*						//
						if(dates[pos].equals("9/26/2012") || dates[pos].equals("9/27/2012") || dates[pos].equals("9/28/2012")) {
							System.out.println("crosspointma = " + crosspointma + " on date " + dates[pos+i]);
						}
*/						//

					}
				}
			
			if(macross) {
				for(int i=crosspointma;i>=0;i--) {
					if(cl[pos+i] < MA(ma1,pos+i) && MA(ma1,pos+i) > MA(ma2,pos+i)) {
						if(crosspointprice == 0) {
							crosspointprice = i;
							pricecross = true;
/*							//
							if(dates[pos].equals("9/26/2012") || dates[pos].equals("9/27/2012") || dates[pos].equals("9/28/2012")) {
								System.out.println("crosspointprice = " + crosspointprice + " on date " + dates[pos+i]);
							}
*/							//

						}
					}
				}
			}
			else return false;
			
			if(pricecross) {
				for(int i=crosspointprice;i>=0;i--) {
					if(cl[pos+i] > MA(ma1,pos+i)) {
						pricebelowma = false;
					}
				}
			}
			else return false;
		}
		else return false;
		
		return pricebelowma;
		
		/*
		 * Looks like I've got the first pass working.  Just need to start checking the validity of the signals being reported - TDA & the csv
		 * 
		 * Working with A.csv right now - Yahoo file - not getting 9/27/2012 for some reason - still working through that
		 * 
		 * Delphic phenomenon - 18DMA crosses above the 40DMA, price crosses below the 18DMA, buy when price goes back above 18DMA
		 * 
		 * I've determined that the 18DMA does cross above the 40DMA - now I'm struggling with price action
		 * price can be below the 18DMA on day one - but it can never go back above - since at that point we are in the trade
		 * So I'm trying to figure out how to make that determination
		 * Maybe I figure out the earliest point that the price goes below the 18DMA (if it does)
		 * Then I make sure that price has stayed below the 18DMA since that price crossover
		 * That is the signal I want to give myself - once it crosses back over the 18DMA I should be in the trade not screening for it
		 * 
		 * I can also have a 2nd "DelphicBuy" where I look for the delphic signal the day before and then price to be above the 18DMA
		 */
	}

	public static boolean isDelphicPhenomenonSell(int pos) {
		
		int count = 30;
		int ma1 = 18;
		int ma2 = 40;
		
		boolean macross = false;
		int crosspointma = 0;
		
		boolean pricecross = false;
		int crosspointprice = 0;
		
		boolean priceabovema = true;

		if(MA(ma1,pos) < MA(ma2,pos) && hi[pos] > MA(ma1,pos)) {
			for(int i=0;i<count;i++)
				if(MA(ma1,pos+i) > MA(ma2,pos+i)) {
					if(crosspointma == 0) {
						crosspointma = i;
						macross = true;
					}
				}
			
			if(macross) {
				for(int i=crosspointma;i>=0;i--) {
					if(cl[pos+i] > MA(ma1,pos+i) && MA(ma1,pos+i) < MA(ma2,pos+i)) {
						if(crosspointprice == 0) {
							crosspointprice = i;
							pricecross = true;
						}
					}
				}
			}
			else return false;
			
			if(pricecross) {
				for(int i=crosspointprice;i>=0;i--) {
					if(cl[pos+i] < MA(ma1,pos+i)) {
						priceabovema = false;
					}
				}
			}
			else return false;
		}
		else return false;
		
		return priceabovema;
	}
	
	public static boolean isMAConvergenceBuy(int pos) {
		
		boolean converge = false;
		
		if(isDelphicPhenomenonBuy(pos)) {
			for(int i=35;i>0;i--) {
				if(isConvergingofMAs(pos+i) && !converge) {
					converge = true;
				}
			}
		}
		
		return converge;
		
		/* 
		 * Working on ma convergence now - this is something that is in addition to the delphic phenomenon
		 * somehow I want to detect whether or not there is convergence - in addition to detecting any kind of delphic phenomenon
		 * So maybe I want to check - I don't know man - I want to think about this some more - visually is it easy to see
		 * The 4, 18 & 40 DMAs all "touch" and then the price explodes - but how do I work this in with the delphic phenomenon?
		 * OK - first - dude - ugh - I'm thinking I want to check the moving averages - but that already happened man - that is in the past
		 * Maybe I check the moving averages for the last 30 days and see if they "touched" - then I'll check for the delphic phenomenon
		 
Code up magic of moving averages - system failure
Code up magic of moving averages - 40 to 18 bounce - I'm not feeling this one much - it depends and a fast moving market and a reversal - no
Code up magic of moving averages - Selling the second hump - this is the former darlings from the 1% strategy and I'm good with it

		 */
	}
	
	public static boolean isMAConvergenceSell(int pos) {
		
		boolean converge = false;
		
		if(isDelphicPhenomenonSell(pos)) {
			for(int i=35;i>0;i--) {
				if(isConvergingofMAs(pos+i) && !converge) {
					converge = true;
				}
			}
		}
		
		return converge;
	}
	
	public static boolean isSystemFailureBuy(int pos) {
		
		/* need to write an idDelphicPhenomenonSell function - that needs to happen for there to be a system failure buy signal */

		boolean maSellConvergence = false;
		
		for(int i=0;i<30;i++) {
			if(isMAConvergenceSell(pos+i)) {
				maSellConvergence = true;
			}
		}
		
		if(maSellConvergence && cl[pos] > MA(40,pos) && cl[pos+1] < MA(40,pos)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isSystemFailureSell(int pos) {
		
		/* need to write an idDelphicPhenomenonSell function - that needs to happen for there to be a system failure buy signal */

		boolean maBuyConvergence = false;
		
		for(int i=0;i<30;i++) {
			if(isMAConvergenceBuy(pos+i)) {
				maBuyConvergence = true;
			}
		}
		
		if(maBuyConvergence && cl[pos] < MA(40,pos) && cl[pos+1] > MA(40,pos)) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
