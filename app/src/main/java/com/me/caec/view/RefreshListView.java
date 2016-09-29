package com.me.caec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * @auther yjh
 * @date 2016/9/29
 */

public class RefreshListView extends ListView {
    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(final int visibility) {
        if (visibility != View.GONE || getCount() != 0)
            super.setVisibility(visibility);
    }
}
