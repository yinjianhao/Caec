package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.ConfirmDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_head)
    private ImageView ivHead;

    @ViewInject(R.id.tv_name)
    private TextView tvNAme;

    @ViewInject(R.id.tv_sex)
    private TextView tvSex;

    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;

    @ViewInject(R.id.tv_birthday)
    private TextView tvBirthday;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tv_login_out)
    private TextView tvLoginOut;

    @ViewInject(R.id.ll_modify)
    private LinearLayout llModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        getUserInfo();

        tvTitle.setText("个人信息");
        tvBack.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        llModify.setOnClickListener(this);
    }

    private void getUserInfo() {
        String token = PreferencesUtils.getString(this, "token", "");
        RequestParams params = new RequestParams(Client.USER_INFO_URL);
        params.addQueryStringParameter("token", token);

        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("result") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        PreferencesUtils.setInt(UserInfoActivity.this, "sex", data.getInt("sex"));
                        PreferencesUtils.setString(UserInfoActivity.this, "birthday", data.getString("birthday"));
                        PreferencesUtils.setString(UserInfoActivity.this, "phone", data.getString("mobile"));
                        PreferencesUtils.setString(UserInfoActivity.this, "nickName", data.getString("nickName"));
                        PreferencesUtils.setString(UserInfoActivity.this, "headImgUrl", data.getString("img"));

                        x.image().bind(ivHead, data.getString("img"));
                        tvNAme.setText(data.getString("nickName"));
                        tvPhone.setText(data.getString("mobile"));
                        tvSex.setText(data.getInt("sex") == 1 ? "男" : "女");
                        tvBirthday.setText(data.getString("birthday"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_login_out:
                loginOut();
                break;
            case R.id.ll_modify:
                startActivity(new Intent(this, ModifyPsdActivity.class));
                break;
            default:
                break;
        }
    }

    private void loginOut() {
        ConfirmDialog dialog = new ConfirmDialog(UserInfoActivity.this);
        dialog.setBody("是否退出登录?");
        dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void confirm() {
                PreferencesUtils.removeInt(UserInfoActivity.this, "sex");
                PreferencesUtils.removeString(UserInfoActivity.this, "birthday");
                PreferencesUtils.removeString(UserInfoActivity.this, "phone");
                PreferencesUtils.removeString(UserInfoActivity.this, "nickName");
                PreferencesUtils.removeString(UserInfoActivity.this, "headImgUrl");
                PreferencesUtils.removeString(UserInfoActivity.this, "token");
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        String token = PreferencesUtils.getString(this, "token", "");
        if (token.isEmpty()) {
            finish();
        }
    }
}
