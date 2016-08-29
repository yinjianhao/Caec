package com.me.caec.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 首页
 * Created by yin on 2016/8/29.
 */
public abstract class BaseFragment extends Fragment {

    //根view
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = initView(inflater, container);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    public void initData() {

    }

    public View getRootView() {
        return rootView;
    }
}
