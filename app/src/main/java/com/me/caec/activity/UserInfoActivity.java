package com.me.caec.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.UploadImage;
import com.me.caec.bean.UserInfo;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.ConfirmDialog;
import com.me.caec.view.TakePhotoPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.ll_head)
    private LinearLayout llHead;

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

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tv_login_out)
    private TextView tvLoginOut;

    @ViewInject(R.id.ll_modify)
    private LinearLayout llModify;

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        getUserInfo();

        tvTitle.setText("个人信息");
        tvBack.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        llModify.setOnClickListener(this);
        llHead.setOnClickListener(this);
    }

    private void getUserInfo() {

        Map<String, String> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(this, "token", ""));

        BaseClient.get(this, RequestUrl.USER_INFO_URL, map, UserInfo.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                UserInfo data = (UserInfo) result;
                if (data.getResult() == 0) {
                    UserInfo.DataBean dataBean = data.getData();
                    PreferencesUtils.setInt(UserInfoActivity.this, "sex", dataBean.getSex());
                    PreferencesUtils.setString(UserInfoActivity.this, "birthday", dataBean.getBirthday());
                    PreferencesUtils.setString(UserInfoActivity.this, "phone", dataBean.getMobile());
                    PreferencesUtils.setString(UserInfoActivity.this, "nickName", dataBean.getNickName());
                    PreferencesUtils.setString(UserInfoActivity.this, "headImgUrl", dataBean.getImg());

                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    builder.setCircular(true);
                    ImageOptions op = builder.build();

                    x.image().bind(ivHead, dataBean.getImg(), op);
                    tvNAme.setText(dataBean.getNickName());
                    tvPhone.setText(dataBean.getMobile());
                    tvSex.setText(dataBean.getSex() == 1 ? "男" : "女");
                    tvBirthday.setText(dataBean.getBirthday());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_login_out:
                loginOut();
                break;
            case R.id.ll_modify:
                startActivity(new Intent(this, ModifyPsdActivity.class));
                break;
            case R.id.ll_head:
                takePhoto(v);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照或从相册选
     */
    private void takePhoto(View v) {

        TakePhotoPopupWindow popupWindow = new TakePhotoPopupWindow(this, new TakePhotoPopupWindow.OnClickListener() {
            @Override
            public void onTakePhoto() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
            }

            @Override
            public void onPickPhoto() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, 0);
    }

    /**
     * 注销
     */
    private void loginOut() {
        ConfirmDialog dialog = new ConfirmDialog(UserInfoActivity.this);
        dialog.setBody("是否退出登录?");
        dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void confirm() {
                PreferencesUtils.removeInt(UserInfoActivity.this, "sex");
                PreferencesUtils.removeString(UserInfoActivity.this, "birthday");
//                PreferencesUtils.removeString(UserInfoActivity.this, "phone");
                PreferencesUtils.removeString(UserInfoActivity.this, "nickName");
                PreferencesUtils.removeString(UserInfoActivity.this, "headImgUrl");
                PreferencesUtils.removeString(UserInfoActivity.this, "token");
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        String token = PreferencesUtils.getString(this, "token", "");
        if (token.isEmpty()) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                crop(data.getData());
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                crop(bitmap);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");

                uploadImg(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     */
    private void uploadImg(final Bitmap bitmap) {
        String base64 = ImageUtils.Bitmap2Base64(bitmap);

        Map<String, Object> map = new HashMap<>();
        map.put("biz", "0");
        map.put("file", base64);
        map.put("token", PreferencesUtils.getString(this, "token", ""));

        BaseClient.post(this, RequestUrl.UPLOAD_IMAGE_URL, map, UploadImage.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                UploadImage data = (UploadImage) result;
                if (data.getResult() == 0) {
                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    builder.setCircular(true);
                    ImageOptions op = builder.build();

                    x.image().bind(ivHead, data.getData().getUrl(), op);
                } else {
                    Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(UserInfoActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void crop(Bitmap bitmap) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("data", bitmap);
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        intent.putExtra("outputFormat", "PNG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        intent.putExtra("outputFormat", "PNG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
