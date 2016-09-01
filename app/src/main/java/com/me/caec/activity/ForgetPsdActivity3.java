package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgetPsdActivity3 extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.et_psd)
    private EditText etPsd;

    @ViewInject(R.id.et_psd_again)
    private EditText etPsdAgain;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private String phone;
    private String imgCode;
    private String msgCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psd3);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("修改密码");
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        imgCode = intent.getStringExtra("imgCode");
        msgCode = intent.getStringExtra("msgCode");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    /**
     * 提交修改密码
     */
    private void confirm() {
        String psd = etPsd.getText().toString();
        String psdAgain = etPsdAgain.getText().toString();

        // TODO: 2016/9/1 验证密码规则
        if (!psd.equals(psdAgain)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        if (psd.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams(Client.FORGET_PSD_URL);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("picCode", imgCode);
        params.addQueryStringParameter("smsCode", msgCode);
        params.addQueryStringParameter("password", psd);

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "密码修改失败", Toast.LENGTH_SHORT).show();
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
     * 密码验证规则(有Bug)
     * @param psd
     * @return
     */
    private Boolean checkPsdRule(String psd) {
        //所有条件限定的正则表达式
        String proof = "^[a-zA-Z0-9~!@#$%^&*()_+-=;':\",./<>?`]+$";
        //四种不同的字符的正则表达式
        String proof1 = "[0-9]";
        String proof2 = "[A-z]";
        //特殊字符:   ~!@#$%^&*()_+\\-=;':",./<>?`
        String proof4 = "[~!@#$%^&*()_+\\-\\=;\':\",./<>\\?`]+";

        int consist = 0;    //密码是否满足规范

        //密码的长度是8-16位,只能包含英文字符、数字和规定的特殊字符
        if (psd.length() < 8 || psd.length() > 16) {
            Toast.makeText(this, "密码长度需8到16位", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!proof.matches(psd)) {
            Toast.makeText(this, "密码含非法字符", Toast.LENGTH_SHORT).show();
            return false;
        }

        consist += (proof1.matches(psd) ? 1 : 0) + (proof2.matches(psd) ? 1 : 0) + (proof4.matches(psd) ? 1 : 0);

        if (consist > 1) {
            return true;
        } else {
            Toast.makeText(this, "密码长度8~16位，由字母，数字和符号的两种以上组合", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
