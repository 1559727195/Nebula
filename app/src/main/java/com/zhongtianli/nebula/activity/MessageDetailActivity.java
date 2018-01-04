package com.zhongtianli.nebula.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;

/**
 * Created by zhu on 2016/11/2.
 */
public class MessageDetailActivity  extends AppCompatActivity {
    private Toolbar toolbars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detial_message);
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
        toolbars = (Toolbar) findViewById(R.id.toolbar_detail_mess);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
    }
}
