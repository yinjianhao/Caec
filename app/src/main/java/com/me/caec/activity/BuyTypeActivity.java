package com.me.caec.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;

import org.xutils.view.annotation.ViewInject;

public class BuyTypeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_buy_type);
    }

    @Override
    public void render() {
        tvTitle.setText("选择经销商");
        tvBack.setOnClickListener(this);
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
