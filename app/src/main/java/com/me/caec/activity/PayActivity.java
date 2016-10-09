package com.me.caec.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.utils.NumberUtils;

import org.xutils.view.annotation.ViewInject;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tv_price)
    private TextView tvPrice;

    @ViewInject(R.id.tv_order_id)
    private TextView tvOrderId;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    public void render() {
        tvTitle.setText("订单支付");
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        float pay = intent.getFloatExtra("pay", 0);
        String orderId = intent.getStringExtra("orderId");

        tvPrice.setText(NumberUtils.toFixed2(pay));
        tvOrderId.setText(orderId);
    }

    @Override
    public void onShow() {

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
