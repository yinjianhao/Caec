package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.R;
import com.me.caec.bean.OrderDetailPay;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 订单拆分时
 */

public class OrderDetailNormalActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    private OrderList.DataBean order;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_normal);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("订单详情");
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        order = (OrderList.DataBean) intent.getSerializableExtra("order");
        orderId = order.getSubOrders().get(0).getId();

        getOrderDetail();
    }

    private void getOrderDetail() {
        RequestParams params = new RequestParams(Client.ORDER_DETAIL_PAY_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("id", orderId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OrderDetailNormalActivity.this, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }
}
