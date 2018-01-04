package com.zhongtianli.nebula.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.trello.rxlifecycle.components.support.RxFragment;


/**
 * Created by Kai on 2015/7/20.
 */
public class BaseFragment extends RxFragment implements BaseView {
    private Dialog mDialog;
    public String start="{\"content\":";
    public String end="}";
    Toast mToast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void setData(String json) {

    }
}

