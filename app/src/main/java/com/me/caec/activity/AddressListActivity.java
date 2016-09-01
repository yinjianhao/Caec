package com.me.caec.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.R;
import com.me.caec.bean.AddressList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class AddressListActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.lv_address)
    private ListView lvAddress;

    @ViewInject(R.id.btn_new)
    private Button btnNew;

    //地址列表数据
    private List<AddressList.DataBean> addressList;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("收货地址管理");
        tvBack.setOnClickListener(this);

        getAddressList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取地址列表
     */
    private void getAddressList() {
        RequestParams params = new RequestParams(Client.ADDRESS_LIST_URL);
        params.addBodyParameter("token", PreferencesUtils.getString(this, "token", ""));

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                AddressList list = JSON.parseObject(string, AddressList.class);
                if (list.getResult() == 0) {
                    addressList = list.getData();
                    adapter = new Adapter();
                    lvAddress.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return addressList.size();
        }

        @Override
        public Object getItem(int position) {
            return addressList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(AddressListActivity.this, R.layout.listview_item_address_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            AddressList.DataBean dataBean = addressList.get(position);
            viewHolder.tvReceiver.setText(dataBean.getReceiver());
            viewHolder.tvPhone.setText(dataBean.getMobile());
            if (dataBean.isIsDeafault()) {
                viewHolder.tvDefault.setVisibility(View.VISIBLE);
                viewHolder.cbCheck.setChecked(true);
            }
            viewHolder.tvAddressZip.setText(dataBean.getAddress() + "  " + dataBean.getZip());
            return convertView;
        }
    }

    private class ViewHolder {

        public TextView tvReceiver;
        public TextView tvPhone;
        public TextView tvDefault;
        public TextView tvAddressZip;
        private CheckBox cbCheck;
        private Button btnEdit;
        private Button btnDelete;

        public ViewHolder(View view) {
            tvReceiver = (TextView) view.findViewById(R.id.tv_receiver);
            tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            tvDefault = (TextView) view.findViewById(R.id.tv_default);
            tvAddressZip = (TextView) view.findViewById(R.id.tv_address_zip);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            btnEdit = (Button) view.findViewById(R.id.btn_edit);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);
        }
    }
}
