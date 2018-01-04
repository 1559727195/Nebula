package com.zhongtianli.nebula.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.zhongtianli.nebula.adapter.FragmentViewPagerAdapter;
import com.zhongtianli.nebula.bean.LoginProjects;

import java.util.ArrayList;

public class DeviceManagerViewPagerActivity extends DeviceManagerActivity {


    @Override
    protected PagerAdapter getPagerAdapter() {
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                mContentPager, mFragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void showNebula_GetLoginProjects(ArrayList<LoginProjects> list, boolean isFirst) {
        initLoginProjes(list);
        isFirstMain = isFirst;//首次成功拿到数据
    }

    @Override
    public void showNebula_LoginProjectsError(boolean isFirst) {
         isFirstMain = isFirst;
          count = 0;
    }
}
