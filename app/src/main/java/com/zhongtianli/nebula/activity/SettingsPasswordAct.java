package com.zhongtianli.nebula.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhongtianli.nebula.view.IShowNebula_SetPasswordView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhu on 2016/11/11.
 */
public class SettingsPasswordAct extends AppCompatActivity implements View.OnClickListener,IShowNebula_SetPasswordView {
    private Toolbar toolbars;
    private EditText verify_pass;
    private EditText verify_new_pass;
    private Button verify_pass_btn;
    private String userNumber;
    private LinearLayout linear_pass;
    private CheckBox password_see;
    private FlightInfoPresenter flightInfoPresenter;//注册MVP模式-》来提交新密码和老账号信息，来拿到设置返回信息
    private Map map = new HashMap();
    private SharedPreferences sp;//下载ProjectCode下的设备列表，需要将ProjectCode和UserName保存到Sp中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_password);
        FinishUtil.addActivity(this);
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        flightInfoPresenter = new FlightInfoPresenter(this);//注册提交新密码信息后，返回信息的事件
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        verify_pass_btn.setOnClickListener(this);//焦点失去时，校验两次密码是否相同
        verify_new_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //获得焦点处理
                }
                else {
                    verfiyPassword();//校验密码
                }
            }
        });

        password_see.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//密码-明文和密文切换
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//密码切明文
                   SettingsPasswordAct.this.verify_new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {//明文切密文
                    SettingsPasswordAct.this.verify_new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
               SettingsPasswordAct.this.verify_new_pass.setSelection(verify_new_pass.getText().length());//让焦点位于文本右边
            }
        });

        linear_pass.setOnTouchListener(new View.OnTouchListener() {//当点击edittextView其他地方时，让editTextView失去焦点
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                linear_pass.setFocusable(true);
                linear_pass.setFocusableInTouchMode(true);
                linear_pass.requestFocus();
                InputMethodManager imm = (InputMethodManager) SettingsPasswordAct.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(verify_pass.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(verify_new_pass.getWindowToken(), 0);//edittextView失去焦点时，软键盘消失
                return true;
            }
        });
    }

    private void verfiyPassword() {//校验两次输入密码是否相等
        //失去焦点处理
        if (!verify_pass.getText().toString().isEmpty() &&
                !verify_new_pass.getText().toString().isEmpty()) {
            if (!verify_pass.getText().toString().equals(verify_new_pass.getText().toString())) {
                Toast.makeText(SettingsPasswordAct.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                verify_pass.setText("");//密码校验错误时，清空
                verify_new_pass.setText("");//密码校验错误时，清空
            }
        }
    }

    private void initData() {
        //账户号
        sp = getSharedPreferences("Nebula_GetDevice", MODE_PRIVATE);
        //用户名
        userNumber = sp.getString("UserName","ni");
    }

    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_set_password);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        verify_pass = (EditText)findViewById(R.id.verify_pass);//老密码
        verify_new_pass = (EditText)findViewById(R.id.verify_new_pass);//新密码
        editTextProties(verify_pass,"新密码");
        editTextProties(verify_new_pass,"在次输入密码");
        //提交新密码设置动作
        verify_pass_btn = (Button) findViewById(R.id.verify_pass_btn);
        linear_pass = (LinearLayout) findViewById(R.id.Linear_pass);//当点击edittextView其他地方时，让editTextView失去焦点
        password_see = (CheckBox) findViewById(R.id.password_see);
    }

    private void editTextProties(EditText editText,String content) {//改变EditText->Hint字体大小
// 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(content);
// 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);
// 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id. verify_pass_btn:
                  verfiyPassword();//校验密码
                  if (!verify_new_pass.getText().toString().isEmpty() && !verify_pass.getText().toString().isEmpty()) {
                      //在这里提交用户名和新密码的操作
                      map = new HashMap();
                      map.put("UserName",userNumber);//账号
                      map.put("Password",verify_new_pass.getText().toString());
                      flightInfoPresenter.setNebula_password(SettingsPasswordAct.this, map);

                  } else {
                      Toast.makeText(SettingsPasswordAct.this,"请输入密码",Toast.LENGTH_SHORT).show();
                  }
                break;
        }
    }

    @Override
    public void showNebula_setPassword_success(String Result) {//成功设置新密码
         Intent intent = new Intent(SettingsPasswordAct.this,SettingsPasswordSuccAct.class);
        //在这里使用用户名和新密码
        map = new HashMap();
        map.put("UserName",userNumber);//账号
        map.put("Password",verify_new_pass.getText().toString());
        intent.putExtra("map", (Serializable) map);
         startActivity(intent);
        SettingsPasswordAct.this.finish();
    }

    @Override
    public void showNebula_setPasswordError() {//设置新密码失败
        SettingsPasswordAct.this.finish();
    }
}
