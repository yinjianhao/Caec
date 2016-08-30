package com.me.caec.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class UserInfoActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        getUserInfo();
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
                        PreferencesUtils.setString(UserInfoActivity.this, "mobile", data.getString("mobile"));
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

    @Event(R.id.btn_back)
    private void onBackClick(View view) {
        finish();
    }
}
