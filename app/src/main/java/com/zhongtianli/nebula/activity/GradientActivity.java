package com.zhongtianli.nebula.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;

public class GradientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
        FinishUtil.addActivity(this);
    }
}

