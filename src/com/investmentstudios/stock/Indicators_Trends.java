package com.investmentstudios.stock;

public class Indicators_Trends extends Indicators_PriceTests {

	public static double MA(int count, int pos) {
		double mavg = 0;
		double counter;
		if(pos+count > stockcount) counter = stockcount - pos;
		else counter = count;
		for(int k=pos;k<pos+counter && k<stockcount;k++) {
			mavg+=cl[k];
		}
		mavg /= counter;
		
		return mavg;
	}
}
