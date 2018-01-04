package com.zhongtianli.nebula.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.DeviceManagerActivity;
import com.zhongtianli.nebula.adapter.Xlist_success_getDeviceAdapter;
import com.zhongtianli.nebula.maxwin.view.XListView;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tanger on 2016/4/11.
 */
public class DeviceMangerSuccessFragment extends Fragment implements XListView.IXListViewListener
,DeviceManagerActivity.UploadedSucListener{
    public static final String FLAG_INFO = "flagInfo";

    private String mInfo;
    private Context context;
    private XListView mListView;
    private Activity activity;
    private Xlist_success_getDeviceAdapter xlist_adapter;
    private TextView su_all1;
    private TextView su_fail1;
    private SQLiteUtils sqLiteUtils;//数据库操作
    private DatabaseHelper databaseHelper;//数据库句柄
    //本地数据库需要先存储三个字段：device_type设备类型 ，device_mac：MAC地址  ，type上传是否成功 1成功,0失败
    private  final String  TABLE_NAME_LIU="projectCode";//
    private List<ConcurrentHashMap> sqlList = new ArrayList<>();//数据库拿到的ProjectCode下的-暂时没有分ProjectCode，
    private String TAG1 = "peng";
    private  static DeviceManagerActivity.UploadedSucListener uploadedSucListener1;//接收可选的projectCode
    private String projectCode;//可选的项目编码
    private TextView su_success;
    private String userName;//用户名

    /**
     * @param  savedInstanceState
     *@param fragmentRefreshUi  @return
     */
     public  static DeviceMangerSuccessFragment instantiation(Bundle savedInstanceState, FragmentRefreshUi fragmentRefreshUi){
         fragmentRefreshUi1 = fragmentRefreshUi;
           final DeviceMangerSuccessFragment fragment=new DeviceMangerSuccessFragment();
         fragment.setArguments(savedInstanceState);
         return  fragment;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_manager_success_fragement, null);
    }

    private void initView(View view) {//初始化xlistview
        mListView = (XListView) view.findViewById(R.id.xListView_success);
        mListView.setFootViewHide();
        mListView.setPullLoadEnable(false);
        su_all1 = (TextView) view.findViewById(R.id.su_all);
        su_fail1 = (TextView) getView().findViewById(R.id.su_fail);//viewPager切换Fragment
        su_success = (TextView)view.findViewById(R.id.su_sucess1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO init datas
        CreateTable();//数据库表操作
        initView(view);
        initDatas();
        initEvent();
    }

    private void CreateTable() {//创建表
        sqLiteUtils = new SQLiteUtils();
        databaseHelper = sqLiteUtils.createDBHelper(context);
    }

    private void initEvent() {
        mListView.setXListViewListener(this);
        su_all1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
                    //viewPager+fragment切换
                    fragmentRefreshUi1.refreshUI(0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        su_fail1.setOnTouchListener(new View.OnTouchListener() {//viewPager切换fragment
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
                    //viewPager+fragment切换
                    fragmentRefreshUi1.refreshUI(2);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO load datas
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private void initDatas() {
        final Bundle savedInstanceState = getArguments();
        Map map = new HashMap();
        map = (Map) savedInstanceState.getSerializable("map");//拿到userName和projectCode的键值对
        if (map.size() != 0) {
            userName = (String) map.get("userName");
            projectCode = (String) map.get("projectCode");
        }
        List<Map> list =new ArrayList<>();
        list = getProjectCodeDeviceList();//拿到该项目编码下的设备列表
        xlist_adapter = new Xlist_success_getDeviceAdapter(context, list);
        mListView.setAdapter(xlist_adapter);
    }

    //拿到该项目编码下的设备列表
    private List<Map> getProjectCodeDeviceList() {
        sqlList = sqLiteUtils.getProjectCodeDeviceList(databaseHelper,projectCode,TABLE_NAME_LIU);//拿下默认projectCode
        //下的设备列表
        HashSet<Map> hashSet = new HashSet<>();
        List<Map> list = new ArrayList<>();
        //去数据库中查找对应ProjectCode下的设备列表

        for (Map sqlMap : sqlList) {
            Log.e(TAG1, "deviceMap:" + sqlMap.get("deviceMac"));
            if (sqlMap.get("type").equals("1")) {//已上传的数据
                hashSet.add(sqlMap);
            }
            list = new ArrayList<>();
            for (Map map : hashSet) {
                list.add(map);
            }
        }
        su_success.setText("已上传("+list.size()+")");
        return list;
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.setRefreshTime("刚刚");
    }


    @Override
    public void onRefresh() {//下拉刷新
        onLoad();
        //刷新一下
        List <Map> list = new ArrayList<>();
        list = getProjectCodeDeviceList();
        xlist_adapter.setList(list);
        xlist_adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {//上拉加载，暂时不用

    }

    private static FragmentRefreshUi fragmentRefreshUi1;

    @Override
    public void uploadedSucClicked(Map map) {//接收项目编码
        if (map.size() != 0) {
            userName = (String) map.get("userName");
            projectCode = (String) map.get("projectCode");
        }
        //刷新一下
        List <Map> list = new ArrayList<>();
        list = getProjectCodeDeviceList();
        xlist_adapter.setList(list);
        xlist_adapter.notifyDataSetChanged();
    }

    public interface FragmentRefreshUi {
        void  refreshUI(int index);
    }
    //在这里添加设备管理，下拉选项卡后对应的设备管理列表的实现方法，并在此刷新更新fragment列表

}
