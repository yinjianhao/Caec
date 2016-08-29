package com.me.caec.utils;

import android.app.Activity;

/**
 * dp和px互相转换
 * Created by yin on 2016/8/29.
 */
public class DpTransforUtils {

    public static int px2dp(Activity activity, int px) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (px / density);
    }

    public static int dp2px(Activity activity, int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
