package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.globle.Client;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
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

    @ViewInject(R.id.btn_send)
    private Button btnSend;

    @ViewInject(R.id.et_code)
    private EditText etCode;

    //标志  倒计时
    private final int FLAG_COUNT_DOWN = 1;

    //倒计时时间(s)
    private int time = 60;

    //标志 能否下一步
    private boolean nextEnable;

    //标志 能否发送
    private boolean sendEnable = true;

    //电话号码
    private String phone;
    //图片验证码
    private String imgCode;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_COUNT_DOWN:
                    if (time > 0) {
                        btnSend.setText(time + "秒");
                        time--;
                        countDown();
                    } else {
                        time = 60;
                        setSendEnable(true);
                        btnSend.setText("再次发送");
                    }
                    break;
            }
            return false;
        }
    });

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
        phone = intent.getStringExtra("phone");
        imgCode = intent.getStringExtra("imgCode");
        tvPhone.setText(phone);

        //发送验证码
        sendMsgCode();

        btnNext.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    checkMsgCode();
                } else if (nextEnable) {
                    setNextEnable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                if (nextEnable) {
                    Intent i = new Intent(ForgetPsdActivity2.this, ForgetPsdActivity3.class);
                    i.putExtra("phone", phone);
                    i.putExtra("msgCode", etCode.getText().toString());
                    i.putExtra("imgCode", imgCode);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.btn_send:
                if (sendEnable) {
                    sendMsgCode();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送短信验证码
     */
    private void sendMsgCode() {
        RequestParams params = new RequestParams(Client.MSG_CODE_URL);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("picCode", imgCode);
        params.addQueryStringParameter("biz", "2");

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "获取验证码成功", Toast.LENGTH_SHORT).show();
                        setSendEnable(false);
                        countDown();
                    } else {
                        Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void checkMsgCode() {
        RequestParams params = new RequestParams(Client.CHECK_MSG_CODE_URL);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", etCode.getText().toString());
        params.addQueryStringParameter("biz", "2");

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        setNextEnable(true);
                    } else {
                        Toast.makeText(getApplicationContext(), "短信验证码错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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
     * 倒计时
     */
    private void countDown() {
        handler.sendEmptyMessageDelayed(FLAG_COUNT_DOWN, 1000);
    }

    /**
     * 设置发送是否可用
     *
     * @param enable
     */
    private void setSendEnable(boolean enable) {
        sendEnable = enable;
        if (enable) {
            btnSend.setBackgroundResource(R.drawable.selector_btn_border_normal);
        } else {
            btnSend.setBackgroundResource(R.drawable.selector_btn_disabled);
        }
    }

    /**
     * 设置下一步是否可用
     *
     * @param enable
     */
    private void setNextEnable(boolean enable) {
        nextEnable = enable;
        if (enable) {
            btnNext.setBackgroundResource(R.drawable.selector_btn_normal);
        } else {
            btnNext.setBackgroundResource(R.drawable.selector_btn_disabled);
        }
    }
}
