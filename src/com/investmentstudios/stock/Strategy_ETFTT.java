package com.investmentstudios.stock;

public class Strategy_ETFTT extends Indicators_StockSignals {

	public static int[] etftttrend = new int[$COUNT];
	public static double[] divergence = new double[$COUNT];
	
	public static double calculateDivergence(int pos) {

		int startpos;
		int counter = pos+3;
		double pdconst = 24.3;
		double diff9 = 0;
		double diff14 = 0;
		double pricediff = 0;
		double bardiff = 0;
		double startmid = 0;
		double curmid = 0;
		double slope1 = 0;
		double slope2 = 0;
		
		double channelhi = findCloseHigh(pos, 46);
		double channello = findCloseLow(pos, 46);
//		System.out.println("channelhi->" + channelhi + "  channello->" + channello);
		double channeldiff = channelhi - channello;
		
		if(isTouching3MAs(pos+1) || isTouching3MAs(pos+2)) {
//		if(isTouching3MAs(pos+1) || isTouching3MAs(pos+2) || isWithin3MAs(pos+1) || isWithin3MAs(pos+2)) {
			startpos = pos+2;
//			System.out.println("From position " + pos + " the next position touching the moving averages is " + startpos + " --> " + stockdata[pos]);
		}
		else {
			while(!isTouching3MAs(counter) && counter < stockcount) {
				counter++;
			}
			if(counter==stockcount) startpos = 2;
			else startpos = counter;
//			System.out.println("From position " + pos + " the next position touching the moving averages is " + startpos + " --> " + stockdata[pos]);
		}

		diff9 = (MA(9,pos) - MA(9,startpos));
		diff14 = (MA(14,pos) - MA(14,startpos));
		startmid = (hi[startpos] + lo[startpos]) / 2;
		curmid = (hi[pos] + lo[pos]) / 2;
		pricediff = curmid-startmid;
		bardiff = startpos - pos;
		
//		System.out.println("diff9->" + diff9 + "  diff14->" + diff14 + "  pricediff->" + pricediff + "  bardiff->" + bardiff + "  channeldiff->" + channeldiff);
		
		double anglebar = Math.atan((pdconst/bardiff)*(pricediff/channeldiff))*(180/Math.PI);
		double angle9ma = Math.atan((pdconst/bardiff)*(diff9/channeldiff))*(180/Math.PI);
		double angle14ma = Math.atan((pdconst/bardiff)*(diff14/channeldiff))*(180/Math.PI);
		double divangle = anglebar - (angle9ma + angle14ma)/2;
		divangle = Math.abs(divangle);
		
//		System.out.println("Date-> " + date[pos] + "  Price Angle->" + anglebar + "  9MA Angle->" + angle9ma + "  14MA Angle->" + angle14ma + "  Total Angle->" + divangle);
		
		return divangle;

//		System.out.println("startpos->" + startpos + "  pos->" + pos + "  pm1->" + pointma1 + "  pm2->" + pointma2 + "  pp1->" + pointprice1 + "  pp2->" + pointprice2 + "  s1->" + slope1 + "  s2->" + slope2);	
	}
	
	public static void calculateETFTrend() {
		
		for(int k=0;k<25000;k++) {
			etftttrend[k] = 0;
		}

		for(int k=stockcount;k>=0;k--) {
			if(stockcount - k < 100) {
				etftttrend[k]=0;
//					System.out.println("the date is --> " + dates[k] + "  and the trend value is --> " + trend[k]);
			}
			else if(etftttrend[k] == 1) {
				etftttrend[k] = 1;
				if(MA(100,k) < MA(100,k+1)) {
					for(int l=0;l<15;l++) {
						if(cl[k+l] < MA(100,k+1)) {
							etftttrend[k] = 0;
						}
					}
				}
			}
				
			else if(etftttrend[k+1] == -1) {
				etftttrend[k] = -1;
				if(MA(100,k) > MA(100,k+1)) {
					for(int l=0;l<15;l++) {
						if(cl[k+l] > MA(100,k+1)) {
							etftttrend[k] = 0;
						}
					}
				}				
			}
			
			else {
				int up = 1;
				int down = 1;
				for(int l=0;l<15;l++) {
					if(MA(100,k+l) < MA(100,k+l+1)) {
						up = 0;
					}
					if(MA(100,k+l) > MA(100,k+l+1)) {
						down = 0;
					}
					if(up == 1) etftttrend[k] = 1;
					else if(down == 1) etftttrend[k] = -1;
					else etftttrend[k] = 0;
				}
			}
		}
	}
	
	public static double determineAFC(double price) {
		double afc;
		if(price <= 25) afc = .05;
		else if(price <= 50) afc = .1;
		else if(price <= 100) afc = .15;
		else if(price <= 150) afc = .2;
		else if(price <= 200) afc = .25;
		else afc = .3;
		return afc;
	}
	
	public static String findPDExit(int signalloc, int profittargetstrategy, double greenpercentrisk, double graypercentrisk, double redpercentrisk, int trailingstopstrategy) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		
		int entryloc = signalloc-1;
		int loc = signalloc-2;
		double entryprice = 0;
		double exitprice = 0;
		double percentrisk = 0;
		double mxdrawdown = 0;
		boolean exited = false;
		String exitreason = new String();
		
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		
		double initialstoptarget = stoptarget;
		double profittarget = 0;
		
		if(profittargetstrategy == 0) profittarget = 0;
		else if(profittargetstrategy == 1) profittarget = entrytarget * 1.01;
		else if(profittargetstrategy == 2) profittarget = entrytarget * 1.02;
		else if(profittargetstrategy == 3) profittarget = entrytarget * 1.03;
		else if(profittargetstrategy == 4) profittarget = entrytarget * 1.04;
		else if(profittargetstrategy == 5) profittarget = entrytarget * 1.05;
		else if(profittargetstrategy == 6) profittarget = entrytarget * 1.06;
		else if(profittargetstrategy == 7) profittarget = entrytarget * 1.07;
		else if(profittargetstrategy == 8) profittarget = entrytarget * 1.08;
		else if(profittargetstrategy == 9) profittarget = entrytarget * 1.09;
		else if(profittargetstrategy == 10) profittarget = entrytarget * 1.10;
		else if(profittargetstrategy == 11) profittarget = entrytarget * 1.005;
		else if(profittargetstrategy == 12) profittarget = entrytarget * 1.015;
		else if(profittargetstrategy == 13) profittarget = entrytarget * 1.025;
		else if(profittargetstrategy == 14) profittarget = entrytarget * 1.035;
		else if(profittargetstrategy == 15) profittarget = entrytarget * 1.045;
		else if(profittargetstrategy == 16) profittarget = entrytarget * 1.055;
		else if(profittargetstrategy == 17) profittarget = entrytarget * 1.065;
		else if(profittargetstrategy == 18) profittarget = entrytarget * 1.075;
		else if(profittargetstrategy == 19) profittarget = entrytarget * 1.085;
		else if(profittargetstrategy == 20) profittarget = entrytarget * 1.095;
		else if(profittargetstrategy == 21) profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		else profittarget = 0;

		if(etftttrend[signalloc] > 0) {
			percentrisk = greenpercentrisk;
		}
		else if(etftttrend[signalloc] < 0){
			percentrisk = redpercentrisk;
		}
		else {
			percentrisk = graypercentrisk;
		}
//		System.out.println("HELLO WORLD    " + entryloc);
//		System.out.println(stockdata[entryloc] + "   entryloc-->" + entryloc);
		if(percentrisk == 0 || signalloc < 5) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;		
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		if(!exited) mxdrawdown = entryprice;
		
		while(!exited && loc >= 1) {
			double afc = determineAFC(lo[loc+1]);
			if(trailingstopstrategy == 0) afc = 0;
			else if (trailingstopstrategy == 1) stoptarget = lo[loc+1] - afc;
			else if (trailingstopstrategy == 2) stoptarget = twoDayLow(loc+1) - afc;
			else if (trailingstopstrategy == 3) stoptarget = threeDayLow(loc+1) - afc;
			else if (trailingstopstrategy == 4) stoptarget = fourDayLow(loc+1) - afc;
			else if (trailingstopstrategy == 5) stoptarget = fiveDayLow(loc+1) - afc;
			else if (trailingstopstrategy == 6) stoptarget = lo[loc+1] - tenDayATR(loc+1) - afc;
			else if (trailingstopstrategy == 7) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.25 - afc;
			else if (trailingstopstrategy == 8) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.5 - afc;
			else if (trailingstopstrategy == 9) stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.75 - afc;
			else if (trailingstopstrategy == 10) stoptarget = lo[loc+1] - tenDayATR(loc+1)*2.0 - afc;
			else if (trailingstopstrategy == 11) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)) - afc;
			else if (trailingstopstrategy == 12) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.25) - afc;
			else if (trailingstopstrategy == 13) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.5) - afc;
			else if (trailingstopstrategy == 14) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*1.75) - afc;
			else if (trailingstopstrategy == 15) stoptarget = max(threeDayLow(loc+1), lo[loc+1] - tenDayATR(loc+1)*2.0) - afc;
			else if (trailingstopstrategy == 16) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1) - afc);
			else if (trailingstopstrategy == 17) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.25 - afc);
			else if (trailingstopstrategy == 18) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5 - afc);
			else if (trailingstopstrategy == 19) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.75 - afc);
			else if (trailingstopstrategy == 20) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*2.0 - afc);
			else if (trailingstopstrategy == 21) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1));
			else if (trailingstopstrategy == 22) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.25);
			else if (trailingstopstrategy == 23) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5);
			else if (trailingstopstrategy == 24) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.75);
			else if (trailingstopstrategy == 25) stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*2.0);
			
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1));
			
			
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(profittarget > 0 && op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "profittarget";
			}
			else if(profittarget > 0 && op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "profittarget";
			}
			else {
				loc--;
			}
			
			if(!exited && mxdrawdown > lo[loc+1]) {
				mxdrawdown = lo[loc+1];
			}
		}
		
		 mxdrawdown = mxdrawdown - entryprice;
		
		if(signalloc<5) {
			signalloc = entryloc = loc = 1;
		}
		
		int duration = entryloc - loc;
//		System.out.println(signalloc + "--" + entryloc + "--" + loc);
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit001(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit002(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = lo[loc+1] - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit003(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit004(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = entrytarget * 1.03;
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit005(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = fourDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit006(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = fiveDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit007(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = twoDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit008(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = twoDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit009(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit010(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = fourDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit011(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = fiveDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit012(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - (determineAFC(lo[signalloc]*2));
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit013(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .02;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit014(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - (determineAFC(lo[signalloc]*2));
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .02;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit015(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit016(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .04;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit017(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .05;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit018(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .06;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit019(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .07;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit020(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .08;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit021(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .09;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit022(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .10;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit023(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] >= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = threeDayLow(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit024(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			stoptarget = max(threeDayLow(loc+1),lo[loc+1]-tenDayATR(loc+1)) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit025(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = lo[loc+1] - tenDayATR(loc+1) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit026(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = lo[loc+1] - tenDayATR(loc+1)*1.5 - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit027(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = lo[loc+1] - (tenDayATR(loc+1) * 2.0) - determineAFC(lo[loc+1]);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit028(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1));
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit029(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit030(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*2.0);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit031(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = hi[signalloc]*1.03;
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit032(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = hi[signalloc]*1.02;
		double exitprice = 0;
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit033(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = hi[signalloc]*1.01;
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit034(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = hi[signalloc]*1.005;
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .03;
		}

// System.out.println(entryloc);
// System.out.println(op[entryloc] + " --> " + entrytarget);
		
		if(op[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget && etftttrend[signalloc] > 0) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited) {
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
				exitreason = "stop";
			}
			else if(op[loc] > profittarget) {
				exitprice = op[loc];
				exited = true;
				exitreason = "channelexit";
			}
			else if(op[loc] < profittarget && hi[loc] > profittarget) {
				exitprice = profittarget;
				exited = true;
				exitreason = "channelexit";
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit035(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .02;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
	public static String findPDExit036(int signalloc) {
		
//		System.out.println("signalloc --> " + signalloc);
		
		calculateETFTrend();
		double entrytarget = hi[signalloc] + determineAFC(hi[signalloc]);
		int entryloc = signalloc-1;
		double entryprice = 0;
		
		boolean exited = false;
		int loc = signalloc-2;
		String exitreason = new String();
		double stoptarget = lo[signalloc] - determineAFC(lo[signalloc]);
		double initialstoptarget = stoptarget;
		double profittarget = ((findCloseHigh(signalloc,22) - findCloseLow(signalloc,22))*0.75) + findCloseLow(signalloc,22);
		double exitprice = 0;	
		double RRR = (profittarget-entrytarget) / (entrytarget-stoptarget);
		double percentrisk = 0;
		double mxdrawdown = 0;
		
		if(etftttrend[signalloc] <= 0) {
			percentrisk = .005;
		}
		else {
			percentrisk = .01;
		}
		
		if(etftttrend[signalloc] <= 0) {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		else if(op[entryloc] > entrytarget) {
			entryprice = op[entryloc];
		}
		else if(op[entryloc] < entrytarget && hi[entryloc] > entrytarget) {
			entryprice = entrytarget;
		}
		else {
			exited = true;
			exitreason = "notrade";
			exitprice = entryprice;
			loc = entryloc;
		}
		
		while(!exited && loc > 0) {
			stoptarget = max(stoptarget, lo[loc+1] - tenDayATR(loc+1)*1.5);
			if(op[loc] < stoptarget) {
				exitprice = op[loc];
				exited = true;
			}
			else if(op[loc] > stoptarget && lo[loc] < stoptarget) {
				exitprice = stoptarget;
				exited = true;
			}
			else {
				loc--;
			}
			if(!exited && mxdrawdown < entryprice - lo[loc+1]) {
				mxdrawdown = entryprice - lo[loc+1];
			}
		}
		
		if(exitprice < entryprice) exitreason = "stop";
		else if(loc == entryloc) exitreason = "notrade";
		else exitreason = "trailingstop";
		
		int duration = entryloc - loc;
		return date[signalloc] + "," + date[entryloc] + "," + date[loc] + "," + exitreason + "," + duration + "," + etftttrend[signalloc] + "," + percentrisk + "," + entryprice + "," + initialstoptarget + "," + stoptarget + "," + profittarget + "," + exitprice + "," + mxdrawdown;
	}
	
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
	
	public static Boolean isLessThanThreeMAs(int pos) {
		if(hi[pos] > MA(14,pos) || hi[pos] > MA(9,pos) || hi[pos] > MA(6,pos)) return false;
		else return true;
	}
	
	public static boolean isPDBuy001(int pos) {
//		System.out.println("date is:" + date[pos] + "  high1 is:" + high[pos] + "  low1 is:" + low[pos] + "  low2 is:" + low[pos+1] + "  low3 is:" + low[pos+2] + "  close is:" + close[pos] + "  midpoint is:" + ((hi[pos]-lo[pos])/2)+low[pos] + "  ma13 is:" + ma(13,pos) + "  ma8 is:" + ma(8,pos) + "  ma5 is:" + ma(5,pos));
		if(high[pos] == low[pos]) return false;
		if(!isThreeLowerLows(pos)) return false;
		if(!isClosedInUpperHalf(pos)) return false;
		if(!isLessThanThreeMAs(pos)) return false;
		
		divergence[pos] = calculateDivergence(pos);
		if(divergence[pos] < 40) return false;

		double entrytarget = hi[pos] + determineAFC(hi[pos]);
		double target = ((findCloseHigh(pos,22) - findCloseLow(pos,22))*0.75) + findCloseLow(pos,22);
	    double stop = lo[pos] - determineAFC(lo[pos]);
		double rrr = (target-entrytarget) / (entrytarget-stop);
		if(rrr<0.5) return false;
		
		return true;
	}
	
	public static boolean isPDBuy002(int pos) {
//		System.out.println("date is:" + date[pos] + "  high1 is:" + high[pos] + "  low1 is:" + low[pos] + "  low2 is:" + low[pos+1] + "  low3 is:" + low[pos+2] + "  close is:" + close[pos] + "  midpoint is:" + ((hi[pos]-lo[pos])/2)+low[pos] + "  ma13 is:" + ma(13,pos) + "  ma8 is:" + ma(8,pos) + "  ma5 is:" + ma(5,pos));
		if(high[pos] == low[pos]) return false;
		if(!isThreeLowerLows(pos)) return false;
		if(!isClosedInUpperHalf(pos)) return false;
		if(!isLessThanThreeMAs(pos)) return false;
		
		divergence[pos] = calculateDivergence(pos);
		if(divergence[pos] < 15) return false;

		double entrytarget = hi[pos] + determineAFC(hi[pos]);
		double target = ((findCloseHigh(pos,22) - findCloseLow(pos,22))*0.75) + findCloseLow(pos,22);
	    double stop = lo[pos] - determineAFC(lo[pos]);
		double rrr = (target-entrytarget) / (entrytarget-stop);
		if(rrr<0.5) return false;;
	    
		return true;
	}
	
	public static boolean isPDBuy003(int pos) {
//		System.out.println("date is:" + date[pos] + "  high1 is:" + high[pos] + "  low1 is:" + low[pos] + "  low2 is:" + low[pos+1] + "  low3 is:" + low[pos+2] + "  close is:" + close[pos] + "  midpoint is:" + ((hi[pos]-lo[pos])/2)+low[pos] + "  ma13 is:" + ma(13,pos) + "  ma8 is:" + ma(8,pos) + "  ma5 is:" + ma(5,pos));
		if(high[pos] == low[pos]) return false;
		if(!isThreeLowerLows(pos)) return false;
		if(!isClosedInUpperHalf(pos)) return false;
//		if(!isLessThanThreeMAs(pos)) return false;
		
		divergence[pos] = calculateDivergence(pos);
		if(divergence[pos] < 15) return false;

		double entrytarget = hi[pos] + determineAFC(hi[pos]);
		double target = ((findCloseHigh(pos,22) - findCloseLow(pos,22))*0.75) + findCloseLow(pos,22);
	    double stop = lo[pos] - determineAFC(lo[pos]);
		double rrr = (target-entrytarget) / (entrytarget-stop);
		if(rrr<0.5) return false;;
	    
		return true;
	}
	

	public static boolean isPDBuyMain(int pos, int candlesize, int bodysize, int lowerlows, int percentcloselevel, int below3mas, double divergencevalue1, double divergencevalue2, int channellocation, double rrrvalue) {
		if(candlesize == 0) if(findCandleSize(pos) == 0) return false;		
		if(!isClosedAboveLevel(pos, percentcloselevel)) return false;		
		if(below3mas == 1) if(!isLessThanThreeMAs(pos)) return false;
		if(!isInsideDay(pos)) if(!isLowerLows(pos,lowerlows)) return false;
		if(isInsideDay(pos) && !isPDBuyMain(pos+1, candlesize, bodysize, lowerlows, percentcloselevel, below3mas, divergencevalue1, divergencevalue2, channellocation, rrrvalue))
			return false;
		divergence[pos] = calculateDivergence(pos);		
		if(divergence[pos] < divergencevalue1 && divergencevalue2 == 0.0) return false;
		else if((divergence[pos] < divergencevalue1 || divergence[pos] > divergencevalue2) && divergencevalue2 != 0.0) return false;

		double entrytarget = hi[pos] + determineAFC(hi[pos]);
		double target = ((findCloseHigh(pos,22) - findCloseLow(pos,22))*0.75) + findCloseLow(pos,22);
	    double stop = lo[pos] - determineAFC(lo[pos]);
		double rrr = (target-entrytarget) / (entrytarget-stop);
		if(rrr < rrrvalue) return false;;
	    
		return true;
	}
	
	public static Boolean isThreeLowerLows(int pos) {
		if(lo[pos]>lo[pos+1] && hi[pos]<hi[pos+1]) {
			if(lo[pos+1] > lo[pos+2] || lo[pos+2] > lo[pos+3]) return false;
		}
		else if(lo[pos] > lo[pos+1] || lo[pos+1] > lo[pos+2]) return false;
	
		return true;	
	}
	
	public static boolean isTouching3MAs(int pos) {
		
		double ma6 = MA(6,pos);
		double ma9 = MA(9,pos);
		double ma14 = MA(14,pos);
		
//		System.out.println("POS->" + pos + "  MA6->" + ma6 + "  MA9->" + ma9 + "  MA14->" + ma14 + "  HI->" + hi[pos] + "  LO->" + lo[pos]);
		
		if((hi[pos] > ma6 && lo[pos] < ma6) || (hi[pos] > ma9 && lo[pos] < ma9) || (hi[pos] > ma14 && lo[pos] < ma14)) {
			return true;
		}
		else {
			return false;
		}
	}

}
