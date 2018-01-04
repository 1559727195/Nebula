package com.zhongtianli.nebula.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.bean.LoginData;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.finddreams.network.NetUtils;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.netutil.MyRetrofit;
import com.zhongtianli.nebula.netutil.TestApi;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.MediaType;

import static cn.ciaapp.sdk.CIAService.context;
import static com.zhongtianli.nebula.R.string.login;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends RxAppCompatActivity implements OnClickListener {

    private EditText nameEdit;
    private EditText passWordEdit;
    private CheckBox hidePassImg;
    private Button login_btn;
    private String userName;
    private String psd;
    private String TAG = "robin debug";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private LinearLayout linear_login;
    //把数据进行保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        FinishUtil.addActivity(this);
        initView();
        getInitEvent();
    }

    //retrofit网络访问接口调用
    public TestApi getTestApi() {
        return new MyRetrofit().getGirlApi();
    }

    private void getInitEvent() {
        linear_login.setOnTouchListener(new View.OnTouchListener() {//当点击edittextView其他地方时，让editTextView失去焦点
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                linear_login.setFocusable(true);
                linear_login.setFocusableInTouchMode(true);
                linear_login.requestFocus();
                InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(hidePassImg.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(passWordEdit.getWindowToken(), 0);//edittextView失去焦点时，软键盘消失
                return true;
            }
        });
        login_btn.setOnClickListener(new NoDoubleClickListener() {//防止过快点击造成多次事件
            @Override
            protected void onNoDoubleClick(View v) {
                login();//登录
                Log.e(TAG,"登录");
            }
        });
        hidePassImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//密码-明文和密文切换
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//密码切明文
                    LoginActivity.this.passWordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {//明文切密文
                    LoginActivity.this.passWordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                LoginActivity.this.passWordEdit.setSelection(passWordEdit.getText().length());
            }
        });

        //监听用户名editTextView的文本变化事件
           /*取得编辑框*/
        /*监听 编辑框中的文本改变事件*/
        nameEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*++这里显示出输入的字符串++*/
                if(null != nameEdit){
                    //把用户名sp清空
                    editor.putString("UserName", "");
                    editor.putString("Password", "");
                    editor.commit();
                }
            }
        });
    }

    private void initView() {
        //主布局
        linear_login = (LinearLayout) findViewById(R.id.linear_login);
        nameEdit = (EditText) findViewById(R.id.edt_operator_name);
        passWordEdit = (EditText) findViewById(R.id.edt_operator_password);
        hidePassImg = (CheckBox) findViewById(R.id.img_see);
        login_btn = (Button) findViewById(R.id.login_btn);
        //初始化sp
        //参数一：保存数据的文件名，不用加后缀，自动会生成usrmsg.xml文件,存在/data/data/包名/shared_prefs目录下
//参数二：私有模式
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sp.edit();
        initSpData();
        nameEdit.setText(sp.getString("UserName", null));//从sp中拿到用户名
        Log.e(TAG,"length:"+nameEdit.getText().length());
        nameEdit.setSelection(nameEdit.getText().length());
        nameEdit.getSelectionStart();//这两句话的意思是:光标总是位于textview文本之后
        //把数据进行保存
    }

    private void initSpData() {//初始化sp
        if(sp.getBoolean("boolean",false)){//为真时说明，上次登录成功
            Intent intent = new Intent(LoginActivity.this, Main_New_Activity.class);
            Bundle b = new Bundle();
            b.putSerializable("list",null);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        } else {//为假说明账号变了

        }
    }

    @Override
    public void onClick(View v) {
    }

    private void login() {
        if (TextUtils.isEmpty(nameEdit.getText().toString())) {
            showToast("用户名不能为空");
        } else if (TextUtils.isEmpty(passWordEdit.getText().toString())) {
            showToast("登录密码不能为空");
        } else {
            JSONObject stoneObject = new JSONObject();
            try {
                stoneObject.put("UserName", userName);
                stoneObject.put("Password", psd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userName = nameEdit.getText().toString();
            psd = passWordEdit.getText().toString();
            //在这里做从sp中拿用户名和密码判断输入账号和密码是否正确，正确的话在去，云端拉取项目列表(即小区列表)
          String userNameSp =  sp.getString("UserName","ni");//sp中的用户名
            String userpassWordSp = sp.getString("Password","");//密码

            //加一个判断网络状况
            boolean isConnected = NetUtils.isNetworkConnected(this);
            if (!isConnected) {
                showToast("网络已断开");
                return;
            }

            //第一次登录时，sp中没有存passWord
                if (!userpassWordSp.equals("")) {
                    if (!userName.equals(userNameSp) || !psd.equals(userpassWordSp)) {
                        showToast("用户名或密码不正确");
                        return;
                    }
                }

            LoginData login = new LoginData(userName, psd);
//        UpUseropen upUseropen = new UpUseropen(token, demac, netTime, "1", "9999");
            Log.e(new Gson().toJson(login) + "这是上传数据", "login1: ");
            OkHttpUtils
                    .postString()
                    .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_SetLogin")
                    .tag(this)
                    .mediaType(JSON)
                    .content(new Gson().toJson(login))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(response + "这是登陆返回数据", "onResponse: ");
                            JSONObject jsonObject = null;
                            String Result = null;
                            try {
                                jsonObject = new JSONObject(response);
                                Result = jsonObject.getString("Result");
                                if (Result.equals("100")) {
                                    JSONArray Projects = null;
                                    try {
                                        Projects = jsonObject.getJSONArray("Projects");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
//                                  {
//                                      "Result": "100",
//                                          "Projects": [
//                                      {
//                                          "ProjectCode": "2016316081670119716643161672551",
//                                              "ProjectName": "水木春城",
//                                              "DeviceAdd": "1",
//                                              "DeviceEdit": "1",
//                                              "DeviceTest": "1",
//                                              "DeviceDebug": "1",
//                                              "ExpiryDate": "2016-10-30"
//                                      }
//                                      ]
//                                  }
                                        //登录成功-用户名和密码存入sp中
                                        editor.putString("UserName", userName);
                                        editor.putString("Password", psd);
                                        editor.putBoolean("boolean", true);//首次登录，保存登录状态，下回直接跳过登录界面
                                        editor.commit();
                                        showToast("登录成功");
                                        Bundle b = new Bundle();
                                        b.putSerializable("list", list);
                                        //此处使用putExtras，接受方就响应的使用getExtra
                                        Intent intent = new Intent(LoginActivity.this, Main_New_Activity.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                        finish();
                                       }
                                    }else if (Result.equals("101")) {
                                        showToast("用户名或密码错误");
                                    } else if (Result.equals("102")) {
                                        showToast("未知错误");
                                    }

                                    Log.i(Result + "这是上传返回数据", "onResponse: ");
                                } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    //防止过快点击造成多次事件
    public abstract class NoDoubleClickListener implements OnClickListener {

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

