package com.fcsbbc.utils;

import java.math.BigDecimal;

public class BaseCalculate {
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * add
	 * @param v1
	 * @param v2
	 * @return	sum result
	 */
    public static double add(double v1, double v2) {   
        BigDecimal b1 = new BigDecimal(Double.toString(v1));   
        BigDecimal b2 = new BigDecimal(Double.toString(v2));   
        return b1.add(b2).doubleValue();   
    }
    
    /**
     * subduction
     * @param v1
     * @param v2
     * @return	sub result
     */
    public static double substract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * multiply
     * @param v1
     * @param v2
     * @return	multiply result
     */
    public static double multiply(double v1, double v2) {   
        BigDecimal b1 = new BigDecimal(Double.toString(v1));   
        BigDecimal b2 = new BigDecimal(Double.toString(v2));   
        return b1.multiply(b2).doubleValue();   
    }
    
    /**
     * divide
     * @param v1
     * @param v2
     * @return	divided result
     */
    public static double divide(double v1, double v2) {   
        return divide(v1, v2, DEF_DIV_SCALE);   
    }
    
    /**
     * divide
     * @param v1
     * @param v2
     * @param scale	precision accuracy
     * @return	divided result
     */
    public static double divide(double v1, double v2, int scale) {   
        if (scale < 0) {   
            throw new IllegalArgumentException("The scale must be a positive integer or zero");   
        }   
           
        BigDecimal b1 = new BigDecimal(Double.toString(v1));   
        BigDecimal b2 = new BigDecimal(Double.toString(v2));   
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();   
    }
    
    /**
     * round-up
     * @param v
     * @param scale
     * @return	round result
     */
    public static double round(double v, int scale) {   
        if (scale < 0) {   
            throw new IllegalArgumentException("The scale must be a positive integer or zero");   
        }   
           
        BigDecimal b = new BigDecimal(Double.toString(v));   
        BigDecimal one = new BigDecimal("1");   
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();   
    }
    
    public static void main(String[] args) {   
        //直接使用浮点数进行计算，得到的结果是有问题的   
        System.out.println(0.01 + 0.05);   
           
        //使用了BigDecimal类进行计算后，可以做到精确计算   
        System.out.println(BaseCalculate.add(0.0000000000005, 0.00000001));  
        
        System.out.println(BaseCalculate.multiply(255.05, 5));
   }
}
