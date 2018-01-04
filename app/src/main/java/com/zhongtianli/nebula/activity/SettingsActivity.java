package com.zhongtianli.nebula.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import com.zhongtianli.nebula.utils.DataCleanManager;
import com.zhongtianli.nebula.utils.FinishUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.id.edit;
import static cn.ciaapp.sdk.CIAService.context;
import static com.zhongtianli.nebula.utils.DataCleanManager.getCacheSize;
import static com.zhongtianli.nebula.utils.DataCleanManager.getFormatSize;

/**
 * Created by Administrator on 2016/10/23.
 */
public class SettingsActivity extends AppCompatActivity  implements View.OnClickListener{

    private ToggleButton mTogBtn;
    private Toolbar toolbars;
    private ImageView img_change_pas;
    private RelativeLayout chag_pass_rel;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private RelativeLayout clear_cache;//清除缓存
    private String TAG = "robin debug";
    private SQLiteUtils sqLiteUtils;//数据库操作
    private DatabaseHelper databaseHelper;//数据库句柄
    private final String DEBUG_TABLE = "debug" ;//debug表
    private String cacheSize;//拿到蓝牙设备开关存到debug表中的数据大小
    private TextView debug_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FinishUtil.addActivity(this);
        CreateTable();//初始化数据库表
        sp = getSharedPreferences("ToggleButton",MODE_PRIVATE);
        //ToggleButton被选中
        editor = sp.edit();
        initView();
        initEvent();
    }

    private void CreateTable() {//创建表
        sqLiteUtils = new SQLiteUtils();
        databaseHelper = sqLiteUtils.createDBHelper(this);
    }

    private void initEvent() {
        getCache();//获取缓存的方法
        clear_cache.setOnClickListener(this);
        //如何保存toogleButton的开/关选择的状态，在这里从sp中读取toggleButton的状态
        boolean pref = sp.getBoolean("TG", false);
        if (pref == true) {
            mTogBtn.setChecked(true);
        } else {
            mTogBtn.setChecked(false);
        }
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //选中
                    editor.putBoolean("TG", true);//被选中
                    editor.commit();
                } else {
                    //未选中
                    editor.putBoolean("TG", false);//未被选中
                    editor.commit();
                }
            }
        });// 添加监听事件

        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chag_pass_rel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作
                    //跳转到修改密码的界面
                    startActivity(new Intent(SettingsActivity.this,VerifyNumberActivity.class));
                    finish();
                }
                return true;
            }
        });

        img_change_pas.setOnTouchListener(new View.OnTouchListener() {//无论是点住imageView还是点住RelativeLayout都
            //进行activity之间的跳转
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作
                    //跳转到修改密码的界面
                    startActivity(new Intent(SettingsActivity.this,VerifyNumberActivity.class));
                }
                return true;
            }
        });
    }

    private void initView() {
        // 获取到控件
        mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
        toolbars = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        img_change_pas = (ImageView) findViewById(R.id.img_change_pas);
        chag_pass_rel = (RelativeLayout) findViewById(R.id.chag_pass_rel);//修改密码
        clear_cache = (RelativeLayout) findViewById(R.id.clear_cache);//清除缓存
        debug_size  = (TextView) findViewById(R.id.debug_size);//拿到蓝牙设备开关存到debug表中的数据大小
    }

    //获取缓存
    private  void getCache () {
        refreshDebugSize();
    }

    private void refreshDebugSize() {
        //数据库表debug路径
        List<Map> list= new ArrayList<>();
        list = sqLiteUtils.selectDebug(databaseHelper,DEBUG_TABLE);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map map : list) {
            stringBuffer.append(map.get("send"));
            stringBuffer.append(map.get("receive"));
        }
        Log.e(TAG,"list:"+stringBuffer.length());
        try {
            //拿到插入到debug表中的字段大小
            cacheSize = DataCleanManager.getFormatSize(stringBuffer.length());
            Log.e(TAG,"cacheSize:"+ cacheSize);
            debug_size.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_cache://把debug表中的数据删完
               sqLiteUtils.deleteDebug(databaseHelper,DEBUG_TABLE);//清空debug调试蓝牙表中的数据后，刷新ui显示
                refreshDebugSize();
                break;//清除缓存
        }
    }
}
