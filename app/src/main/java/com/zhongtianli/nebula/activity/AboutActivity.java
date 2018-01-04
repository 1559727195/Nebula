package com.zhongtianli.nebula.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhongtianli.nebula.App;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;

/**
 * Created by Administrator on 2016/10/23.
 */
public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FinishUtil.addActivity(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
    }
}
