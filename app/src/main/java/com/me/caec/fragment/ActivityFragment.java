package com.me.caec.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.caec.R;

/**
 * 活动
 * Created by yin on 2016/8/29.
 */
public class ActivityFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void initData() {

    }
}
