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

import com.me.caec.R;
import com.me.caec.globle.RequestAddress;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.ConfirmDialog;
import com.me.caec.view.TakePhotoPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
        String token = PreferencesUtils.getString(this, "token", "");
        RequestParams params = new RequestParams(RequestAddress.USER_INFO_URL);
        params.addQueryStringParameter("token", token);

        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.getInt("result") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        PreferencesUtils.setInt(UserInfoActivity.this, "sex", data.getInt("sex"));
                        PreferencesUtils.setString(UserInfoActivity.this, "birthday", data.getString("birthday"));
                        PreferencesUtils.setString(UserInfoActivity.this, "phone", data.getString("mobile"));
                        PreferencesUtils.setString(UserInfoActivity.this, "nickName", data.getString("nickName"));
                        PreferencesUtils.setString(UserInfoActivity.this, "headImgUrl", data.getString("img"));

                        x.image().bind(ivHead, data.getString("img"));
                        tvNAme.setText(data.getString("nickName"));
                        tvPhone.setText(data.getString("mobile"));
                        tvSex.setText(data.getInt("sex") == 1 ? "男" : "女");
                        tvBirthday.setText(data.getString("birthday"));
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

    private void loginOut() {
        ConfirmDialog dialog = new ConfirmDialog(UserInfoActivity.this);
        dialog.setBody("是否退出登录?");
        dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void confirm() {
                PreferencesUtils.removeInt(UserInfoActivity.this, "sex");
                PreferencesUtils.removeString(UserInfoActivity.this, "birthday");
                PreferencesUtils.removeString(UserInfoActivity.this, "phone");
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

        RequestParams params = new RequestParams(RequestAddress.UPLOAD_IMAGE_URL);
        params.addQueryStringParameter("biz", "0");
        params.addQueryStringParameter("file", base64);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("result") == 0) {
                        Log.d("upload", jsonObject.getJSONObject("data").getString("url"));
                        ivHead.setImageBitmap(bitmap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

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
