package com.zhongtianli.nebula.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.adapter.Xlist_View_getLan_scan_Adapter;
import com.zhongtianli.nebula.interfaces.AdapterBackToAct;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.maxwin.view.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;


/**
 * Created by zhu on 2016/10/21.
 */
public class HomeSecondFragment extends BaseFragment implements XListView.IXListViewListener,Main_New_Activity.OnButtonClickListener
{

    //private int mPage = 1;
    private String url;
    private boolean hasTitle = true;
    private XListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;
    private ArrayList<ConcurrentHashMap> successl =  new ArrayList<>();
    private String TA="robin debug";
    private static Context context1;
    private  String userName;//用户名
    private Xlist_View_getLan_scan_Adapter xlist_adapter;
    private static FragmentRefreshList fragmentRefreshList;
    public Map user =  new HashMap();//存放项目下的用户名和ProjectCode
    private  static  AdapterBackToAct adapterBackToAct1;//back键回来后refresh主界面蓝牙数据


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static HomeSecondFragment newInstance(Context context, FragmentRefreshList fragmentRefreshLis
    ,AdapterBackToAct adapterBackToAct) {//第二个fragment初始化
        HomeSecondFragment f = new HomeSecondFragment();
       fragmentRefreshList = fragmentRefreshLis;
       context1 = context;
        adapterBackToAct1 = adapterBackToAct;//back键回来后refresh主界面蓝牙数据
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            user = (Map) getArguments().getSerializable("user");
           successl = (ArrayList<ConcurrentHashMap>) getArguments().getSerializable("GetDevice");
            //拿到设备信息
        }
        View view = inflater.inflate(R.layout.main_xlist_view, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initEvent();
        initData();
        return view;
    }

        private void initData() {//初始化蓝牙设备
                xlist_adapter = new Xlist_View_getLan_scan_Adapter(context1, successl,
                        user, adapterBackToAct1);
                mListView.setAdapter(xlist_adapter);
            }
            //蓝牙初始化附近设备信息

    private void initEvent() {
        mListView.setXListViewListener(this);
    }

    private void initView(View view) {
        mListView = (XListView) view.findViewById(R.id.xListView);
        mListView.setFootViewHide();
        mListView.setPullLoadEnable(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        onLoad();
        //在这里刷新项目下的设备列表
        fragmentRefreshList.refreshList();
    }

    @Override
    public void onLoadMore() {

    }

    @Override  //在这里写一个设置activity多次携带数据返回fragment的接口实现
    public void onButtonClicked(ArrayList<ConcurrentHashMap> success, Map user) {//项目下的-》用户名和项目Code
        if(xlist_adapter !=null) {
            this.user = user;
            xlist_adapter.setList(success,user);
            xlist_adapter.notifyDataSetChanged();
        }
    }

    public interface FragmentRefreshList {//定义这个接口的目的是:
        void  refreshList();
    }
}






