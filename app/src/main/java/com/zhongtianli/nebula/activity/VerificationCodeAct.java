package com.zhongtianli.nebula.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;

import static com.zhongtianli.nebula.R.id.button;
import static com.zhongtianli.nebula.R.id.chg_password;
import static com.zhongtianli.nebula.R.id.swipeRefreshLayout;

/**
 * Created by zhu on 2016/11/11.
 */
public class VerificationCodeAct extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbars;
    private Button verify_code_btn;
    private EditText chg_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);
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
        verify_code_btn.setOnClickListener(this);
    }

    private void initView() {
        toolbars = (Toolbar) findViewById(R.id.toolbar_verfiy_code);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        editTextProties();
        verify_code_btn = (Button) findViewById(R.id.verify_code_btn);
    }

    private void editTextProties() {//改变EditText->Hint字体大小
        chg_verify = (EditText)findViewById(R.id.chg_verify);
// 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("输入验证码");
// 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);
// 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// 设置hint
        chg_verify.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id. verify_code_btn ://校验二维码成功后进入，设置密码界面
                if (!chg_verify.getText().toString().isEmpty()) {
                    Intent intent = new Intent(VerificationCodeAct.this,SettingsPasswordAct.class);
//                    intent.putExtra("UserNumber",chg_verify.getText().toString());
                    startActivity(intent);
                    VerificationCodeAct.this.finish();
                } else {
                    Toast.makeText(VerificationCodeAct.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
