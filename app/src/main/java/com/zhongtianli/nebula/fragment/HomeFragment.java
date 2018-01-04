package com.zhongtianli.nebula.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.interfaces.FragmentRefreshUi;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.view.IShowFragmentView;

import butterknife.ButterKnife;

import static com.zhongtianli.nebula.R.id.img_search;


/**
 * Created by zhu on 2016/10/21.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,IShowFragmentView
 {

    //private int mPage = 1;
    private String url;
    private boolean hasTitle = true;
    private static FragmentRefreshUi fragmentRefreshUi1;
    private ImageView search_img;
    private TextView search_txt;
    private View view;
    private Main_New_Activity mainActivity;

    public static HomeFragment newInstance(FragmentRefreshUi fragmentRefreshUi) {
       fragmentRefreshUi1 = fragmentRefreshUi;
        HomeFragment f = new HomeFragment();
        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
            //判断
            if (url.equals("http://www.juzimi.com/meitumeiju?page=")) {
                hasTitle = true;//美图美句 带标题和图片
            } else {
                hasTitle = false;
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        search_img = (ImageView) view.findViewById(img_search);
        search_txt = (TextView) view.findViewById(R.id.search_txt);
    }

    private void initEvent() {
        search_img.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case img_search://查询动作
                  search_img.setImageResource(R.drawable.busy);
                search_txt.setText("正在搜索附近设备");
                fragmentRefreshUi1.refreshUI(this);
                break;
        }
    }

    //activity刷新fragment的UI
    @Override
    public void toHomeFragment(String success) {
    }

    @Override
    public void showFailedError() {
        search_img.setImageResource(R.drawable.btn_search);
        search_txt.setText("搜索设备");
    }
}

