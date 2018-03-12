/**
 * 
 */
package com.fcsbbc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author luo.changshu
 *
 */
public class ArithmeticUtil {
	private static final int DEF_DIV_SCALE = 10;	//default accuracy
	NumberFormat currency = NumberFormat.getCurrencyInstance();
	
	public static NumberFormat getNumberFormatInstance(int formatFlag) {
		NumberFormat numberFormatInstance = null;
		if (formatFlag == 1) {
			numberFormatInstance = NumberFormat.getCurrencyInstance();
		}
		else if (formatFlag == 2) {
			numberFormatInstance = NumberFormat.getPercentInstance();
		}
		return numberFormatInstance;
	}
	/**
	 * add method
	 * @param var1
	 * @param var2
	 * @return	result[double]
	 */
	public static double add(double var1, double var2) {
		BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
		return bigDecimal1.add(bigDecimal2).doubleValue();
	}
	/**
	 * add method
	 * @param var1
	 * @param var2
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal add(String var1, String var2) {
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.add(bigDecimal2);
	}
	/**
	 * add method
	 * @param var1
	 * @param var2
	 * @param scale	accuracy
	 * @return	result[String]
	 */
	public static String add(String var1, String var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.add(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * sub method
	 * @param var1
	 * @param var2
	 * @return	result[double]
	 */
	public static double sub(double var1, double var2) {
		BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
		return bigDecimal1.subtract(bigDecimal2).doubleValue();
	}
	/**
	 * sub method
	 * @param var1
	 * @param var2
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal sub(String var1, String var2) {
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.subtract(bigDecimal2);
	}
	/**
	 * sub method
	 * @param var1
	 * @param var2
	 * @param scale	accuracy
	 * @return	result[String]
	 */
	public static String sub(String var1, String var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.subtract(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * multiply method
	 * @param var1
	 * @param var2
	 * @return	result[double]
	 */
	public static double mul(double var1, double var2) {
		BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
		return bigDecimal1.multiply(bigDecimal2).doubleValue();
	}
	/**
	 * multiply method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[double]
	 */
	public static double mul(double var1, double var2, int scale) {
		BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
		return round(bigDecimal1.multiply(bigDecimal2).doubleValue(), scale);
	}
	/**
	 * multiply method
	 * @param var1
	 * @param var2
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal mul(String var1, String var2) {
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.multiply(bigDecimal2);
	}
	/**
	 * multiply method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[String]
	 */
	public static String mul(String var1, String var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.multiply(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * divide method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[double]
	 */
	public static double div(double var1, double var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
		return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * divide method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[String]
	 */
	public static String div(String var1, String var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * divide method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal div(BigDecimal var1, BigDecimal var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return var1.divide(var2, scale, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * round method
	 * @param var
	 * @param scale
	 * @return	result[double]
	 */
	public static double round(double var, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal = new BigDecimal(Double.toString(var));
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * round method
	 * @param var
	 * @param scale
	 * @return	result[String]
	 */
	public static String round(String var, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal = new BigDecimal(var);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * remainder method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[String]
	 */
	public static String remainder(String var1, String var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.remainder(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	/**
	 * remainder method
	 * @param var1
	 * @param var2
	 * @param scale
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal remainder(BigDecimal var1, BigDecimal var2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return var1.remainder(var2).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * compare method
	 * @param var1
	 * @param var2
	 * @return	result[boolean]
	 */
	public static boolean compare(String var1, String var2) {
		boolean compareResult = false;
		if ((var1 != null && var1.length() > 0) && (var2 != null && var2.length() > 0)) {
			BigDecimal bigDecimal1 = new BigDecimal(var1);
			BigDecimal bigDecimal2 = new BigDecimal(var2);
			int result = bigDecimal1.compareTo(bigDecimal2);
			if (result > 0) {
				compareResult = true;
			}
		}
		return compareResult;
	}
	/**
	 * compare method
	 * @param var1
	 * @param var2
	 * @return	result[boolean]
	 */
	public static boolean compare(BigDecimal var1, BigDecimal var2) {
		boolean compareResult = false;
		int result = var1.compareTo(var2);
		if (result > 0) {
			compareResult = true;
		}
		return compareResult;
	}
	/**
	 * compare method
	 * @param var1
	 * @param var2
	 * @return	result[integer]
	 */
	public static int compareDetails(String var1, String var2) {
		int compareResult = -2;
		if ((var1 != null && var1.length() > 0) && (var2 != null && var2.length() > 0)) {
			BigDecimal bigDecimal1 = new BigDecimal(var1);
			BigDecimal bigDecimal2 = new BigDecimal(var2);
			compareResult = bigDecimal1.compareTo(bigDecimal2);
		}
		return compareResult;
	}
	/**
	 * 
	 * @param var1
	 * @param var2
	 * @return	result[integer]
	 */
	public static int getInteger(String var1, String var2) {
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		BigDecimal bigDecimal2 = new BigDecimal(var2);
		return bigDecimal1.divideToIntegralValue(bigDecimal2).intValue();
	}
	/**
	 * 
	 * @param var1
	 * @return	result[String]
	 */
	public static String getFormat(String var1) {
		BigDecimal bigDecimal1 = new BigDecimal(var1);
		DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
		return decimalFormat.format(bigDecimal1.doubleValue());
	}
	/**
	 * 
	 * @param var1
	 * @param var2
	 * @return	result[integer]
	 */
	public static int remainderZero(String var1, String var2) {
		int divideOkResult = -1;
		if ((var1 != null && var1.length() > 0) && (var2 != null && var2.length() > 0)) {
			BigDecimal bigDecimal1 = new BigDecimal(var1);
			BigDecimal bigDecimal2 = new BigDecimal(var2);
			BigDecimal remainder = bigDecimal1.divideAndRemainder(bigDecimal2)[1];
			if (compareDetails(remainder.toString(), "0") == 0) {
				divideOkResult = 0;
			}
			else {
				divideOkResult = 1;
			}
		}
		return divideOkResult;
	}
	/**
	 * 
	 * @param var1
	 * @param var2
	 * @return	result[BigDecimal]
	 */
	public static BigDecimal getQuotient(String var1, String var2) {
		BigDecimal quotient = null;
		if ((var1 != null && var1.length() > 0) && (var2 != null && var2.length() > 0)) {
			BigDecimal bigDecimal1 = new BigDecimal(var1);
			BigDecimal bigDecimal2 = new BigDecimal(var2);
			quotient = bigDecimal1.divideAndRemainder(bigDecimal2)[0];
		}
		return quotient;
	}

	public static int getNumericLength(String var1) {
		int numericLength = -1;
		if (var1 != null && var1.length() > 0) {
			int numericposition = var1.lastIndexOf(".");
			if (numericposition > 0) {
				numericLength = var1.length() - numericposition - 1;
			}
			else {
				numericLength = 0;
			}
		}
		return numericLength;
	}
	
	public static String removeDecimalPoint(String var1) {
		String removedDecimalPoint = "";
		if (var1 != null && var1.length() > 0) {
			int numericposition = var1.lastIndexOf(".");
			int numericLength = getNumericLength(var1);
			if (numericLength > 0) {
				String preSplitString = var1.substring(0, numericposition);
//				preSplitString = preSplitString.replaceAll("\\.", "");
				preSplitString = preSplitString.replace(".", "");
				removedDecimalPoint = preSplitString + var1.substring(numericposition, var1.length());
			}
			else if (numericLength == 0 && numericposition > 0) {
				removedDecimalPoint = var1.replace(".", "");
			}
			else {
				removedDecimalPoint = var1;
			}
		}
		return removedDecimalPoint;
	}
	/**
	 * 
	 * @param var1
	 * @return
	 */
	public static String removeDecimalZero(String var1) {
		String removedDecimalZero = "";
		if (var1 != null && var1.length() > 0) {
			removedDecimalZero = String.valueOf(Double.parseDouble(var1));
			if (removedDecimalZero.lastIndexOf(".") > 0) {
				removedDecimalZero = removedDecimalZero.replaceAll("0+?$", "");
				removedDecimalZero = removedDecimalZero.replaceAll("[.]$", "");
			}
//			else {
//				removedDecimalZero = var1;
//			}
		}
		return removedDecimalZero;
	}
	
	public static String getNearestValue(String var1, String var2) {
		String nearestValue = "";
		if ((var1 != null && var1.length() > 0) && (var2 != null && var2.length() > 0)) {
			if (remainderZero(var1, var2) == 0) {
				nearestValue = var1;
			}
			else if (remainderZero(var1, var2) == 1) {
				BigDecimal bigDecimal = getQuotient(var1, var2);
				nearestValue = mul(bigDecimal.toString(), var2).toString();
			}
			nearestValue = removeDecimalZero(nearestValue);
		}
		return nearestValue;
	}
	/**
	 * 
	 * @param var1
	 * @return
	 */
	public static String removeZero(String var1) {
		String removeZero = "";
		if (var1 != null && var1.length() > 0) {
			removeZero = String.valueOf(Double.parseDouble(var1));
		}
		return removeZero;
	}
	
	public static void main(String[] args) {
		String var1 = "5000000.00", var2 = "56500", var3 = "50000000.2444";
		System.out.println("var1 > var2 : " + ArithmeticUtil.compare(var1, var2));
		System.out.println("var1 / var2 : " + ArithmeticUtil.div(var1, var2, DEF_DIV_SCALE));
		System.out.println("var1 * var2 : " + ArithmeticUtil.mul(var1, var2));
		System.out.println("var1 % var2 : " + ArithmeticUtil.remainder(var1, var2, 0));
		System.out.println("var1 % var2 : " + ArithmeticUtil.getInteger(var1, var2));
		System.out.println("var1 % var2 : " + ArithmeticUtil.getFormat(var3));
		BigDecimal priceTick = new BigDecimal("0.04");
		String minPrice = "238.55";
		System.out.println(ArithmeticUtil.sub(minPrice, priceTick.toString()).toString());
		System.out.println("var1 >= var2 : " + ArithmeticUtil.compare("100.10", "100.10"));
		System.out.println(ArithmeticUtil.remainder(new BigDecimal(minPrice), priceTick, 9));
		BigDecimal bigDecimal = ArithmeticUtil.div(new BigDecimal(minPrice), priceTick, 0);
		System.out.println(ArithmeticUtil.mul(bigDecimal.toString(), priceTick.toString()));
		System.out.println("var1 vs var2 : " + ArithmeticUtil.compareDetails("2.0000011", "2.00001"));
		System.out.println(minPrice + "%" + priceTick.toString() + ":" + ArithmeticUtil.remainderZero(minPrice, priceTick.toString()));
		System.out.println(minPrice + "/" + priceTick.toString() + ":" + ArithmeticUtil.getQuotient(minPrice, priceTick.toString()));
		String var4 = "1";
		String removeDecimalPoint = ArithmeticUtil.removeDecimalPoint(var4);
//		System.out.println("removeDecimalPoint = " + removeDecimalPoint);
//		String removedDecimalZero = ArithmeticUtil.removeDecimalZero(removeDecimalPoint);
//		String removedDecimalZero = ArithmeticUtil.removeZero(removeDecimalPoint);
		String removedDecimalZero = ArithmeticUtil.removeDecimalZero(removeDecimalPoint);
//		System.out.println("removedDecimalZero = " + removedDecimalZero);
		System.out.println("numericFormat = " + removedDecimalZero + " : numericLength = " + ArithmeticUtil.getNumericLength(removedDecimalZero));
//		String var5 = "56.05001";
//		System.out.println(var5.indexOf("."));
		String var6 = "238.1005556", var7 = "0.05";
		System.out.println(getNearestValue(var6, var7));
		
	}
}
