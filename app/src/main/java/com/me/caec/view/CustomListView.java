package com.me.caec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @auther yjh
 * @date 2016/9/20
 */

public class CustomListView extends ListView {

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("aaaa", "onInterceptTouchEvent");

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getX();
                Log.d("aaaa", "startX - endX --->" + Math.abs(startX - endX));

                if (Math.abs(startX - endX) == 0) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
            default:
                startX = endX = 0;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
