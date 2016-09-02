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
import com.me.caec.bean.AddressList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 创建或修改地址
 */
public class CreateAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    @ViewInject(R.id.et_receiver)
    private EditText etRecriver;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_zip)
    private EditText etZip;

    @ViewInject(R.id.et_pro_city)
    private EditText etProCity;

    @ViewInject(R.id.et_area)
    private EditText etArea;

    @ViewInject(R.id.et_address)
    private EditText etAddress;

    //创建地址
    public static final int FLAG_ADDRESS_CREATE = 0;

    //修改地址
    public static final int FLAG_ADDRESS_EDIT = 1;

    //创建 or 修改  默认为创建
    private int type;

    //修改地址时,传过来的地址数据
    private AddressList.DataBean dataBean;

    //省市区id
    private String provinceId;
    private String cityId;
    private String areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        Intent i = getIntent();
        type = i.getIntExtra("type", FLAG_ADDRESS_CREATE);//默认为新建地址
        if (type == FLAG_ADDRESS_CREATE) {
            tvTitle.setText("新建收货地址");
        } else {
            tvTitle.setText("修改收货地址");
            btnConfirm.setText("保存");
            dataBean = (AddressList.DataBean) i.getSerializableExtra("data");
        }

        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
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
        String receiver = etRecriver.getText().toString();
        String mobile = etRecriver.getText().toString();
        String zip = etRecriver.getText().toString();
        String address = etRecriver.getText().toString();

        if (receiver.isEmpty() || mobile.isEmpty() || zip.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "请正确填写全部信息", Toast.LENGTH_SHORT).show();
            return;
        }

        //发起请求
        RequestParams params = new RequestParams(type == FLAG_ADDRESS_CREATE ? Client.CREATE_ADDRESS_URL : Client.EDIT_ADDRESS_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("receiver", receiver);
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("zip", zip);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("provinceId", provinceId);
        params.addQueryStringParameter("cityId", cityId);
        params.addQueryStringParameter("areaId", areaId);

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int result = jsonObject.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();

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

    /**
     * 提交修改
     */
    private void confirmEdit() {

    }

    /**
     * 提交新建
     */
    private void confirmCreate() {

    }
}
