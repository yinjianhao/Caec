package com.me.caec.utils;

import java.text.DecimalFormat;

/**
 * Created by yin on 2016/9/21.
 */

public class NumberUtils {

    private static DecimalFormat decimalFormat;

    /**
     * 转换为2为小数
     *
     * @param number
     * @return
     */
    public static String toFixed2(float number) {
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("0.00");
        }
        return decimalFormat.format(number);
    }

}
