package com.zhongtianli.nebula.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhongtianli.nebula.R;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainFragment extends BaseFragment{


    //private int mPage = 1;
    private String url;
    private boolean hasTitle = true;



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
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
