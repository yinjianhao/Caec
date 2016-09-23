package com.me.caec.activity;

import android.content.Intent;
import android.content.res.ObbInfo;
import android.util.Log;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.ConfirmOrder;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOrderActivity extends BaseActivity {

    private ConfirmOrder.DataBean dataBean;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_confirm_order);
    }

    @Override
    public void render() {
        Intent intent = getIntent();
        String params = intent.getStringExtra("params");
        getConfirmOrderList(params);
    }

    @Override
    public void onShow() {

    }

    /**
     * 获取确认订单列表
     */
    private void getConfirmOrderList(String params) {
        Map<String, Object> map = new HashMap<>();

        map.put("token", PreferencesUtils.getString(this, "token", ""));
        map.put("goods", params);

        BaseClient.post(this, RequestUrl.CONFIRM_LIST_URL, map, ConfirmOrder.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                ConfirmOrder data = (ConfirmOrder) result;

                if (data.getResult() == 0) {
                    dataBean = data.getData();
                    initConfirmList();
                } else {
                    Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 渲染列表
     */
    private void initConfirmList() {

    }
}
