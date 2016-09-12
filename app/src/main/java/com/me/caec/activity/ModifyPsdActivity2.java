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
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class ModifyPsdActivity2 extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.et_old_psd)
    private EditText etOldPsd;

    @ViewInject(R.id.et_new_psd)
    private EditText etNewPsd;

    @ViewInject(R.id.et_new_psd_again)
    private EditText etNewPsdAgain;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private String phone;
    private String msgCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psd2);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("修改密码");
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
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

    private void confirm() {
        String oldPsd = etOldPsd.getText().toString();
        String newPsd = etNewPsd.getText().toString();
        String newPsdAgain = etNewPsdAgain.getText().toString();

        // TODO: 2016/9/1 验证密码规则
        if (oldPsd.isEmpty() || newPsd.isEmpty() || newPsdAgain.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPsd.equals(newPsdAgain)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }

        //发起请求
        RequestParams params = new RequestParams(RequestUrl.MODIFY_PSD_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("smsCode", msgCode);
        params.addQueryStringParameter("password", oldPsd);
        params.addQueryStringParameter("newPassword", newPsd);

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                        cleanUserInfo();
                        startActivity(new Intent(ModifyPsdActivity2.this, LoginActivity.class));
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

    private void cleanUserInfo() {
        PreferencesUtils.removeInt(this, "sex");
        PreferencesUtils.removeString(this, "birthday");
        PreferencesUtils.removeString(this, "phone");
        PreferencesUtils.removeString(this, "nickName");
        PreferencesUtils.removeString(this, "headImgUrl");
        PreferencesUtils.removeString(this, "token");
    }
}
