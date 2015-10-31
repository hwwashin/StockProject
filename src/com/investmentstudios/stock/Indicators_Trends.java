package com.investmentstudios.stock;

public class Indicators_Trends extends Indicators_PriceTests {

	public static double MA(int count, int pos) {
		double mavg = 0;
		for(int k=pos;k<pos+count && k<stockcount;k++) {
			mavg+=cl[k];
		}
		if(pos+count<stockcount) mavg /= count;
		else mavg /= stockcount-pos;
		
		return mavg;
	}
}
