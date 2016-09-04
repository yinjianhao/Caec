package com.me.caec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * 自定义checkbox
 * 只响应点击事件,不改变状态
 * <p/>
 * Created by yin on 2016/9/2.
 */
public class CustomCheckBox extends CheckBox {

    public CustomCheckBox(Context context) {
        super(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return callOnClick();
    }
}
