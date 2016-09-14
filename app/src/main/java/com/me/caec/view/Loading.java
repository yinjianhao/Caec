package com.me.caec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * loading
 * <p>
 * Created by yin on 2016/9/7.
 */
public class Loading extends LinearLayout {

    private Context context;
    private ImageView ivLoad;
    private TextView tvText;

    public Loading(Context context) {
        super(context);

        this.context = context;
        initView();
    }

    public Loading(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
    }

    public Loading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.loading, null);
        addView(view);

        ivLoad = (ImageView) view.findViewById(R.id.iv_load);
        tvText = (TextView) view.findViewById(R.id.tv_text);

        //避免IDE预览报错
        if (!isInEditMode()) {
            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setIgnoreGif(false);
            ImageOptions op = builder.build();

            x.image().bind(ivLoad, "assets://loading.gif", op);
        }

        setGravity(Gravity.CENTER);
    }
}
