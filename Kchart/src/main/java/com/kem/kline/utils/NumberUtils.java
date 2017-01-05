package com.kem.kline.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 描述：数字格式化工具类
 *
 * @author zhaoyi
 */
public class NumberUtils {

    /**
     * 数字格式化
     *
     * @param in      需要格式化的内容
     * @param keepNum 保留位数,默认保留两位小数
     * @param isRound 是否开启四舍五入
     * @return 字符串格式结果
     */
    public static String format(double in, int keepNum, boolean isRound) {

        String result = "";

        DecimalFormat format = new DecimalFormat();

        if (keepNum < 0) {
            keepNum = 0;
        }

        format.setMaximumFractionDigits(keepNum);

        format.setMinimumFractionDigits(keepNum);

        format.setGroupingUsed(false);

        if (isRound) {
            format.setRoundingMode(RoundingMode.HALF_UP);
        } else {
            format.setRoundingMode(RoundingMode.FLOOR);
        }

        result = format.format(in);

        return result;
    }

    /**
     * 数字格式化
     *
     * @param in      需要格式化的内容
     * @param keepNum 保留位数,默认保留两位小数
     * @param isRound 是否开启四舍五入
     * @return 返回字符串结果
     */
    public static String format(String in, int keepNum, boolean isRound) {
        String result = "";
        double indouble = 0;
        try {
            indouble = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return result = "";
        }
        return format(indouble, keepNum, isRound);
    }

    /**
     * 数字格式化
     *
     * @param in        需要格式化的内容
     * @param stocktype 证券类型,请使用StockType常量类
     * @return 返回字符串结果
     */
    public static String format(double in, int stocktype) {
        String result = "";
        switch (stocktype) {
            case 0:
            case 1:
            case 2:
            case 7:
            case 9:
            case 14:
            case 15:
            case 17:
            case 18:
            case 60:
            case 64:
            case 65:
            case 66:
                result = format(in, 2, true);
                break;
            case -2:
            case 3:
            case 4:
            case 6:
            case 10:
            case 11:
            case 12:
            case 13:
            case 16:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 30:
            case 61:
                result = format(in, 3, true);
                break;

            case -3:
                result = format(in, 4, true);
                break;
            default:
                result = format(in, 2, true);
        }
        return result;
    }

    /**
     * 数字格式化
     *
     * @param in        需要格式化的内容
     * @param stocktype 证券类型,请使用StockType常量类
     * @return 返回字符串结果
     */
    public static String format(String in, int stocktype) {
        String result = "";
        double indouble = 0;
        try {
            indouble = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        result = format(indouble, stocktype);
        return result;
    }

    /**
     * 数字格式化
     *
     * @param in        需要格式化的内容
     * @param stocktype 证券类型,请使用StockType常量类
     * @param tyep 当数据为0时默认返回数据
     * @return 返回字符串结果
     */
    public static String format(String in, int stocktype, String tyep) {
        if (Double.parseDouble(in) == 0) {
            return tyep;
        }

        String result = "";
        double indouble = 0;
        try {
            indouble = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        result = format(indouble, stocktype);
        return result;
    }


    /**
     * 格式化数字为中文
     *
     * @param in      需要格式化的内容
     * @param keepNum 保留位数,默认保留两位小数
     * @param isRound 是否开启四舍五入
     * @return 返回字符串结果(100万亿)
     */
    public static String formatToChinese(double in, int keepNum, boolean isRound) {
        long wanyi = 1000000000000l;//万亿
        long yi = 100000000l;       //亿
        long wan = 10000l;          //万

        String result = "";
        try {
            result = "";
            if (Math.abs(Double.parseDouble(format((in / wanyi), keepNum, isRound))) >= 1) {//单位:万亿
                result = format((in / wanyi), keepNum, isRound) + "万亿";
            } else if (Math.abs(Double.parseDouble(format((in / yi), keepNum, isRound))) >= 1) {//单位:亿
                result = format((in / yi), keepNum, isRound) + "亿";
            } else if (Math.abs(Double.parseDouble(format((in / wan), keepNum, isRound))) >= 1) {//单位:万
                result = format((in / wan), keepNum, isRound) + "万";
            } else {//单位:万以下
                result = format(in, keepNum, isRound);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化数字字符串为中文
     *
     * @param in      需要格式化的内容
     * @param keepNum 保留位数,默认保留两位小数
     * @param isRound 是否开启四舍五入
     * @return 返回字符串结果(100万亿)
     */
    public static String formatToChinese(String in, int keepNum, boolean isRound) {
        double temp = 0d;
        try {
            temp = Double.parseDouble(in);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            temp = 0d;
        }
        return formatToChinese(temp, keepNum, isRound);
    }


    public static double parseDouble(String str) {
        if (StringUtils.isEmptyAsString(str)) {
            return 0;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }

    }

    public static float parseFloat(String str) {
        if (StringUtils.isEmptyAsString(str)) {
            return 0f;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0f;
        }
    }

}
