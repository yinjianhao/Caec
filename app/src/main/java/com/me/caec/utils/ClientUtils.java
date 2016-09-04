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

    public static <T> void baseHttp(RequestParams params, final Class<T> clazz, BaseHttpListener listener) {

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                T t = JSON.parseObject(string, clazz);
                ((Location) t).getResult();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * baseHttp回调接口
     */
    public interface BaseHttpListener {
        void getResult(int result, String data);

        void onSuccess(String data);

        void onError(Throwable ex, boolean isOnCallback);

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
    }

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
