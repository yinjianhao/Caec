package com.me.caec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by yin on 2016/9/9.
 */
public class CustomLinearLayout extends LinearLayout {

    private float startX;
    private float startY;
    private float endX;
    private float endY;
    HorizontalScrollView a;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getX();

                if (Math.abs(startX - endX) > 5) {
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            default:
                startX = endX = 0;
                break;
        }
        return true;
    }
}
