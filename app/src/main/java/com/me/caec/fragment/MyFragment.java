package com.me.caec.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.activity.LoginActivity;
import com.me.caec.activity.UserInfoActivity;
import com.me.caec.utils.LoginUtils;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Objects;

/**
 * 我的
 * Created by yin on 2016/8/29.
 */

@ContentView(R.layout.fragment_my)
public class MyFragment extends BaseFragment {

    @ViewInject(R.id.iv_head)
    private ImageView ivHead;

    @ViewInject(R.id.tv_login)
    private TextView tvLogin;

    @ViewInject(R.id.ll_user_info)
    private LinearLayout llUserInfo;

    @ViewInject(R.id.tv_name)
    private TextView tvName;

    @ViewInject(R.id.tv_account)
    private TextView tvAccount;

    @Override
    public void initData() {
        setUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();

        setUserInfo();
    }

    /**
     * 初始化用户信息
     */
    private void setUserInfo() {
        //判断登录
        if (LoginUtils.isLogin(getActivity())) {
            String nickName = PreferencesUtils.getString(getActivity(), "nickName", "");
            String mobile = PreferencesUtils.getString(getActivity(), "mobile", "");
            String headImgUrl = PreferencesUtils.getString(getActivity(), "headImgUrl", "");

            tvLogin.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.VISIBLE);

            x.image().bind(ivHead, headImgUrl);
            tvAccount.setText(mobile);
            tvName.setText(nickName);
        }
    }

    @Event(R.id.iv_head)
    private void onHeadClick(View view) {
        //判断是否登录
        if (LoginUtils.isLogin(getActivity())) {
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Event(R.id.tv_login)
    private void onLoginClick(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
