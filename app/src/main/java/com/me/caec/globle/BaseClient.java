package com.me.caec.globle;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.activity.LoginActivity;
import com.me.caec.bean.BaseBean;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther yjh
 * @date 2016/9/12
 */
public class BaseClient {

    /**
     * 发送get请求
     */
    public static Callback.Cancelable get(final Context ctx, String url, Map<String, String> map, final Class<?> resultClass, final BaseCallBack baseCallBack) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }

        return x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseBean baseBean = JSON.parseObject(result, BaseBean.class);

                if (baseBean.getResult() == -1) {  //token过期
                    goLogin(ctx);
                } else {
                    Object data = JSON.parseObject(result, resultClass);
                    baseCallBack.onSuccess(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                baseCallBack.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                baseCallBack.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                baseCallBack.onFinished();
            }
        });
    }

    /**
     * 发送post请求
     */
    public static Callback.Cancelable post(final Context ctx, String url, Map<String, Object> map, final Class<?> resultClass, final BaseCallBack baseCallBack) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        return x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseBean baseBean = JSON.parseObject(result, BaseBean.class);

                if (baseBean.getResult() == -1) {  //token过期
                    goLogin(ctx);
                } else {
                    Object data = JSON.parseObject(result, resultClass);
                    baseCallBack.onSuccess(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                baseCallBack.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                baseCallBack.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                baseCallBack.onFinished();
            }
        });
    }

    /**
     * 去登陆
     */
    private static void goLogin(Context ctx) {
        Toast.makeText(ctx, "登陆过期,请重新登陆", Toast.LENGTH_SHORT).show();
        PreferencesUtils.removeString(ctx, "token");
        Intent i = new Intent(ctx, LoginActivity.class);
        ctx.startActivity(i);
    }

    /**
     * 续期token
     */
    private static void refreshToken(String url, Map<String, Object> map, final Class<?> beanClass, final BaseCallBack baseCallBack) {
        String phone = PreferencesUtils.getString(x.app(), "phone", "");
    }

    /**
     * 回调
     */
    public interface BaseCallBack {

        void onSuccess(Object result);

        void onError(Throwable ex, boolean isOnCallback);

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
    }
}
