package com.zhongtianli.nebula.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.netutil.MyRetrofit;
import com.zhongtianli.nebula.netutil.TestApi;

/**
 * Created by kaizen on 15/10/21.
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseView
{
    private Dialog mDialog;
    protected TextView mTitleTv;
    public String start="{\"content\":";
    public String end="}";
    Toast mToast;
    private Toolbar mToolbar;

    @Override
       protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(contentView());
        initView();
    }

    public TestApi getTestApi(){
        return new MyRetrofit().getGirlApi();
    }
    protected abstract int contentView();

    protected abstract void initView(
    );

    protected void initEvent()
    {

    }

    protected void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleTv.setText(title);
        mToolbar.setTitle("");
//        mToolbar.setNavigationIcon(R.mipmap.back);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out); //由右向左滑入的效果
    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
//        super.startActivityForResult(intent, requestCode, options);
////        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out); //由右向左滑入的效果
//    }


}
