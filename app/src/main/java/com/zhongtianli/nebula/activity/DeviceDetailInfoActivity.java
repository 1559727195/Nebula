package com.zhongtianli.nebula.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhongtianli.nebula.view.IShowNebula_SetDeviceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhongtianli.nebula.R.drawable.btn;

/**
 * Created by zhu on 2016/11/4.
 */
public class DeviceDetailInfoActivity extends AppCompatActivity implements IShowNebula_SetDeviceView
,View.OnClickListener{
    private static final int CHANGE_DEVICE_NAME = 0x0001;//请求设备名称的编码requestCode
    private Toolbar toolbars;
    private ImageView cha_device_name;
    private String DEVICE_NAME_KEY = "deviceName";
    private TextView device_name1;//设备名称-可修改
    private SetDeviceBean setDeviceBean =new SetDeviceBean();
    private Button btn_send;
    private String device_name;
    private FlightInfoPresenter flightInfoPresenter;//注册上传设备信息的监听器
    private TextView device_mac;
    private TextView device_type;
    private Button btn_sho1u;//收藏
    private SQLiteUtils sqLiteUtils;//数据库操作
    private DatabaseHelper databaseHelper;//数据库句柄
    //本地数据库需要先存储三个字段：device_type设备类型 ，device_mac：MAC地址  ，type上传是否成功 1成功,0失败
      private  final String  TABLE_NAME_LIU="projectCode";//
    private Intent mainIntent;//拿到mainActivity的Intent
    private RelativeLayout rel_chag_name;//跳转到修改项目名称deviceName
    private String TAG1 = "zhu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        FinishUtil.addActivity(this);
        //拿到主界面的intent
        mainIntent = getIntent();
        CreateTable();//创建表
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        //添加一个字段判断是哪个activity进入到DeviceDetailInfoActivity中去的
       String activity_type = mainIntent.getStringExtra("activity_type");
        if (activity_type.equals("Main_NewActivity")) {//是主界面调过来的
            btn_sho1u.setVisibility(View.VISIBLE);//可见
        } else if (activity_type.equals("DeviceManagerActivity")) {//说明是DeviceManagerActivity跳过来的
            btn_sho1u.setVisibility(View.INVISIBLE);
        }
        setDeviceBean = (SetDeviceBean) mainIntent.getSerializableExtra("SetDeviceBean");
        device_name1.setText( setDeviceBean.getDevices().get(0).getDeviceName());//得到设备名称
        device_mac.setText( setDeviceBean.getDevices().get(0).getDeviceMac());//得到设备Mac
       //得到设备类型
        switch (setDeviceBean.getDevices().get(0).getDeviceType()) {
            case "01"://门禁
                device_type.setText("门禁");
                break;
            case "02"://摆闸
                device_type.setText("摆闸");
                break;
            case "03"://电梯
                device_type.setText("车库");
                break;
            case "06"://电梯
                device_type.setText("电梯");
                break;
        }
    }

    private void initEvent() {
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                setResult(0,mainIntent);//返回到主界面去刷新主界面数据
                finish();
            }
        });
        cha_device_name.setOnTouchListener(new View.OnTouchListener() {//跳转到修改设备名称activity
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作-
                    //修改设备名称并回传到设备详细信息界面
                    Intent intent = new Intent(DeviceDetailInfoActivity.this, DeviceNameChanAct.class);
                    intent.putExtra("deviceName",device_name1.getText().toString());//把设备名称给它传过去
                    startActivityForResult(intent, CHANGE_DEVICE_NAME);
                    return true;
                }
                return false;
            }
        });

        rel_chag_name.setOnTouchListener(new View.OnTouchListener() {//跳转到修改设备名称activity
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作-
                    //修改设备名称并回传到设备详细信息界面
                    Intent intent = new Intent(DeviceDetailInfoActivity.this, DeviceNameChanAct.class);
                    intent.putExtra("deviceName",device_name1.getText().toString());//把设备名称给它传过去
                    startActivityForResult(intent, CHANGE_DEVICE_NAME);
                    return true;
                }
                return false;
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//上传修改过的设备名称的设备信息
                //上传设备信息
                if (device_name != null) {
                    setDeviceBean.getDevices().get(0).setDeviceName(device_name);
                } else {
                    setDeviceBean.getDevices().get(0).setDeviceName(device_name1.getText().toString());
                }
                flightInfoPresenter.setNebula_GetDevice(DeviceDetailInfoActivity.this,setDeviceBean);//上传修改后的设备信息
            }
        });
        flightInfoPresenter = new FlightInfoPresenter(this);//设置监听器
        btn_sho1u.setOnClickListener(this);
    }

    private void CreateTable() {//创建表
        sqLiteUtils = new SQLiteUtils();
        databaseHelper = sqLiteUtils.createDBHelper(this);
    }

    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_device_detail);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        cha_device_name = (ImageView) findViewById(R.id.cha_device_name);
        device_name1 = (TextView) findViewById(R.id.device_name1);
        btn_send = (Button) findViewById(R.id.btn_send);//上传
        btn_sho1u = (Button) findViewById(R.id.btn_shou);//收藏
        device_mac = (TextView) findViewById(R.id.device_mac);//设备mac
        device_type = (TextView) findViewById(R.id.device_type);//设备类型
        rel_chag_name = (RelativeLayout) findViewById(R.id.rel_chag_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode是请求的activity编码，resultCode是返回的activity编码

        //修改设备名称后回传
        if (requestCode == CHANGE_DEVICE_NAME && resultCode == 12) {
            if (data != null) {
                //--deviceName
                //解析回传的string
                device_name = data.getStringExtra("deviceName");
                device_name1.setText(device_name);
            }
        }
    }

    @Override//显示设备信息是否上传成功
    public void showNebula_SetDevice(String Result) {
         if (Result.contains("100")) {//上传设备信息成功
             Toast.makeText(DeviceDetailInfoActivity.this,"上传设备信息成功",Toast.LENGTH_SHORT).show();
             //成功将type =1
             sqlShouCang("1");//上传成功，将type改为1

         } else if (Result.contains("101")) {//失败，项目编号不正确
             Toast.makeText(DeviceDetailInfoActivity.this,"项目编号不正确",Toast.LENGTH_SHORT).show();
             sqlShouCang("0");
         } else if (Result.contains("102")) {//未知错误
             Toast.makeText(DeviceDetailInfoActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
             sqlShouCang("0");
         }
        DeviceDetailInfoActivity.this.finish();
    }

    @Override
    public void showNebula_FailedError() {
        Toast.makeText(DeviceDetailInfoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
        //上传失败相当于直接收藏type = 0，上传成功 type = 1
        sqlShouCang("0");
        //无论是上传成功抑或失败或者收藏都将该activity-》finish掉
        DeviceDetailInfoActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shou://收藏
                sqlShouCang("0");//收藏
                DeviceDetailInfoActivity.this.finish();
                Toast.makeText(DeviceDetailInfoActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void sqlShouCang(String type) {//收藏
        List<Map> list = new ArrayList<>();
        Map map = new HashMap();//新数据
        map.put("deviceMac",setDeviceBean.getDevices().get(0).getDeviceMac());
        map.put("deviceName",device_name1.getText().toString());
        map.put("deviceType",setDeviceBean.getDevices().get(0).getDeviceType());
        map.put("ProjectCode",setDeviceBean.getProjectCode());//添加一个ProjectCode项目编码，这里为空指针ProjectCode为null,当在离线下的情况
        if (type.equals("0")) {//上传失败，或者直接收藏
            map.put("type","0");//0->代表没有上传或者上传失败
        } else if (type.equals("1")) {//上传成功
            map.put("type","1");//1->代表上传成功
        }
//                收藏之前先从数据库中读取，没有就插入，有就更新
        list = sqLiteUtils.select(databaseHelper,setDeviceBean.getDevices().get(0).getDeviceMac()
         ,TABLE_NAME_LIU);//根据该deviceMac去查找该设备信息
        Log.e(TAG1,list.size()+"deviceMac:"+setDeviceBean.getDevices().get(0).getDeviceMac()
                );
        if (list.size() == 1) {//说明该deviceMac下已经有项目记录,则执行更新操作
            sqLiteUtils.upgrate(databaseHelper,map,TABLE_NAME_LIU);
        }  else if (list.size() > 1) {//说明有多条，执行删除操作,只保留一条
            sqLiteUtils.delete(databaseHelper,setDeviceBean.getDevices().get(0).getDeviceMac(),
                    TABLE_NAME_LIU,list.size());//执行删除，只保留一条，
            sqLiteUtils.insert(databaseHelper, map, TABLE_NAME_LIU);//删完，在重写插入该deviceMac的最新记录
        }
        else if (list.size() == 0) {
            //deviceMac,deviceName,deviceType,type
            sqLiteUtils.insert(databaseHelper, map, TABLE_NAME_LIU);//插入新更改项目名字的项目
        }
    }
}
