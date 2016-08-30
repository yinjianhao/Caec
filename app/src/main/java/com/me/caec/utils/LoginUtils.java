package com.me.caec.utils;

import android.content.Context;

/**
 * 登录相关操作
 * Created by yin on 2016/8/30.
 */
public class LoginUtils {

    /**
     * 判断是否登录
     *
     * @param ctx
     * @return
     */
    public static Boolean isLogin(Context ctx) {
        String token = PreferencesUtils.getString(ctx, "token", "");
        return !token.isEmpty();
    }
}
