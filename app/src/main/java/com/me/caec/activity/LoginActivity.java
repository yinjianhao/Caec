package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.Login;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录页面
 * 1.缺少多次错误验证码
 * 2.缺少登录加密
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
        etPhone.setText(PreferencesUtils.getString(this, "phone", ""));
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

    private void login(final String phone, String psd) {

        //发起登录请求
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", psd);

        BaseClient.post(this, RequestUrl.LOGIN_URL, params, Login.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                Login loginData = (Login) result;

                if (loginData.getResult() == 0) {
                    Login.DataBean dataBean = loginData.getData();

                    //保存用户信息
                    PreferencesUtils.setString(LoginActivity.this, "token", dataBean.getToken());
                    PreferencesUtils.setString(LoginActivity.this, "phone", dataBean.getMobile());
                    PreferencesUtils.setString(LoginActivity.this, "nickName", dataBean.getNickname());
                    PreferencesUtils.setString(LoginActivity.this, "headImgUrl", dataBean.getImg());

                    //设置更新数据
                    Intent i = new Intent();
                    i.putExtra("reload", true);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

//    private void login(final String phone, final String psd) {
//
//        //获取公钥加密
//        BaseClient.get(this, RequestUrl.PUBILIC_KEY_URL, null, RSA.class, new BaseClient.BaseCallBack() {
//            @Override
//            public void onSuccess(Object result) {
//                RSA data = (RSA) result;
//                try {
//                    final String encodePsd = RSAUtils.encodeByModAndExp(psd, data.getData().getMod(), data.getData().getExp());
//
//                    //发起登录请求
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("mobile", phone);
//                    params.put("password", encodePsd);
//                    params.put("mod", data.getData().getMod());
//                    BaseClient.post(RequestUrl.LOGIN_URL, params, Login.class, new BaseClient.BaseCallBack() {
//                        @Override
//                        public void onSuccess(Object result) {
//                            Login loginData = (Login) result;
//
//                            if (loginData.getResult() == 0) {
//                                Login.DataBean dataBean = loginData.getData();
//
//                                //保存用户信息
//                                PreferencesUtils.setString(LoginActivity.this, "psd", encodePsd);
//                                PreferencesUtils.setString(LoginActivity.this, "modulus", dataBean.getMod());
//                                PreferencesUtils.setString(LoginActivity.this, "token", dataBean.getToken());
//                                PreferencesUtils.setString(LoginActivity.this, "phone", dataBean.getMobile());
//                                PreferencesUtils.setString(LoginActivity.this, "nickName", dataBean.getNickname());
//                                PreferencesUtils.setString(LoginActivity.this, "headImgUrl", dataBean.getImg());
//                                finish();
//                            } else {
//                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable ex, boolean isOnCallback) {
//                            Toast.makeText(LoginActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled(Callback.CancelledException cex) {
//
//                        }
//
//                        @Override
//                        public void onFinished() {
//
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(LoginActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }


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
