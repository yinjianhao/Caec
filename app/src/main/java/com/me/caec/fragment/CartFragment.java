package com.me.caec.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.me.caec.R;
import com.me.caec.activity.ConfirmOrderActivity;
import com.me.caec.bean.BaseBean;
import com.me.caec.bean.CartList;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.NumberUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.CartDialog;
import com.me.caec.view.ConfirmDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @ViewInject(R.id.cb_check_all)
    private CheckBox cbCheckAll;

    @ViewInject(R.id.tv_price)
    private TextView tvPrice;

    @ViewInject(R.id.tv_label)
    private TextView tvLabel;

    @ViewInject(R.id.btn_settlement)
    private Button btnSettlement;

    @ViewInject(R.id.btn_delete)
    private Button btnDelete;

    //是否是编辑状态
    private boolean isEdit = false;
    private List<CartList.DataBean> cartList;
    private Adapter adapter;

    //当前选中的
    private List<Integer> CheckedList = new ArrayList<>();

    //删除时,当前选中的
    private List<Integer> deleteList = new ArrayList<>();

    @Override
    public void initData() {
        tvEdit.setOnClickListener(this);
        btnSettlement.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        adapter = new Adapter();
        lvCart.setAdapter(adapter);

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

        cbCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    deleteList.clear();
                    if (cbCheckAll.isChecked()) {
                        for (int i = 0, l = adapter.getCount(); i < l; i++) {
                            deleteList.add(i);
                        }
                    }
                } else {
                    CheckedList.clear();
                    if (cbCheckAll.isChecked()) {
                        for (int i = 0, l = adapter.getCount(); i < l; i++) {
                            CheckedList.add(i);
                        }
                    }
                    countPrice();
                }
                adapter.notifyDataSetChanged();
            }
        });

        getCartList();
    }

    /**
     * 删除购物车
     */
    private void deleteCartMutil() {

        if (deleteList.size() == 0) {
            Toast.makeText(getActivity(), "你还没有选择商品哦!", Toast.LENGTH_SHORT).show();
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog(getActivity());
        dialog.setBody("你确定要删除这些商品吗?");
        dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void confirm() {
                Map<String, Object> map = new HashMap<>();
                map.put("token", PreferencesUtils.getString(getActivity(), "token", ""));

                String id = "[";
                id = id + "\"" + cartList.get(0).getCartItemId() + "\"";
                for (int i = 1, l = deleteList.size(); i < l; i++) {
                    id = id + ",\"" + cartList.get(i).getCartItemId() + "\"";
                }
                id += "]";

                map.put("cartItemIds", id);

                BaseClient.post(getActivity(), RequestUrl.CART_DELETE_URL, map, BaseBean.class, new BaseClient.BaseCallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        BaseBean data = (BaseBean) result;

                        if (data.getResult() == 0) {
                            for (int position : deleteList) {
                                cartList.remove(position);
                                if (CheckedList.contains(position)) {
                                    CheckedList.remove((Integer) position);
                                }
                            }
                            deleteList.clear();
                            adapter.notifyDataSetChanged();
//                            tvEdit.performClick();
                        } else {
                            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
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
            public void cancel() {

            }
        });
        dialog.show();
    }

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
                    adapter.notifyDataSetChanged();
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
                edit();
                break;
            case R.id.btn_settlement:
                goConfirmOrder();
                break;
            case R.id.btn_delete:
                deleteCartMutil();
                break;
            default:
                break;
        }
    }

    /**
     * 编辑
     */
    private void edit() {
        isEdit = !isEdit;
        if (isEdit) {
            tvEdit.setText("完成");
            tvLabel.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            btnSettlement.setVisibility(View.GONE);

            cbCheckAll.setChecked(false);
            deleteList.clear();
        } else {
            tvEdit.setText("编辑");
            tvLabel.setVisibility(View.VISIBLE);
            tvPrice.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            btnSettlement.setVisibility(View.VISIBLE);

            cbCheckAll.setChecked(CheckedList.size() == cartList.size());
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 去确认订单页面
     */
    private void goConfirmOrder() {
        if (CheckedList.size() == 0) {
            Toast.makeText(getActivity(), "您还没有选择商品哦!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        String cartId = "[";

        for (int i = 0, l = CheckedList.size(); i < l; i++) {
            int position = CheckedList.get(i);
            CartList.DataBean dataBean = cartList.get(position);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", dataBean.getId());
            jsonObject.put("count", dataBean.getCount());
            jsonObject.put("optionalInfo", "[]");
            jsonArray.add(jsonObject);

            if (i == 0) {
                cartId = cartId + "\"" + cartList.get(0).getCartItemId() + "\"";
            } else {
                cartId = cartId + ",\"" + cartList.get(i).getCartItemId() + "\"";
            }
        }
        cartId += "]";

        Intent i = new Intent(getActivity(), ConfirmOrderActivity.class);
        i.putExtra("params", jsonArray.toString());
        i.putExtra("cartId", cartId);
        getActivity().startActivity(i);
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cartList == null ? 0 : cartList.size();
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

            viewHolder.position = position;   //设置当前位置
            viewHolder.cbCheck.setChecked(isEdit ? deleteList.contains(position) : CheckedList.contains(position));
            x.image().bind(viewHolder.ivGood, dataBean.getImg(), ImageUtils.getDefaultImageOptions());
            viewHolder.tvGood.setText(dataBean.getName());
            viewHolder.tvDesc.setText(dataBean.getProp());
            viewHolder.tvNum.setText(String.valueOf(dataBean.getCount()));
            viewHolder.tvPrice.setText("¥" + NumberUtils.toFixed2(dataBean.getPrice()));
            viewHolder.tvOriginalPrice.setText("¥" + NumberUtils.toFixed2(dataBean.getOriginalPrice()));
            viewHolder.tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.tvOriginalPrice.setTag(position);

            viewHolder.tvPlus.setEnabled(dataBean.getCount() > 1);
            if (dataBean.getCount() >= dataBean.getStock() || dataBean.getCount() >= 99) {
                viewHolder.tvAdd.setEnabled(false);
            } else {
                viewHolder.tvAdd.setEnabled(true);
            }

            return convertView;
        }
    }

    private class ViewHolder {

        int position;

        RelativeLayout rlCheck;
        CheckBox cbCheck;
        ImageView ivGood;
        TextView tvGood;
        TextView tvDesc;
        TextView tvPrice;
        TextView tvOriginalPrice;
        TextView tvPlus;
        TextView tvNum;
        TextView tvAdd;

        ViewHolder(View view) {
            rlCheck = (RelativeLayout) view.findViewById(R.id.rl_check);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            ivGood = (ImageView) view.findViewById(R.id.iv_good);
            tvGood = (TextView) view.findViewById(R.id.tv_good);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tv_originalPrice);
            tvPlus = (TextView) view.findViewById(R.id.tv_plus);
            tvNum = (TextView) view.findViewById(R.id.tv_num);
            tvAdd = (TextView) view.findViewById(R.id.tv_add);

            rlCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdit) {       //编辑
                        if (cbCheck.isChecked()) {
                            deleteList.remove((Integer) position);
                            cbCheck.setChecked(false);
                        } else {
                            deleteList.add(position);
                            cbCheck.setChecked(true);
                        }

                        //是否全选
                        cbCheckAll.setChecked(deleteList.size() == adapter.getCount());
                    } else {
                        if (cbCheck.isChecked()) {
                            CheckedList.remove((Integer) position);
                            cbCheck.setChecked(false);
                        } else {
                            CheckedList.add(position);
                            cbCheck.setChecked(true);
                        }

                        //是否全选
                        cbCheckAll.setChecked(CheckedList.size() == adapter.getCount());

                        countPrice();  //计算总价
                    }
                }
            });

            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();
                    CartList.DataBean dataBean = cartList.get(position);

                    int count = dataBean.getCount();
                    int stock = dataBean.getStock();

                    count--;
                    if (count > stock) {
                        count = stock;
                    }

                    dataBean.setCount(count);

                    if (!CheckedList.contains(position)) {
                        CheckedList.add(position);
                    }
                    countPrice();
                    adapter.notifyDataSetChanged();
                }
            });

            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (int) tvOriginalPrice.getTag();
                    final CartList.DataBean dataBean = cartList.get(position);

                    final int count = dataBean.getCount();
                    int stock = dataBean.getStock();

                    CartDialog cartDialog = new CartDialog(getActivity(), count, stock);
                    cartDialog.setOnOperationListener(new CartDialog.OnOperationListener() {
                        @Override
                        public void confirm(int num) {
                            dataBean.setCount(num);
                            if (!CheckedList.contains(position)) {
                                CheckedList.add(position);
                            }
                            countPrice();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    cartDialog.show();
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();
                    CartList.DataBean dataBean = cartList.get(position);

                    int count = dataBean.getCount();

                    count++;
                    dataBean.setCount(count);

                    if (!CheckedList.contains(position)) {
                        CheckedList.add(position);
                    }
                    countPrice();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 计算总价
     */
    private void countPrice() {

        float price = 0;

        for (int position : CheckedList) {
            CartList.DataBean dataBean = cartList.get(position);
            price += dataBean.getCount() * dataBean.getPrice();
        }

        btnSettlement.setText("去结算(" + CheckedList.size() + ")");
        tvPrice.setText("¥" + NumberUtils.toFixed2(price));
    }
}
