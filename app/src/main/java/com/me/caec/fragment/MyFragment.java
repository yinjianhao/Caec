package com.me.caec.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.activity.AddressListActivity;
import com.me.caec.activity.CartActivity;
import com.me.caec.activity.LoginActivity;
import com.me.caec.activity.OrderListActivity;
import com.me.caec.activity.UserInfoActivity;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.ClientUtils;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
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

    @ViewInject(R.id.tv_unpaid_count)
    private TextView tvUnpaidCount;

    @ViewInject(R.id.tv_uncar_count)
    private TextView tvUncarCount;

    @ViewInject(R.id.tv_unreceipt_count)
    private TextView tvUnreceiptCount;

    @ViewInject(R.id.tv_comment_count)
    private TextView tvCommentCount;

    @ViewInject(R.id.tv_service_count)
    private TextView tvServiceCount;

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();

        setUserInfo();
        getOrderCount();
    }

    /**
     * 获取订单数量
     */
    private void getOrderCount() {
        if (ClientUtils.isLogin(getActivity())) {
            RequestParams params = new RequestParams(RequestUrl.ORDER_NUM_URL);
            params.addQueryStringParameter("token", PreferencesUtils.getString(getActivity(), "token", ""));

            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getInt("result") == 0) {
                            json = json.getJSONObject("data");
                            int count;
                            if ((count = json.getInt("unpaid")) > 0) {
                                tvUnpaidCount.setVisibility(View.VISIBLE);
                                tvUnpaidCount.setText(String.valueOf(count));
                            } else {
                                tvUnpaidCount.setVisibility(View.GONE);
                            }

                            if ((count = json.getInt("unreceived")) > 0) {
                                tvUnreceiptCount.setVisibility(View.VISIBLE);
                                tvUnreceiptCount.setText(String.valueOf(count));
                            } else {
                                tvUnreceiptCount.setVisibility(View.GONE);
                            }

                            if ((count = json.getInt("unselfservice")) > 0) {
                                tvUncarCount.setVisibility(View.VISIBLE);
                                tvUncarCount.setText(String.valueOf(count));
                            } else {
                                tvUncarCount.setVisibility(View.GONE);
                            }

                            if ((count = json.getInt("unassess")) > 0) {
                                tvCommentCount.setVisibility(View.VISIBLE);
                                tvCommentCount.setText(String.valueOf(count));
                            } else {
                                tvCommentCount.setVisibility(View.GONE);
                            }

                            if ((count = json.getInt("applyback")) > 0) {
                                tvServiceCount.setVisibility(View.VISIBLE);
                                tvServiceCount.setText(String.valueOf(count));
                            } else {
                                tvServiceCount.setVisibility(View.GONE);
                            }
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

            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setCircular(true);
            ImageOptions op = builder.build();

            x.image().bind(ivHead, headImgUrl, op);
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

    /**
     * 跳向订单页面
     *
     * @param index 选中的tab索引
     */
    private void goOrder(int index) {
        if (ClientUtils.isLogin(getActivity())) {
            Intent i = new Intent(getActivity(), OrderListActivity.class);
            i.putExtra("index", index);
            startActivity(i);
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    //我的订单
    @Event(R.id.tv_order)
    private void onOrderClick(View view) {
        goOrder(0);
    }

    //待付款
    @Event(R.id.rl_unpaid)
    private void onUnpaidOrderClick(View view) {
        goOrder(1);
    }

    //待提车
    @Event(R.id.rl_uncar)
    private void onUncarOrderClick(View view) {
        goOrder(2);
    }

    //待收货
    @Event(R.id.rl_unreceipt)
    private void onUnreceiptOrderClick(View view) {
        goOrder(3);
    }

    //待评价
    @Event(R.id.rl_comment)
    private void onCommentOrderClick(View view) {
        goOrder(4);
    }

    //售后
    @Event(R.id.rl_service)
    private void onServiceOrderClick(View view) {
        goOrder(5);
    }

    //收货地址
    @Event(R.id.tv_address)
    private void onAddressClick(View view) {
        if (ClientUtils.isLogin(getActivity())) {
            startActivity(new Intent(getActivity(), AddressListActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
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
        startActivity(new Intent(getActivity(), CartActivity.class));
    }
}
