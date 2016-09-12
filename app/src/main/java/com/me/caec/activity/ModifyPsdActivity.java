package com.me.caec.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.globle.RequestAddress;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class ModifyPsdActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_img_code)
    private EditText etImgCode;

    @ViewInject(R.id.et_msg_code)
    private EditText etMsgCode;

    @ViewInject(R.id.iv_img_code)
    private ImageView ivImgCode;

    @ViewInject(R.id.btn_send)
    private Button btnSend;

    @ViewInject(R.id.btn_next)
    private Button btnNext;

    //当前用户的登录手机号
    private String phone;

    //标志  能否发送短信验证码
    private boolean sendEnable;

    //标志  能否下一步
    private boolean nextEnable;

    //标志  倒计时
    private final int FLAG_COUNT_DOWN = 1;

    //倒计时时间(s)
    private int time = 60;

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
        setContentView(R.layout.activity_modify_psd);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("修改密码");
        tvBack.setOnClickListener(this);
        ivImgCode.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        phone = PreferencesUtils.getString(this, "phone", "");
        etPhone.setText(phone);
        etPhone.setSelection(phone.length());

        etImgCode.requestFocus();     //获取焦点

        getImageCode(phone);    //获取图片验证码

        etImgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    checkImageCode();
                }
            }
        });

        etMsgCode.addTextChangedListener(new TextWatcher() {
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
                } else if (sendEnable) {
                    setSendEnable(false);
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
            case R.id.iv_img_code:
                getImageCode(phone);
                break;
            case R.id.btn_send:
                if (sendEnable) {
                    sendMsgCode();
                }
                break;
            case R.id.btn_next:
                if (nextEnable) {
                    Intent i = new Intent(this, ModifyPsdActivity2.class);
                    i.putExtra("phone", phone);
                    i.putExtra("msgCode", etMsgCode.getText().toString());
                    startActivity(i);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取图片验证码
     */
    private void getImageCode(String phone) {
        x.image().bind(ivImgCode, RequestAddress.IMAGE_CODE_URL + "?phone=" + phone + "&_=" + Math.random());
    }

    /**
     * 验证图片验证码
     */
    private void checkImageCode() {
        RequestParams params = new RequestParams(RequestAddress.CHECK_IMAGE_CODE_URL);
        params.addQueryStringParameter("phone", etPhone.getText().toString());
        params.addQueryStringParameter("picCode", etImgCode.getText().toString());

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        setSendEnable(true);
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                        if (sendEnable) {
                            setSendEnable(false);
                        }
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
     * 设置下一步是否可用
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
     * 发送短信验证码
     */
    private void sendMsgCode() {
        RequestParams params = new RequestParams(RequestAddress.MSG_CODE_URL);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("picCode", etImgCode.getText().toString());
        params.addQueryStringParameter("biz", "1");

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
                    } else if (result == 103) {
                        Toast.makeText(getApplicationContext(), "图片验证码错误", Toast.LENGTH_SHORT).show();
                        getImageCode(phone);
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
        RequestParams params = new RequestParams(RequestAddress.CHECK_MSG_CODE_URL);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", etMsgCode.getText().toString());
        params.addQueryStringParameter("biz", "1");

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
                        if (nextEnable) {
                            setNextEnable(false);
                        }
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

    private void countDown() {
        handler.sendEmptyMessageDelayed(FLAG_COUNT_DOWN, 1000);
    }
}
