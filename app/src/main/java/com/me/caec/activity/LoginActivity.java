package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 登录页面
 * 1.缺少多次错误验证码
 */
public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_psd)
    private EditText etPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("登录");
    }

    //登录
    @Event(R.id.btn_login)
    private void onLoginClick(View view) {
        String rule = "^1\\d{10}$";
        String phone = etPhone.getText().toString();
        String psd = etPsd.getText().toString();

        if (phone.isEmpty()) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (psd.isEmpty()) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches(rule)) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        login(phone, psd);
    }

    /**
     * 发起登录请求
     *
     * @param phone 手机号
     * @param psd   密码
     */
    private void login(String phone, String psd) {
        RequestParams params = new RequestParams(Client.LOGIN_URL);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("password", psd);

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("result") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        //保存用户信息
                        PreferencesUtils.setString(LoginActivity.this, "token", data.getString("token"));
                        PreferencesUtils.setString(LoginActivity.this, "phone", data.getString("mobile"));
                        PreferencesUtils.setString(LoginActivity.this, "nickName", data.getString("nickname"));
                        PreferencesUtils.setString(LoginActivity.this, "headImgUrl", data.getString("img"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, ex.toString());
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Event(R.id.tv_register)
    private void onRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Event(R.id.tv_forget)
    private void onForgetClick(View view) {
        startActivity(new Intent(this, ForgetPsdActivity.class));
    }

    @Event(R.id.btn_back)
    private void onBackClick(View view) {
        finish();
    }
}
