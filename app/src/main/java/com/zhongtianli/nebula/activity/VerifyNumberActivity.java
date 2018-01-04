package com.zhongtianli.nebula.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.FinishUtil;

import cn.ciaapp.sdk.CIAService;
import cn.ciaapp.sdk.VerificationListener;

import static com.zhongtianli.nebula.R.id.verify_new_pass;
import static com.zhongtianli.nebula.R.id.verify_pass;

/**
 * Created by zhu on 2016/11/2.
 */
public class VerifyNumberActivity extends AppCompatActivity implements View.OnClickListener{

    private Button verify_btn;
    private Toolbar toolbars;
    private EditText chg_password;
    private LinearLayout rel_verify_numbers;//父容器获取焦点
    private ProgressDialog progressDialog;
    private Dialog dialog;//正在验证，请稍后

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);
        FinishUtil.addActivity(this);
        initView();
        initEvent();
    }


    private void initEvent() {
        relHasFocus();//editTextView失去焦点时，让其父容器获取焦点
        verify_btn.setOnClickListener(this);
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        rel_verify_numbers = (LinearLayout) findViewById(R.id.rel_verify_numbers);
        editTextProties();
        verify_btn = (Button) findViewById(R.id.verify_btn);
        toolbars = (Toolbar) findViewById(R.id.toolbar_verfiynumber);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
    }

    private void relHasFocus() {//editTextView失去焦点时，让其父容器获取焦点
        rel_verify_numbers.setOnTouchListener(new View.OnTouchListener() {//当点击edittextView其他地方时，让editTextView失去焦点
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                rel_verify_numbers.setFocusable(true);
                rel_verify_numbers.setFocusableInTouchMode(true);
                rel_verify_numbers.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(chg_password .getWindowToken(), 0);//edittextView失去焦点时，软键盘消失
                return true;
            }
        });
    }

    private void editTextProties() {//改变EditText->Hint字体大小
        chg_password = (EditText)findViewById(R.id.chg_password);
// 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("输入手机号码");
// 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);
// 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// 设置hint
        chg_password.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    //改变editTextView的Hint属性
    public static CharSequence stringOrSpannedString(CharSequence source) {
        if (source == null)
            return null;
        if (source instanceof SpannedString)
            return source;
        if (source instanceof Spanned)
            return new SpannedString(source);

        return source.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_btn://手机号码校验
                if (!chg_password.getText().toString().isEmpty()) {
                    // TODO 判断网络状态
                    // TODO 校验手机号码格式
                    showProgress();

                    // 调用CIAService发起验证请求
                    CIAService.startVerification(chg_password.getText().toString(), new VerificationListener() {
                        /**
                         * 校验结果回调
                         * @param status        验证状态码
                         * @param msg           验证状态的描述
                         * @param transId       当前验证请求的流水号，服务器可以根据流水号查询验证的状态
                         */
                        @Override
                        public void onStateChange(int status, String msg, String transId) {
                            dismissProgress();
                            /**
                             * status 是返回的状态码，CIAService包含了一些常量
                             * @see CIAService.VERIFICATION_SUCCESS 验证成功
                             * @see CIAService.VERIFICATION_FAIL 验证失败，请查看 msg 参数描述，例如手机号码格式错误，手机号格式一般需要开发者先校验
                             * @see CIAService.SECURITY_CODE_MODE   验证码模式
                             *      验证码模式：需要提示用户输入验证码，调用
                             *      @see CIAService.getSecurityCode()    获取当前的验证码，格式类似05311234****，需要提示用户****部分是输入的验证码内容
                             * @see CIAService.REQUEST_EXCEPTION    发生异常，msg 是异常描述，例如没有网络连接，网络连接状况一般需要开发者先判断
                             *
                             * 其他情况，status不在上述常量中，是服务器返回的错误，查看 msg 描述，例如 appId 和 authKey 错误。
                             */

                            switch (status) {
                                case CIAService.VERIFICATION_SUCCESS: // 验证成功
                                    // TODO 进入下一步业务逻辑
                                    Intent intent = new Intent(VerifyNumberActivity.this, SettingsPasswordAct.class);
                                    startActivity(intent);
                                    VerifyNumberActivity.this.finish();
                                    showToast("验证成功");//进入到密码设置界面

                                    break;
                                case CIAService.SECURITY_CODE_MODE: // 验证码模式-请求成功，等待验证码送达
                                    // 进入输入验证码的页面，并提示用户输入验证码
                                    startActivity(new Intent(VerifyNumberActivity.this, SecurityCodeActivity.class));
                                    VerifyNumberActivity.this.finish();
                                    break;
                                case CIAService.VERIFICATION_FAIL:
                                    showToast("验证失败：" + msg);
                                    break;
                                case CIAService.REQUEST_EXCEPTION:
                                    showToast("请求异常：" + msg);
                                    break;
                                default:
                                    // 服务器返回的错误
                                    showToast(msg);
                            }
                        }
                    });
                } else {
                    Toast.makeText(VerifyNumberActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 显示进度条对话框
     */
    private void showProgress(){
        dismissProgress();
        dialog = createLoadingDialog(VerifyNumberActivity.this, "正在验证,请稍后...");//创建 loadingDialog
        dialog.show();
        dialogFullScreen();//让loadingDialog全屏
    }

    //Dialog全屏设置
    private void dialogFullScreen() {//让loadingDialog全屏
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height = display.getHeight();//设置高度
        lp.width = (int)(display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 关闭进度条对话框
     */
    private void dismissProgress(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Toast提示
     *
     * @param txt
     */
    private void showToast(String txt) {

        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }
}
