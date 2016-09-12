package com.me.caec.globle;

import org.xutils.common.Callback;

/**
 * xutils  http请求的回调
 * 基础回调,统一处理token过期等问题
 *
 * @auther yjh
 * @date 2016/9/12
 */
public class BaseCallBack<AddressList> implements Callback.CommonCallback<AddressList> {
    @Override
    public void onSuccess(AddressList result) {

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
}
