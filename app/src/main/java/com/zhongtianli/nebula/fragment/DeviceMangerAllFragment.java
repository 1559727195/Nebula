package com.zhongtianli.nebula.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.DeviceManagerActivity;
import com.zhongtianli.nebula.adapter.Xlist_all_getDeviceAdapter;
import com.zhongtianli.nebula.bean.GetDevice;
import com.zhongtianli.nebula.maxwin.view.XListView;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.view.IShowNebula_GetDeviceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tanger on 2016/4/11.
 */
public class DeviceMangerAllFragment extends Fragment implements DeviceManagerActivity.OnButtonClickListener
, XListView.IXListViewListener,IShowNebula_GetDeviceView {
    public static final String FLAG_INFO = "url";
    private String mInfo;
    private Context context;
    private String TAG="robin debug";
    private TextView al_sucess;
    private TextView al_fail;
    private XListView mListView;
    private Xlist_all_getDeviceAdapter xlist_adapter;
    private FlightInfoPresenter flightInfoPresenter;
    private Map<String, String> mapPro;//存储用户名和项目编号
   private  GetDevice list = new GetDevice();//下载设备信息
    private List<Map> listMap = new ArrayList<>();//
    private  String projectCode;//项目编码
    private String userName;
    private TextView al_yun;//云端

    /**
     * @param  savedInstanceState
     * @param onButtonClickListener
     * @param fragmentRefreshUi
     * @return
     */
     public  static DeviceMangerAllFragment instantiation(Bundle savedInstanceState, DeviceManagerActivity.OnButtonClickListener onButtonClickListener,
                                                          FragmentRefreshUi fragmentRefreshUi){
          fragmentRefreshUi1 = fragmentRefreshUi;
           final DeviceMangerAllFragment fragment=new DeviceMangerAllFragment();
         onButtonClickListener = (DeviceManagerActivity.OnButtonClickListener)fragment;
         fragment.setArguments(savedInstanceState);
         return  fragment;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_all_fragement, container, false);
        initDatas();
        initViews(view);
        initEvent();
          return  view;
    }

    private void initEvent() {
        mListView.setFootViewHide();
        mListView.setPullLoadEnable(false);
        flightInfoPresenter = new FlightInfoPresenter(this);//下载设备信息
        flightInfoPresenter.getNebula_GetDevice(context,mapPro);//下载选择projectCode下的项目列表
        al_sucess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
                    //viewPager+fragment切换
                    fragmentRefreshUi1.refreshUI(1);
                    return true;
                } else {
                    return false;
                }
            }
        });

        al_fail.setOnTouchListener(new View.OnTouchListener() {
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
        mListView.setXListViewListener(this);
        xlist_adapter = new Xlist_all_getDeviceAdapter(context, listMap);
        mListView.setAdapter(xlist_adapter);
    }

    private void initViews(View view) {
        al_sucess = (TextView) view.findViewById(R.id.al_sucess);
        al_fail = (TextView) view.findViewById(R.id.al_fail);
        al_yun = (TextView) view.findViewById(R.id.a_all1);//云端
        mListView = (XListView) view.findViewById(R.id.xListView_all);

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
        //初始化时拿到projectCode
        Map map = new HashMap();
        map = (Map) savedInstanceState.getSerializable("map");//拿到userName和projectCode的键值对
        if (map.size() != 0) {
            userName = (String) map.get("userName");
            projectCode = (String) map.get("projectCode");
        }
        mapPro = new HashMap<>();
        mapPro.put("UserName", userName);
        mapPro.put("ProjectCode",projectCode);
    }

    @Override //传过来的ProjectCode项目编码
    public void onButtonClicked(Map map) {//在这里listView.setAdapter();//刷新适配器
        if (map.size() != 0) {
            userName = (String) map.get("userName");
            projectCode = (String) map.get("projectCode");
        }
        mapPro = new HashMap<>();
        mapPro.put("UserName", userName);
        mapPro.put("ProjectCode",this.projectCode);
        Log.e(TAG,"UserName:"+ userName +",ProjectCode:"+projectCode);
        //popwindow选择后传过来的-会重新加载下云端数据
        if (flightInfoPresenter != null) {
            flightInfoPresenter.getNebula_GetDevice(context, mapPro);//下载选择projectCode下的项目列表
        }
    }

    private static FragmentRefreshUi fragmentRefreshUi1;

    private void onLoad() {
        mListView.stopRefresh();
        mListView.setRefreshTime("刚刚");
    }


    @Override
    public void onRefresh() {//下拉刷新
        onLoad();
        if (flightInfoPresenter != null) {
            flightInfoPresenter.getNebula_GetDevice(context, mapPro);//下载选择projectCode下的项目列表
        }
    }

    @Override
    public void onLoadMore() {//上拉加载，暂时不用

    }

    @Override//下载ProjectCode下的列表成功
    public void showNebula_GetDevice(GetDevice list) {
        listMap = new ArrayList<>();
        for (int i = 0; i < list.getDevices().size(); i++) {
            Map map = new HashMap();
            map.put("deviceName",list.getDevices().get(i).getDeviceName());
            map.put("deviceType",list.getDevices().get(i).getDeviceType());
            map.put("deviceMac",list.getDevices().get(i).getDeviceMac());
            map.put("deviceStatus",list.getDevices().get(i).getDeviceStatus());
            listMap.add(map);
        }
       xlist_adapter.setList(listMap,"");
        xlist_adapter.notifyDataSetChanged();
        al_yun.setText("云端("+listMap.size()+")");
    }

    @Override//下载ProjectCode下的列表失败
    public void showNebula_FailedError() {
        listMap = new ArrayList<>();
        xlist_adapter.setList(listMap,"");
        xlist_adapter.notifyDataSetChanged();
        al_yun.setText("云端("+listMap.size()+")");
    }

    public interface FragmentRefreshUi {
        void  refreshUI(int index);
    }
}
