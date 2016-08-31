package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class ForgetPsdActivity2 extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;

    @ViewInject(R.id.btn_next)
    private Button btnNext;

    //能否下一步
    private boolean nextEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd2);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("找回密码");
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        tvPhone.setText(phone);

        sendMsgCode();

        btnNext.setOnClickListener(this);
    }

    /**
     * 发送短信验证码
     */
    private void sendMsgCode() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                if (nextEnable) {

                }
                break;
            default:
                break;
        }
    }
}
