package com.me.caec.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.me.caec.bean.BaseBean;
import com.me.caec.bean.Location;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.LocationUtils;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private EditText etReceiver;

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

    private List<Location.DataBean> provinceList;
    private String[] provinceNameArray;
    private int provinceId;
    private int tempProvinceId;  //缓存id,当选择市之后才替换provinceId
    private String provinceName;

    private List<Location.DataBean.CityBean> cityList;
    private String[] cityNameArray;
    private int cityId;

    private List<Location.DataBean.CityBean.AreaBean> areaList;
    private String[] areaNameArray;
    private int areaId;

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
            setEditInfo();
        }

        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        etProCity.setOnClickListener(this);
        etArea.setOnClickListener(this);
    }

    /**
     * 填入编辑的信息
     */
    private void setEditInfo() {
        etReceiver.setText(dataBean.getReceiver());
        etReceiver.setSelection(dataBean.getReceiver().length());
        etPhone.setText(dataBean.getMobile());
        etZip.setText(dataBean.getZip());
        etProCity.setText(dataBean.getProvinceName() + dataBean.getCityName());
        provinceId = dataBean.getProvinceId();
        cityId = dataBean.getCityId();
        etArea.setText(dataBean.getAreaName());
        areaId = dataBean.getAreaId();
        etAddress.setText(dataBean.getAddress());
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
            case R.id.et_pro_city:
                showProvinceDialog();
                break;
            case R.id.et_area:
                if (cityId == 0) {
                    Toast.makeText(this, "请先选择省市", Toast.LENGTH_SHORT).show();
                } else {
                    showAreaDialog();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 选择省份弹出框
     */
    private void showProvinceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择省份");
        if (provinceList == null) {
            provinceList = LocationUtils.getLocation(this);
            if (provinceList != null) {
                int length = provinceList.size();
                provinceNameArray = new String[length];
                for (int i = 0; i < length; i++) {
                    provinceNameArray[i] = provinceList.get(i).getAreaname();
                }
            }
        }

        builder.setSingleChoiceItems(provinceNameArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempProvinceId = provinceList.get(which).getAreaid();
                provinceName = provinceList.get(which).getAreaname();
                showCityDialog();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 选择市弹出框
     */
    private void showCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择市");

        cityList = LocationUtils.findCityBeanWithProvinceId(provinceList, tempProvinceId);
        if (cityList != null) {
            int length = cityList.size();
            cityNameArray = new String[length];
            for (int i = 0; i < length; i++) {
                cityNameArray[i] = cityList.get(i).getAreaname();
            }
        }

        builder.setSingleChoiceItems(cityNameArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                provinceId = tempProvinceId;
                cityId = cityList.get(which).getAreaid();
                //填入省市信息
                etProCity.setText(provinceName + cityList.get(which).getAreaname());
                //清空区县
                etArea.setText("");
                areaId = 0;
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 选择区县弹出框
     */
    private void showAreaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择区县");

        areaList = LocationUtils.findAreaBeanWithcityId(cityList, cityId);
        if (areaList != null) {
            int length = areaList.size();
            areaNameArray = new String[length];
            for (int i = 0; i < length; i++) {
                areaNameArray[i] = areaList.get(i).getAreaname();
            }
        }

        builder.setSingleChoiceItems(areaNameArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                areaId = areaList.get(which).getAreaid();
                etArea.setText(areaList.get(which).getAreaname());
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 发起请求，提交信息
     */
    private void confirm() {
        final String receiver = etReceiver.getText().toString();
        String mobile = etPhone.getText().toString();
        String zip = etZip.getText().toString();
        String address = etAddress.getText().toString();

        if (receiver.isEmpty() || mobile.isEmpty() || zip.isEmpty() || address.isEmpty() || provinceId == 0 || cityId == 0 || areaId == 0) {
            Toast.makeText(this, "请正确填写全部信息", Toast.LENGTH_SHORT).show();
            return;
        }

        //发起请求
        Map<String, Object> map = new HashMap<>();
        String url = RequestUrl.CREATE_ADDRESS_URL;
        if (type == FLAG_ADDRESS_EDIT) {
            map.put("id", dataBean.getId());
            url = RequestUrl.EDIT_ADDRESS_URL;
        }
        map.put("token", PreferencesUtils.getString(this, "token", ""));
        map.put("receiver", receiver);
        map.put("mobile", mobile);
        map.put("zip", zip);
        map.put("address", address);
        map.put("provinceId", String.valueOf(provinceId));
        map.put("cityId", String.valueOf(cityId));
        map.put("areaId", String.valueOf(areaId));

        BaseClient.post(this, url, map, BaseBean.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                BaseBean data = (BaseBean) result;

                if (data.getResult() == 0) {
                    Toast.makeText(getApplicationContext(), "保存地址成功", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.putExtra("reload", true);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "保存地址失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
