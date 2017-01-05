package com.kem.kline.utils;

public class StringUtils {

    /**
     * <div style='font-size:14px'>
     * <b>说明</b>
     * <p>
     * 判断传入的字符串是否为空,其中如果字符值为"null",鉴定为true
     * </p>
     * </div>
     *
     * @param value
     */
    public static boolean isEmptyAsString(String value) {
        return !isNotEmptyAsString(value);
    }

    /**
     * <div style='font-size:14px'>
     * <b>说明</b>
     * <p>
     * 判断传入的字符串是否不为空,其中如果字符值为"null",鉴定为true
     * </p>
     * </div>
     *
     * @param value
     */
    public static boolean isNotEmptyAsString(String value) {
        value = value == null ? "" : value.equalsIgnoreCase("null") ? "" : value.trim();
        return value.matches(".{1,}");
    }

}
