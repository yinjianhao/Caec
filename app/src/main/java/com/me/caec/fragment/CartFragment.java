package com.me.caec.fragment;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.BaseBean;
import com.me.caec.bean.CartList;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.CartDialog;
import com.me.caec.view.ConfirmDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<CartList.DataBean> cartList;
    private Adapter adapter;

    @Override
    public void initData() {
        tvEdit.setOnClickListener(this);

        getCartList();

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ConfirmDialog dialog = new ConfirmDialog(getActivity());
                dialog.setBody("你确定要删除此商品吗?");
                dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void confirm() {
                        deleteCart(position);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    /**
     * 删除购物车
     */
    private void deleteCart(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(getActivity(), "token", ""));
        String[] ids = new String[]{cartList.get(position).getCartItemId()};
        map.put("cartItemIds", Arrays.toString(ids));

        BaseClient.post(getActivity(), RequestUrl.CART_DELETE_URL, map, BaseBean.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                BaseBean data = (BaseBean) result;

                if (data.getResult() == 0) {
                    cartList.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取购物车列表
     */
    private void getCartList() {

        Map<String, String> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(getActivity(), "token", ""));
        map.put("pageIndex", "1");
        map.put("pageSize", "20");

        BaseClient.get(getActivity(), RequestUrl.CART_LIST_URL, map, CartList.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                CartList data = (CartList) result;

                if (data.getResult() == 0) {
                    cartList = data.getData();
                    if (adapter == null) {
                        adapter = new Adapter();
                        lvCart.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

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

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cartList.size();
        }

        @Override
        public CartList.DataBean getItem(int position) {
            return cartList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.listview_item_cart_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CartList.DataBean dataBean = getItem(position);

            x.image().bind(viewHolder.ivGood, dataBean.getImg(), ImageUtils.getDefaultImageOptions());
            viewHolder.tvGood.setText(dataBean.getName());
            viewHolder.tvDesc.setText(dataBean.getProp());
            viewHolder.tvNum.setText(String.valueOf(dataBean.getCount()));
            viewHolder.tvPrice.setText("¥" + String.valueOf(dataBean.getPrice()));
            viewHolder.tvOriginalPrice.setText("¥" + String.valueOf(dataBean.getOriginalPrice()));
            viewHolder.tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.tvOriginalPrice.setTag(position);

            return convertView;
        }
    }

    private class ViewHolder {

        public ImageView ivGood;
        public TextView tvGood;
        public TextView tvDesc;
        public TextView tvPrice;
        public TextView tvOriginalPrice;
        public TextView tvPlus;
        public TextView tvNum;
        public TextView tvAdd;

        public ViewHolder(View view) {
            ivGood = (ImageView) view.findViewById(R.id.iv_good);
            tvGood = (TextView) view.findViewById(R.id.tv_good);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tv_originalPrice);
            tvPlus = (TextView) view.findViewById(R.id.tv_plus);
            tvNum = (TextView) view.findViewById(R.id.tv_num);
            tvAdd = (TextView) view.findViewById(R.id.tv_add);

            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();

                    int num = Integer.parseInt(tvNum.getText().toString());

                    Log.d("CartFragment", String.valueOf(position));
                }
            });

            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();

                    int num = Integer.parseInt(tvNum.getText().toString());

                    Log.d("CartFragment", String.valueOf(position));

                    CartDialog cartDialog = new CartDialog(getActivity());
                    cartDialog.show();
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();

                    int num = Integer.parseInt(tvNum.getText().toString());

                    Log.d("CartFragment", String.valueOf(position));
                }
            });
        }
    }
}
