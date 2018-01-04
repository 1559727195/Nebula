package com.zhongtianli.nebula.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.adapter.Xlist_View_getDevice_remote_Adapter;
import com.zhongtianli.nebula.finddreams.network.NetUtils;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.view.IShowNebula_VisitorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhu on 2016/12/13.
 */
public class DeviceRemoteWorkActivity extends AppCompatActivity implements IShowNebula_VisitorView{
    private Toolbar toolbar_remote;
    private List<ConcurrentHashMap> remote_control_list = new ArrayList<>();//远程控制设备列表
    private String TAG = "robin debug";
    private ListView remote_list;//listView刷新
    private FlightInfoPresenter flightInfoPresenter;
    private List<Map> listMap;
    private Dialog dialog;//远程控制进度
    private Timer timer;//定时器
    private MyTimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_remote_work);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        remote_control_list = (List<ConcurrentHashMap>) getIntent().getSerializableExtra("remote_control");//远程控制设备列表
        listMap = new ArrayList<>();
        for (Map concurrentHashMap : remote_control_list) {
            Map map = new HashMap();
            map.put("deviceMac", concurrentHashMap.get("deviceMac"));
            map.put("deviceName", concurrentHashMap.get("deviceName"));
            map.put("deviceType", concurrentHashMap.get("deviceType"));
            map.put("paramId", concurrentHashMap.get("paramId"));//先添加这4个字段
            map.put("type", concurrentHashMap.get("type"));//为ListView添加不同的Item布局文件、
            listMap.add(map);
        }
        Log.e(TAG,"remote_control_list.size():"+remote_control_list.size());
    }

    private void initEvent() {
        toolbar_remote.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    private void initView() {
        toolbar_remote = (Toolbar) findViewById(R.id.toolbar_remote_control);
        setSupportActionBar(toolbar_remote);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        flightInfoPresenter = new FlightInfoPresenter(this);
        remote_list = (ListView) findViewById(R.id.remote_list);//远程控制列表显示\
        remote_list.setAdapter(new Xlist_View_getDevice_remote_Adapter(DeviceRemoteWorkActivity.this,listMap,
                new Xlist_View_getDevice_remote_Adapter.RemoteVisitorToDeviceAct() {

                    @Override
                    public void remoteVisitorToDeviceAct(Map map) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                //在2s秒内搜索蓝牙设备列表
                                dialog = createLoadingDialog(DeviceRemoteWorkActivity.this, "正在加载");//创建 loadingDialog
                                dialog.show();
                                dialogFullScreen();//让loadingDialog全屏
                                Looper.loop();
                            }
                        }).start();
                        new LoginThread().start();//防止断网时，loading进度无响应，5秒后消失
                        flightInfoPresenter.nebula_remote_control(DeviceRemoteWorkActivity.this, map);//拿到远程访客列表
                    }
                }
        ));
    }

    private void dialogFullScreen() {//让loadingDialog全屏
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height = display.getHeight();//设置高度
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    @Override//拿到远程设备列表
    public void showNebula_visitorView_success(String Result) {
        dialog.dismiss();
    }

    @Override//拿到远程设备列表失败
    public void showNebula_visitorView_Error() {
        //加一个判断网络状况
        boolean isConnected = NetUtils.isNetworkConnected(DeviceRemoteWorkActivity.this);
        if (!isConnected) {
             Toast.makeText(DeviceRemoteWorkActivity.this,"网络已断开",Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },300);
    }

    //    第二种方法相对来说就比较复杂了，你需要自定义请求超时操作
//    1）自定义一个TimerTask，用于向handler发送请求超时消息

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handlerTimeOut.sendEmptyMessage(5000);
        }
    }

//    2）定义一个设置请求超时的方法，超时时间为5秒

    private void checkTimeOut() {
        try {
            timer = new Timer();
            task = new MyTimerTask();
            timer.schedule(task, 5000);//延时5秒在执行task
        } catch (Exception e) {
            Log.e("timer", e.getMessage());
        }
    }

//    3）开发登录线程

    private class LoginThread extends Thread {
        @Override
        public void run() {
            try {
                checkTimeOut();
            } catch (Exception e) {
                Log.e("LoginThread", e.getMessage());
            }
        }
    }

//    在handler中处理请求超时或者请求成功的操作

    Handler handlerTimeOut = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5000:
                    //关掉timer
                    timer.cancel();
                    //处理请求超时时要做的操作
                    dialog.dismiss();
                    break;
            }
        }
    };
}
