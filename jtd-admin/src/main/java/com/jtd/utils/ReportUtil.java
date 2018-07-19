package com.jtd.utils;

public class ReportUtil {
	
	public static double getClickRate(int click,int pv){
		if(click != 0 && pv != 0){
			return (click / (float)pv) * 100 ;
		}
		return 0 ;
	}
	
	public static int getExpend(int price, int pv, int click, int expendType) {
		
		int expend = 0;
		
		if (expendType == 0) {
			if(price != 0 && pv != 0){
				expend = (int)(price * (pv / 1000f)); // cpm即出价*千次展示
			}
			
		} else if (expendType == 1) {
			if(price != 0 && click != 0){
				expend = price * click; // cpc即出价*点击数
			}
		}
		
		return expend;
	}

	public static int getCost(int expend, double grossProfitRatio) {

		if(expend != 0 && grossProfitRatio != 0){
			return (int) (expend * (1 - grossProfitRatio));
		}
		return 0 ;
	}
	
	public static double getYuan(long fenAmount){
		if(fenAmount != 0){
			return fenAmount / 100f ;
		}
		return 0 ;
	}

	public static double getCpmSingleYuan(int costOrExpendFen,int pv){
		if(costOrExpendFen != 0 && pv != 0){
			return costOrExpendFen / ((float)pv/1000) / 100f ;
		}
		return 0 ;
	}
	
	public static double getCpcSingleYuan(int costOrExpendFen,int click){
		if(costOrExpendFen != 0 && click != 0){
			return costOrExpendFen / (float)click / 100f ;
		}
		return 0 ;
	}
	
	public static double getCpmSingleYuan(double costOrExpendYuan,int pv){
		if(costOrExpendYuan != 0 && pv != 0){
			return costOrExpendYuan / (pv/1000) ;
		}
		return 0 ;
	}
	
	public static double getCpcSingleYuan(double costOrExpendYuan,int click){
		if(costOrExpendYuan != 0 && click != 0){
			return costOrExpendYuan / click ;
		}
		return 0 ;
	}
	
	public static void main(String[] args){
		System.out.println(getCpmSingleYuan(2343, 25000));
	}
}
