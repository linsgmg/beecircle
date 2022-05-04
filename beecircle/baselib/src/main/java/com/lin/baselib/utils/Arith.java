package com.lin.baselib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

public class Arith {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 不能实例化
    private Arith() {
    }

    /**
     * 说明：
     * 提供精确的加法运算
     * 创建日期: 2019-4-28
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        if (v1 == null||v1.equals("null") || TextUtils.isEmpty(v1)) {
            v1 = "0.00";
        }
        if (v2 == null||v2.equals("null") || TextUtils.isEmpty(v2)) {
            v2 = "0.00";
        }
        BigDecimal b1 = new BigDecimal(v1);// 建议写string类型的参数，下同
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }


    /**
     * 说明：
     * 提供精确的减法运算
     * 创建日期: 2019-4-28
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String sub(String v1, String v2) {
        if (v1 == null||v1.equals("null") || TextUtils.isEmpty(v1)) {
            v1 = "0.00";
        }
        if (v2 == null||v2.equals("null") || TextUtils.isEmpty(v2)) {
            v2 = "0.00";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 说明：
     * 提供精确的乘法运算
     * 创建日期: 2019-4-28
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String mul(String v1, String v2) {
        if (v1 == null || v1.equals("null") || TextUtils.isEmpty(v1)) {
            v1 = "0.00";
        }
        if (v2 == null || v2.equals("null") || TextUtils.isEmpty(v2)) {
            v2 = "0.00";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 说明：
     * 提供相对精确的除法运算，当发生除不尽的情况，精确到.后10位
     * 创建日期: 2019-4-28
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 说明：提供相对精确的除法运算，并且保留后几位
     * 创建日期: 2019-4-28
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static String div(String v1, String v2, int scale) {
        if (v1 == null||v1.equals("null") || TextUtils.isEmpty(v1)) {
            v1 = "0.00";
        }
        if (v2 == null||v2.equals("null") || TextUtils.isEmpty(v2)) {
            v2 = "0.00";
        }
        if (scale < 0) {
            throw new IllegalArgumentException(" the scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();// scale 后的四舍五入
    }

    /**
     * 说明：保留后几位小数
     * 创建日期: 2019-4-28
     *
     * @param v1
     * @param scale
     * @return
     */
    public static String round(String v1, int scale) {
        if (v1 == null||v1.equals("null") || TextUtils.isEmpty(v1)) {
            return "0.00";
        }
        if (scale < 0) {
            throw new IllegalArgumentException(" the scale must be a positive integer or zero");
        }
        if ("0E-8".equals(v1)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < scale; i++) {
                sb.append("0");
            }
            return "0." + sb.toString();
        }
        BigDecimal b1 = new BigDecimal(v1);
        return b1.setScale(scale, BigDecimal.ROUND_DOWN).toString();
    }


}
