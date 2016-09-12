package com.me.caec.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.me.caec.R;

/**
 * 拍照时的PopupWindow
 * <p/>
 * Created by yin on 2016/9/12.
 */
public class TakePhotoPopupWindow extends PopupWindow implements View.OnClickListener {

    private OnClickListener listener;

    public TakePhotoPopupWindow(Context context, OnClickListener listener) {
        super(context);

        this.listener = listener;

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        View view = View.inflate(context, R.layout.popwindow_upload_img, null);
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setAnimationStyle(R.style.PopupAnimation);

        view.findViewById(R.id.btn_pick_photo).setOnClickListener(this);
        view.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
            case R.id.btn_pick_photo:
                if (listener != null) {
                    listener.onPickPhoto();
                }
                break;
            case R.id.btn_take_photo:
                if (listener != null) {
                    listener.onTakePhoto();
                }
                break;
            default:
                break;
        }

        dismiss();
    }

    public interface OnClickListener {
        void onTakePhoto();

        void onPickPhoto();
    }
}
