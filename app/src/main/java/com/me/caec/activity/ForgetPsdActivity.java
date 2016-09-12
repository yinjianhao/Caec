package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.me.caec.globle.RequestUrl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_code)
    private EditText etCode;

    @ViewInject(R.id.iv_code)
    private ImageView ivCode;

    @ViewInject(R.id.ll_code)
    private LinearLayout llCode;

    @ViewInject(R.id.btn_next)
    private Button btnNext;

    //能否下一步
    private boolean nextEnable;

    //手机号正则
    private String phoneRule = "^1\\d{10}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("找回密码");
        tvBack.setOnClickListener(this);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches(phoneRule)) {
                    checkIsRegister(s.toString());
                }
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
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
                } else if (nextEnable) {
                    setNextEnable(false);
                }
            }
        });

        ivCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    /**
     * 检查是否已注册
     */
    private void checkIsRegister(final String phone) {
        RequestParams params = new RequestParams(RequestUrl.PHONE_ISREGISTER_URL);
        params.addQueryStringParameter("phone", phone);

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getBoolean("result")) {
                        getImageCode();
                        llCode.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "该手机还未注册", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        });
    }

    /**
     * 验证图片验证码
     */
    private void checkImageCode() {
        RequestParams params = new RequestParams(RequestUrl.CHECK_IMAGE_CODE_URL);
        params.addQueryStringParameter("phone", etPhone.getText().toString());
        params.addQueryStringParameter("picCode", etCode.getText().toString());

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        setNextEnable(true);
                    } else if (result == 103) {
                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_code:
                getImageCode();
                break;
            case R.id.btn_next:
                if (nextEnable) {
                    Intent i = new Intent(this, ForgetPsdActivity2.class);
                    i.putExtra("phone", etPhone.getText().toString());
                    i.putExtra("imgCode", etCode.getText().toString());
                    startActivity(i);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void getImageCode() {
        String phone = etPhone.getText().toString();
        if(phoneRule.matches(phone)) {
            x.image().bind(ivCode, RequestUrl.IMAGE_CODE_URL + "?phone=" + phone + "&_=" + Math.random());
        } else {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        }
    }
}
