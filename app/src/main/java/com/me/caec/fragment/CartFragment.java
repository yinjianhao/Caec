package com.me.caec.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.bean.CartList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 购物车
 * Created by yin on 2016/8/29.
 */

@ContentView(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tv_edit)
    private TextView tvEdit;

    @ViewInject(R.id.lv_cart)
    private ListView lvCart;

    //是否是编辑状态
    private boolean isEdit = false;

    @Override
    public void initData() {
        tvEdit.setOnClickListener(this);

        getCartList();

    }

    /**
     * 获取购物车列表
     */
    private void getCartList() {
        RequestParams params = new RequestParams(Client.CART_LIST_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(getActivity(), "token", ""));
        params.addQueryStringParameter("pageIndex", "1");
        params.addQueryStringParameter("pageSize", "20");

        x.http().get(params, new Callback.CommonCallback<CartList>() {
            @Override
            public void onSuccess(CartList result) {

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
            case R.id.tv_edit:

                break;
            default:
                break;
        }
    }
}
