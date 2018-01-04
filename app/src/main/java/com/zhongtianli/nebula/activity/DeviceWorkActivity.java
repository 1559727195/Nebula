package com.zhongtianli.nebula.activity;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.massky.ywx.ackpasslibrary.OnOpenDeviceListener;
import com.massky.ywx.ackpasslibrary.OnScanListener;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.adapter.Xlist_View_getDevice_scan_Adapter;
import com.zhongtianli.nebula.interfaces.AdapterRefreshList;
import com.zhongtianli.nebula.interfaces.IShowAdapterList;
import com.zhongtianli.nebula.interfaces.OnChangedListener;
import com.zhongtianli.nebula.interfaces.OnDialogFragmentforCompleted;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.maxwin.view.XListView;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import com.zhongtianli.nebula.utils.FinishUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhongtianli.nebula.R.id.text_select;

/**
 * Created by Administrator on 2016/10/23.
 */
public class DeviceWorkActivity extends AppCompatActivity implements OnDialogFragmentforCompleted,OnChangedListener
,XListView.IXListViewListener,OnScanListener {
    private final String TAG = "robin debug";
    private ImageView img_share1;
    private ImageView img_remove1;
    private Toolbar toolbar;
    private XListView mListView;
    private Xlist_View_getDevice_scan_Adapter xlist_adapter;
    private AckpassClass mAckpassClass;//蓝牙jar包
    private ArrayList<ConcurrentHashMap> listMap = new ArrayList<>();//存储蓝牙扫描的设备信息
    private String TAG1 = "zhu";
    Handler scanHandler = new Handler();
    private SQLiteUtils sqLiteUtils;//数据库操作
    private DatabaseHelper databaseHelper;//数据库句柄
    private int ScanTime = 1000;//蓝牙扫描时间，初始化时为500，刷新时为1000
    //本地数据库需要先存储三个字段：device_type设备类型 ，device_mac：MAC地址  ，type上传是否成功 1成功,0失败
    private final String TABLE_NAME_LIU = "projectCode";//
    private String projectCode;//可选的项目编码
    private Set<ConcurrentHashMap> setMap = new HashSet<>();////存储蓝牙扫描的设备信息
    private Dialog dialog; //创建弹出对话框
    private MediaPlayer mp;//打开蓝牙设备时playMusic
    private SharedPreferences sp;//用来读取打开设备时，music是否打开-》在SettingsActivity中设置是否打开
    private SharedPreferences spCodeProject;//读取从主界面，传过来的可选的projectCode
    private Timer timer;//超时timer
    private MyTimerTask task;//超时timerTask
    private TextView debug_send1;//发送
    private TextView debug_receive1;//接收
    private String debugMessage;////发送debug信息
    private int debugStatus;//接收状态码
    private final String DEBUG_TABLE = "debug" ;//debug表
    private RelativeLayout rela_share;//分享
    private RelativeLayout relati_remover;//删除
    private ImageView remote_contro;//远程控制开关
    private List<ConcurrentHashMap> remote_control_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_work_background);
        FinishUtil.addActivity(this);
        sp = getSharedPreferences("ToggleButton", MODE_PRIVATE);
        //当打开蓝牙设备成功时，playMusic
        mp = MediaPlayer.create(DeviceWorkActivity.this, R.raw.yi_zhanggong_shengyin);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //在2s秒内搜索蓝牙设备列表
                dialog = createLoadingDialog(DeviceWorkActivity.this, "正在加载");//创建 loadingDialog
                dialog.show();
                dialogFullScreen();//让loadingDialog全屏
                Looper.loop();
            }
        }).start();
        initRecieveBroadCastProjectCode();//接收来自MainAct传过来的可选projectCode
        CreateTable();//初始化数据库表
        new Thread(new Runnable() {//在这里添加异步，目的是防止打开蓝牙时，出现黑屏
            @Override
            public void run() {
                Looper.prepare();
                initLanYa();
                Looper.loop();
            }
        }).start();
        initView();
        initEvent();
        initData();
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

    private void initRecieveBroadCastProjectCode() {//接收来自MainAct传过来的可选projectCode
//        projectCode = getIntent().getStringExtra("ProjectCode");
        spCodeProject = getSharedPreferences("Nebula_GetDevice", MODE_PRIVATE);
        projectCode = spCodeProject.getString("ProjectCode", "11");
    }

    Handler loadingDeviceHandler = new Handler() {//在2.5s秒内搜索蓝牙设备列表
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3://停止扫蓝牙设备列表
                    dialog.dismiss();
                    break;
            }
        }
    };

    Handler handlerDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//关闭dialog
                dialog.dismiss();
            } else if (msg.what == 1) {//打开dialog
                //创建对话框
                dialog = createLoadingDialog(DeviceWorkActivity.this, "正在加载");//创建 loadingDialog
                dialog.show();
                dialogFullScreen();//让loadingDialog全屏
            }
        }
    };

    private void dialogFullScreen() {//让loadingDialog全屏
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height = display.getHeight();//设置高度
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    //蓝牙初始化
    private void initLanYa() {
        mAckpassClass = new AckpassClass(this);
        mAckpassClass.setOnScanListener(this);
        mAckpassClass.setOnOpenDeviceListener(new OnOpenDeviceListener() {
            @Override
            public void OnOpenDevice(int status) {
                handlerDialog.sendEmptyMessage(0);//关闭Dialog
                debugHandler.sendEmptyMessage(1);//发送1则说明是开始接收了
                 debugStatus = status;
                switch (status) {
                    case 0:
                        if (sp.getBoolean("TG", false))//在SettingsActivity中设置为true时，才可以playMusic
                            mp.start();
                        toast("成功");
                        break;
                    case 1:
                        toast("失败");
                        break;
                    case 2:
                        toast("连接设备失败");
                        break;
                    case 3:
                        toast("参数错误");
                        break;
                    case 4:
                        toast("其他异常");
                        break;
                }
            }
        });

        //检查一下如果不支持BLE，直接退出
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!mAckpassClass.Initialize()) {//没注册成功
            Toast.makeText(this, "Initialize fail", Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter.isEnabled()) {//true表示已经开启，false表示蓝牙并没启用。
            Log.e(TAG1, "blueadapter.isEnabled()");
            mayRequestLocation();//蓝牙扫描设备
        } else {//蓝牙没有开启或正在开启中
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mayRequestLocation();
                    Log.e(TAG1, "blueadapter.isUnEnabled()");
                }
            }, 1000);//延时一秒执行
        }
    }

    private static final int REQUEST_FINE_LOCATION=0;
    private void mayRequestLocation() {//提权防止蓝牙在高版本跑不了
        int versionint = Integer.parseInt(android.os.Build.VERSION.SDK);
        Log.e(TAG,"versionint:"+versionint);
        if (versionint >= 23) {
            //int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(DeviceWorkActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(DeviceWorkActivity.this,"ble_need", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_FINE_LOCATION);
                return;
            }else{
                //  scanLeDevice5(true);
                scanDevice();
            }
        }
        else if (versionint < 23 && versionint > 20){
            // scanLeDevice5(true);
            scanDevice();
        }
        else {
            scanDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.
                    // scanLeDevice5(true);
                    scanDevice();
                } else{
                    // The user disallowed the requested permission.
                    Toast.makeText(DeviceWorkActivity.this,"requested permission fail", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void CreateTable() {//创建表
        sqLiteUtils = new SQLiteUtils();
        databaseHelper = sqLiteUtils.createDBHelper(this);
    }

    private void scanDevice() {
        mAckpassClass.StartScan();//初始时开始扫描
        mHandler.postDelayed(r, 0);//延时100毫秒
    }

    private boolean canAddDel;//防止 listMap删除其中重复的元素时，其他地方还在给它添加新元素，
    // 定义Handler
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //handler处理消息
            if (msg.what == 0) {
                mAckpassClass.StopScan();//停止扫描
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        loadingDeviceHandler.sendEmptyMessage(3);//2s扫描设备列表结束
                        Looper.loop();
                    }
                }).start();
                Log.e(TAG1, listMap.size() + "->listMap");
                canAddDel = true;//防止 listMap删除其中重复的元素时，其他地方还在给它添加新元素，
//                HashSet<Map> hs = new HashSet<Map>(listMap); //此时已经去掉重复的数据保存在hashset中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canAddDel = false;
                    }
                }, 20);
                mHandler.removeCallbacks(r);
                Log.e(TAG1, setMap.size() + "->HashSet");
                Log.e(TAG, "貌似是时间到了");
                List<ConcurrentHashMap> list = new ArrayList<>();
                for (ConcurrentHashMap map : setMap) {
                    list.add(map);
                }
                if (projectCode != null) {//说明有网
                    list = sqlLanDeviceMac(list);//匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
                }
                //把蓝牙扫到的设备给远程控制remote_control_activity界面
                remote_control_list = new ArrayList<>();
                remote_control_list = list;
                //传过去
                xlist_adapter.setList(list);
                xlist_adapter.notifyDataSetChanged();
                Log.e(TAG, list.size() + "->notifyDataSetChanged");
            }
        }
    };

    private List<ConcurrentHashMap> sqlLanDeviceMac(List<ConcurrentHashMap> list) {
        //匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
        List<ConcurrentHashMap> sqlList = new ArrayList<>();//存储数据库中的设备信息
//        sqlList = sqLiteUtils.getAll(databaseHelper,TABLE_NAME_LIU);//拿到数据库项目下的所有设备
        sqlList = sqLiteUtils.getProjectCodeDeviceList(databaseHelper, projectCode, TABLE_NAME_LIU);//拿到可选项目编码projectCode下的设备列表
        list = getIntersection(list, sqlList);//从数据库中拿到的deviceMac
        return list;
    }

    //把两个list里边相同的数据取出
    public List<ConcurrentHashMap> getIntersection(List<ConcurrentHashMap> success,
                                                   List<ConcurrentHashMap> sq1List) {
        //拿到success里下的所有mac地址
        List<ConcurrentHashMap> result = new ArrayList<ConcurrentHashMap>();
        for (int j = 0; j < success.size(); j++) {//遍历list1
            for (int i = 0; i < sq1List.size(); i++) {
                if (success.get(j).get("deviceMac").equals(sq1List.get(i).get("deviceMac"))) {//从数据库中拿到的deviceMac
                    //和蓝牙扫描到的deviceMac相等
                    success.get(j).put("deviceName", sq1List.get(i).get("deviceName"));
                }
            }
        }
        result = success;
        return result;
    }

    int i = 0;
    // ler类的postDelayed方法：
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            i++;
            if (i > ScanTime) {
                i = 0;
                handler.sendEmptyMessage(0);
            }
            Log.e(TAG, i + "");
            mHandler.postDelayed(this, 1);
        }
    };

    private void initView() {
        remote_contro = (ImageView) findViewById(R.id.remote_control);//远程控制设备开关
        img_share1 = (ImageView) findViewById(R.id.img_share);
        rela_share = (RelativeLayout) findViewById(R.id.rela_share);//分享
        relati_remover = (RelativeLayout) findViewById(R.id.relati_remover);//删除
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        img_remove1 = (ImageView) findViewById(R.id.img_remove);
        mListView = (XListView) findViewById(R.id.xListView_scan);
        mListView.setFootViewHide();
        mListView.setPullLoadEnable(false);
        //debug-send发送
        debug_send1 = (TextView) findViewById(R.id.debug_send1);
        //debug-receive接收
        debug_receive1 = (TextView) findViewById(R.id.debug_receive1);
    }

    // 定义计划任务，根据参数的不同可以完成以下种类的工作：在固定时间执行某任务，在固定时间开始重复执行某任务，重复时间间隔可控，在延迟多久后执行某任务，在延迟多久后重复执行某任务，重复时间间隔可控

    private void initData() {//初始化蓝牙设备
        HashSet<ConcurrentHashMap> hs = new HashSet<ConcurrentHashMap>(listMap); //此时已经去掉重复的数据保存在hashset中
        List<ConcurrentHashMap> list = new ArrayList<>();
        for (ConcurrentHashMap map : hs) {
            list.add(map);
        }
        xlist_adapter = new Xlist_View_getDevice_scan_Adapter(DeviceWorkActivity.this, list, mAckpassClass,
                new AdapterRefreshList() {
                    @Override
                    public void refreshList(IShowAdapterList iShowAdapterList) {
                        iShowAdapterList.toDeviceAct(mAckpassClass);
                    }
                }, new Xlist_View_getDevice_scan_Adapter.LoadingDialogToDeviceAct() {//在这里搞一个点击蓝牙开关-加载的Dialog弹出框

            @Override
            public void loadingDialogToDevAct(String debugLog) {//在打开蓝牙设备的过程中
                handlerDialog.sendEmptyMessage(1);//打开dialog，正在打开的过程，即还在蓝牙设备附近，可能会有响应
                //我在这里添加一个超时线程，防止超过蓝牙设备范围时，OnOpenDevice(int status)回调方法还没有被执行，以至于loadingDialog
                //还在那打转
                debugMessage = debugLog;
                debugHandler.sendEmptyMessage(0);//发送0说明是发送debug
                new LoginThread().start();
            }
        });
        mListView.setAdapter(xlist_adapter);
        //蓝牙初始化附近设备信息
    }

    //打开蓝牙设备信息debug-Handler
    Handler debugHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    debug_send1.setText(debugMessage);//发送蓝牙设备开关信息或异常信息
                    break;//发送信息
                case 1://接收信息
                    switch (debugStatus) {
                        case 0:
                            debug_receive1.setText("成功");
                            break;
                        case 1:
                            debug_receive1.setText("失败");
                            break;
                        case 2:
                            debug_receive1.setText("连接设备失败");
                            break;
                        case 3:
                            debug_receive1.setText("参数错误");
                            break;
                        case 4:
                            debug_receive1.setText("其他异常");
                            break;
                    }
                    //把debug调试蓝牙信息插入到debug表中去,
                    insertdebug(debug_send1.getText().toString(),debug_receive1.getText().toString());
                    break;
            }
        }
    };

    //把debug调试蓝牙信息插入到debug表中去,
    private void insertdebug(String send, String receive) {
        Map map = new HashMap();
        map.put("send",send);//debug-发送的信息
        map.put("receive",receive);//debug-接收的信息
        sqLiteUtils.insertDebug(databaseHelper,map,DEBUG_TABLE);
    }

    private void initEvent() {
        //远程控制设备开关
       remote_contro.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
                     /*
				  * 在这里跳转到远程控制设备界面
				  * */
                    Intent intent_remote = new Intent(DeviceWorkActivity.this, DeviceRemoteWorkActivity.class);
                    intent_remote.putExtra("remote_control", (Serializable) remote_control_list);//远程控制列表给remote_controlActivity
                    //传过去
                    startActivity(intent_remote);
                    return true;
                } else {
                    return false;
                }
            }
        });
            mListView.setXListViewListener(this);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            img_share1.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                        setImg_share();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            img_remove1.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                        setTxt_remove();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

        rela_share.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                    setImg_share();
                    return true;
                } else {
                    return false;
                }
            }
        });
        relati_remover.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                    setTxt_remove();
                    return true;
                } else {
                    return false;
                }
            }
        });

        }

        //dialogFragment给activity传值
        @Override
        public void onDialogFragmentforCompleted(String userName, String passWord) {

        }

        @Override
        public void OnChanged(boolean CheckState) {//自定义左右画动按钮
            if (CheckState) {
                Toast.makeText(this, "打开了", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "关闭了", Toast.LENGTH_LONG).show();
            }
        }

        private void setTxt_remove() {//清除send和receive蓝牙设备的操作信息
            debug_receive1.setText("");
            debug_send1.setText("");
        }

        private void setImg_share() {
            //用Stringbuffer封装要分享的send，和receive字段
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("send:");
            stringBuffer.append(debug_send1.getText().toString());
            stringBuffer.append("\n");
            stringBuffer.append("receive:");
            stringBuffer.append(debug_receive1.getText().toString());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuffer.toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        private void onLoad() {
            mListView.stopRefresh();
            mListView.setRefreshTime("刚刚");
        }

        long mExitTime = 0;

        @Override
        public void onRefresh() {
            onLoad();
//        ScanTime = 1000;
            //在这里刷新项目下的设备列表
            if ((System.currentTimeMillis() - mExitTime) > 2000 && !mHandler.hasMessages(0)) {
                if (!canAddDel) {//防止listMap删除其中重复元素时，其他地方还在给它添加新元素
//                    listMap = new ArrayList<>();
                    setMap = new HashSet<>();
                }
                //刷新的时候如果蓝牙被手动关掉，需要在这里重启
                BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
                if (blueadapter.isEnabled()) {//true表示已经开启，false表示蓝牙并没启用。
                    Log.e(TAG1, "blueadapter.isEnabled()");
                    mayRequestLocation();//蓝牙扫描设备
                } else {//蓝牙没有开启或正在开启中
                    new Thread(new Runnable() {//在这里添加异步，目的是防止打开蓝牙时，出现黑屏
                        @Override
                        public void run() {
                            Looper.prepare();
                            initSwipeScan();
                            scanHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mayRequestLocation();
                                    Log.e(TAG1, "blueadapter.isUnEnabled()");
                                }
                            }, 1000);//延时一秒执行
                            Looper.loop();
                        }
                    }).start();
                }
                mExitTime = System.currentTimeMillis();
            } else {
                //防止listView刷新过于频繁
            }
        }

        //蓝牙初始化
        private void initSwipeScan() {
            mAckpassClass = new AckpassClass(this);
            mAckpassClass.setOnScanListener(this);
            mAckpassClass.setOnOpenDeviceListener(new OnOpenDeviceListener() {
                @Override
                public void OnOpenDevice(int status) {
                    switch (status) {
                        case 0:
                            toast("成功");
                            break;
                        case 1:
                            toast("失败");
                            break;
                        case 2:
                            toast("连接设备失败");
                            break;
                        case 3:
                            toast("参数错误");
                            break;
                        case 4:
                            toast("其他异常");
                            break;
                    }
                }
            });
            //检查一下如果不支持BLE，直接退出
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }

            if (!mAckpassClass.Initialize()) {//没注册成功
                Toast.makeText(this, "Initialize fail", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onLoadMore() {
        }

        @Override //蓝牙扫描
        public void OnScan(String deviceMac, String deviceName, String deviceType) {
            Log.e(TAG, "deviceMac:" + deviceMac + " deviceName:" + deviceName + " deviceType:" + deviceType);
            //扫描个2秒，然后停掉，然后下拉刷新时，在扫个两秒
            String paramId = "11111111";//为4字节卡号，用16进制字符串表示数据
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
            deviceMac = deviceMac.toLowerCase();//全部转换为小写
            map.put("deviceMac", deviceMac);
            map.put("deviceName", deviceName);
            map.put("deviceType", deviceType);
            map.put("paramId", paramId);//先添加这4个字段
            map.put("type", listTypeOfItem(deviceType));//为ListView添加不同的Item布局文件
            if (!canAddDel) {//防止listMap在删除其中重复的元素时，还在给它添加新元素
                setMap.add(map);
            }
        }

        private int listTypeOfItem(String deviceType) {
            int type = 0;
            switch (deviceType) {//这边负责显示
                case "06"://梯控
                    type = 0;
                    break;
                case "01"://门禁
                    type = 0;
                    break;
                case "02"://摆闸
                    type = 1;
                    break;
                case "03"://车库
                    type = 1;
                    break;
                case "04"://车位锁
                    type = 0;
                    break;
                case "05"://电子锁
                    type = 0;
                    break;
                case "07"://呼梯
                    type = 0;
                    break;
            }
            return type;
        }

        private void toast(String content) {
            Toast.makeText(DeviceWorkActivity.this, content, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
        }
    }



