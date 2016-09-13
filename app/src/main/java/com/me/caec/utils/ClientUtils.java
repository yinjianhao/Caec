package com.me.caec.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.me.caec.bean.Location;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登录相关操作
 * Created by yin on 2016/8/30.
 */
public class ClientUtils {

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
