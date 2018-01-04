package com.zhongtianli.nebula.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by zhu on 2016/11/7.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {//实现监听网络连接情况
    @Override
    public void onReceive(Context context, Intent intent) {//注册广播接收器
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//            Toast.makeText(context,"网络不可用",Toast.LENGTH_SHORT).show();
            //改变背景或者 处理网络的全局变量
        }else {
            //改变背景或者 处理网络的全局变量
//            Toast.makeText(context,"网络可以了",Toast.LENGTH_SHORT).show();
        }
    }
}
