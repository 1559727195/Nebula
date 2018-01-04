package com.zhongtianli.nebula;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.ciaapp.sdk.CIAService;

import static com.zhongtianli.nebula.R.menu.main;

/**
 * Created by zhu on 2016/11/10.
 */

public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化传入appId和authKey
        CIAService.init(this, "4f3bcea860f843d1bbe35eb4976edd33", "124cf2f0457c4ca68b73c9bcd11b21e7");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
