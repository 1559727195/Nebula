package com.zhongtianli.nebula.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import static com.zhongtianli.nebula.activity.LoginActivity.JSON;

/**
 * Created by zhu on 2016/11/16.
 */
public class SettingsPasswordSuccAct extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbars;
    private Map map;
    private Button verify_login;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_password_success);
        FinishUtil.addActivity(this);
        initData();
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
        verify_login.setOnClickListener(new NoDoubleClickListener() {//防止过快点击造成多次事件
            @Override
            protected void onNoDoubleClick(View v) {
                reLogin(map);//重新登录
            }
        });
    }


    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_verfiy_successfully);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        verify_login = (Button) findViewById(R.id.verify_login);//重新登录
        //初始化sp
        //参数一：保存数据的文件名，不用加后缀，自动会生成usrmsg.xml文件,存在/data/data/包名/shared_prefs目录下
//参数二：私有模式
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("UserName", (String) map.get("UserName"));
        editor.putString("Password", (String) map.get("Password"));
        editor.commit();
    }

    private void initData() {
        map = new HashMap();
        map = (Map) getIntent().getSerializableExtra("map");
    }

    @Override
    public void onClick(View v) {
    }

    private void reLogin(final Map map) {
        OkHttpUtils
                .postString()
                .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_SetLogin")
                .tag(this)
                .mediaType(JSON)
                .content(new Gson().toJson(map))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e(response + "这是登陆返回数据", "onResponse: ");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Result = jsonObject.getString("Result");
                            JSONArray Projects = jsonObject.getJSONArray("Projects");
                            ArrayList<LoginProjects> list = new ArrayList<>();
                            for (int i = 0; i < Projects.length(); i++) {
                                LoginProjects loginProjects = new LoginProjects();
                                JSONObject jsonObject_item = Projects.getJSONObject(i);
                                String ProjectCode = jsonObject_item.getString("ProjectCode");
                                String ProjectName = jsonObject_item.getString("ProjectName");
                                String DeviceAdd = jsonObject_item.getString("DeviceAdd");
                                String DeviceEdit = jsonObject_item.getString("DeviceEdit");
                                String DeviceTest = jsonObject_item.getString("DeviceTest");
                                String DeviceDebug = jsonObject_item.getString("DeviceDebug");
                                loginProjects.setProjectCode(ProjectCode);
                                loginProjects.setProjectName(ProjectName);
                                loginProjects.setDeviceAdd(DeviceAdd);
                                loginProjects.setDeviceEdit(DeviceEdit);
                                loginProjects.setDeviceTest(DeviceTest);
                                loginProjects.setDeviceDebug(DeviceDebug);
                                list.add(loginProjects);
                            }
                            if (Result.equals("100")) {
                                //登录成功-用户名和密码存入sp中
                                showToast("登录成功");
                                Bundle b = new Bundle();
                                b.putSerializable("list",list);
                                //此处使用putExtras，接受方就响应的使用getExtra
                                FinishUtil.finishActivity();//销毁所有的activity，重新登录时，销毁当前应用程序，开启一个新的应用程序
                                Intent intent = new Intent(SettingsPasswordSuccAct.this, Main_New_Activity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            } else if (Result.equals("1001")) {
                                showToast("用户名或密码错误");
                            } else if (Result.equals("102")) {
                                showToast("未知错误");
                            }

                            Log.i(Result + "这是上传返回数据", "onResponse: ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //防止过快点击造成多次事件
    public abstract class NoDoubleClickListener implements View.OnClickListener {

        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//防止login-button不停地被点
                //防止过快点击造成多次事件
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }

        protected abstract void onNoDoubleClick(View v);
    }
}
