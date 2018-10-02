package com.ice.eclair.calculator;

import java.math.BigDecimal;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 21:48
 * @Description:
 */
public class CalculatorUtils {

	public static Double plus(Double add1, Double add2) {
		return new BigDecimal(add1).add(new BigDecimal(add2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static Double subtract(Double subtract1, Double subtract2) {
		return new BigDecimal(subtract1).subtract(new BigDecimal(subtract2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
