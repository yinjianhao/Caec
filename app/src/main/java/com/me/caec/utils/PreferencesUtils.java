package com.me.caec.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePreferences 工具类
 *
 * @auther yjh
 * @date 2016/8/28
 */
public class PreferencesUtils {

    public static final String NAME_SPACE = "CAECConfig";

    /**
     * 获取sharedPreferences
     *
     * @param ctx          上下文
     * @param key          键值
     * @param defaultValue 默认值
     * @return 返回值
     */
    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static Boolean getBoolean(Context ctx, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, Boolean value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME_SPACE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
}
