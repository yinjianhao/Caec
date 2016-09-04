package com.me.caec.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.activity.AddressListActivity;
import com.me.caec.activity.LoginActivity;
import com.me.caec.activity.OrderListActivity;
import com.me.caec.activity.UserInfoActivity;
import com.me.caec.utils.ClientUtils;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
        if (ClientUtils.isLogin(getActivity())) {
            String nickName = PreferencesUtils.getString(getActivity(), "nickName", "");
            String phone = PreferencesUtils.getString(getActivity(), "phone", "");
            String headImgUrl = PreferencesUtils.getString(getActivity(), "headImgUrl", "");

            tvLogin.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.VISIBLE);

            x.image().bind(ivHead, headImgUrl);
            tvAccount.setText(phone);
            tvName.setText(nickName);
        } else {
            ivHead.setImageResource(R.drawable.user_photo);
            tvLogin.setVisibility(View.VISIBLE);
            llUserInfo.setVisibility(View.GONE);
        }
    }

    @Event(R.id.iv_head)
    private void onHeadClick(View view) {
        //判断是否登录
        if (ClientUtils.isLogin(getActivity())) {
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Event(R.id.tv_login)
    private void onLoginClick(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    //我的订单
    @Event(R.id.tv_order)
    private void onOrderClick(View view) {
        startActivity(new Intent(getActivity(), OrderListActivity.class));
    }

    //待付款
    @Event(R.id.rl_unpaid)
    private void onUnpaidOrderClick(View view) {

    }

    //待提车
    @Event(R.id.rl_uncar)
    private void onUncarOrderClick(View view) {

    }

    //待收货
    @Event(R.id.rl_unreceipt)
    private void onUnreceiptOrderClick(View view) {

    }

    //待评价
    @Event(R.id.rl_comment)
    private void onCommentOrderClick(View view) {

    }

    //售后
    @Event(R.id.rl_service)
    private void onServiceOrderClick(View view) {

    }

    //收货地址
    @Event(R.id.tv_address)
    private void onAddressClick(View view) {
        startActivity(new Intent(getActivity(), AddressListActivity.class));
    }

    //足迹
    @Event(R.id.tv_foot)
    private void onFootClick(View view) {
        Toast.makeText(getActivity(), "足迹", Toast.LENGTH_SHORT).show();
    }

    //客服
    @Event(R.id.tv_custom)
    private void onCustomClick(View view) {
        Toast.makeText(getActivity(), "客服", Toast.LENGTH_SHORT).show();
    }

    //关于
    @Event(R.id.tv_about)
    private void onAboutClick(View view) {
        Toast.makeText(getActivity(), "关于", Toast.LENGTH_SHORT).show();
    }
}
