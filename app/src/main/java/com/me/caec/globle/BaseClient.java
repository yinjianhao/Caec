package com.me.caec.globle;

import com.alibaba.fastjson.JSON;
import com.me.caec.bean.BaseBean;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * @auther yjh
 * @date 2016/9/12
 */
public class BaseClient {

    /**
     * 发送get请求
     *
     * @param url
     * @param map
     * @param beanClass
     * @param baseCallBack
     * @param <T>
     * @return
     */
    public static <T> Callback.Cancelable get(String url, Map<String, String> map, final Class<T> beanClass, final BaseCallBack baseCallBack) {
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

                } else {
                    Object data = JSON.parseObject(result, beanClass);
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
     *
     * @param url
     * @param map
     * @param beanClass
     * @param baseCallBack
     * @param <T>
     * @return
     */
    public static <T> Callback.Cancelable post(String url, Map<String, Object> map, final Class<T> beanClass, final BaseCallBack baseCallBack) {
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

                } else {
                    Object data = JSON.parseObject(result, beanClass);
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
     * 续期token
     */
    private static <T> void refreshToken(String url, Map<String, Object> map, final Class<T> beanClass, final BaseCallBack baseCallBack) {
        String phone = PreferencesUtils.getString(x.app(), "phone", "");

//        post(RequestUrl.REFRESH_TOKEN_URL)
    }

    public interface BaseCallBack {

        void onSuccess(Object result);

        void onError(Throwable ex, boolean isOnCallback);

        void onCancelled(Callback.CancelledException cex);

        void onFinished();
    }
}
