package com.me.caec.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.me.caec.R;

import org.xutils.image.ImageOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @auther yjh
 * @date 2016/9/11
 */
public class ImageUtils {

    /**
     * x.image()  默认参数,只设置 默认加载图片
     *
     * @return
     */
    public static ImageOptions getDefaultImageOptions() {
        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
        return builder.build();
    }

    /**
     * 图片bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String Bitmap2Base64(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] imgBytes = out.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}
