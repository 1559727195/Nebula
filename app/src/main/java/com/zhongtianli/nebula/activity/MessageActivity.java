package com.zhongtianli.nebula.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.adapter.Xlist_getMessageAdapter;
import com.zhongtianli.nebula.maxwin.view.XListView;
import com.zhongtianli.nebula.utils.FinishUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/23.
 */
public class MessageActivity extends AppCompatActivity implements XListView.IXListViewListener {
    private XListView xListView_messages;
    private Xlist_getMessageAdapter xlist_adapter;
    private Toolbar toolbars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        FinishUtil.addActivity(this);
        initView();
        initDatas();
        initEvent();
    }

    private void initView() {//初始化xlistview
        xListView_messages = (XListView) findViewById(R.id.xListView_message);
        xListView_messages.setFootViewHide();
        xListView_messages.setPullLoadEnable(false);
        toolbars = (Toolbar) findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
    }
    private void initDatas() {
        ArrayList<String> list =new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("kfjg:"+i);
        }
        xlist_adapter = new Xlist_getMessageAdapter(MessageActivity.this,list);
        xListView_messages.setAdapter(xlist_adapter);
    }

    private void initEvent() {
        xListView_messages.setXListViewListener(this);
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void onLoad() {
        xListView_messages.stopRefresh();
        xListView_messages.setRefreshTime("刚刚");
    }


    @Override
    public void onRefresh() {//下拉刷新
        onLoad();
    }

    @Override
    public void onLoadMore() {//上拉加载，暂时不用

    }
}
