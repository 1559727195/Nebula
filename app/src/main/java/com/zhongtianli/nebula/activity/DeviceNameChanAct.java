package com.zhongtianli.nebula.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;


/**
 * Created by zhu on 2016/11/4.
 */
public class DeviceNameChanAct extends AppCompatActivity {
    private Toolbar toolbars;
    private EditText edt_device_name_chag;
    private Button btn_device_name;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_name_chag);
        FinishUtil.addActivity(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        intent = getIntent();
        String deviceName =  intent.getStringExtra("deviceName");
        edt_device_name_chag.setText(deviceName);//传过来的deviceName
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_device_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把edittext获取的设备名称回传到上传设备详细界面
                if (!edt_device_name_chag.getText().toString().isEmpty()) {
                    intent.putExtra("deviceName", edt_device_name_chag.getText().toString());
                    setResult(12, intent);
                    DeviceNameChanAct.this.finish();
                } else {
                    Toast.makeText(DeviceNameChanAct.this,"设备名称不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_device_name_chag);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        edt_device_name_chag = (EditText) findViewById(R.id.edt_device_name_chag);
        btn_device_name = (Button) findViewById(R.id.btn_device_name);
    }
}
