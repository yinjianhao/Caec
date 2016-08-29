package com.me.caec.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.caec.R;

/**
 * 首页
 * Created by yin on 2016/8/29.
 */
public class HomeFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData() {

    }
}
