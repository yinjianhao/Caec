package com.me.caec.utils;

import com.me.caec.R;

import org.xutils.image.ImageOptions;

/**
 * @auther yjh
 * @date 2016/9/11
 */
public class ImageUtils {

    /**
     * x.image()  默认参数,只设置加载图片
     * @return
     */
    public static ImageOptions getDefaultImageOptions() {
        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
        return builder.build();
    }
}
