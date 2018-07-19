package com.jtd.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AccountAmountUtil {

	public static long computeAmountFen(double amountYuan) {
		
		BigDecimal yuan = new BigDecimal(amountYuan+"");
		BigDecimal fen = yuan.multiply(new BigDecimal(100));
		
		return fen.longValue();
	}

	public static String getAmountYuanString(long amountFen) {
		
		BigDecimal fen = new BigDecimal(amountFen);
		BigDecimal yuan = fen.divide(new BigDecimal(100));
		
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(yuan.doubleValue());
	}

	public static void main(String[] args) {
		
		System.out.println(AccountAmountUtil
				.computeAmountFen(1111111555.34));

		System.out.println(AccountAmountUtil
				.getAmountYuanString(1111111555550203L));

	}
}
