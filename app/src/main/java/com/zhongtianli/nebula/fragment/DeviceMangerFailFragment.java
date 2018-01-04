package com.zhongtianli.nebula.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.DeviceDetailInfoActivity;
import com.zhongtianli.nebula.activity.DeviceManagerActivity;
import com.zhongtianli.nebula.adapter.Xlist_fail_getDeviceAdapter;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.interfaces.AdapterBackToDeviceManaAct;
import com.zhongtianli.nebula.interfaces.AdapterBackToDeviceManaAct_big;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.maxwin.view.XListView;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zhongtianli.nebula.R.id.fai_all;

/**
 * Created by tanger on 2016/4/11.
 */
public class DeviceMangerFailFragment extends Fragment implements XListView.IXListViewListener
,DeviceManagerActivity.UploadedFailListener{
    public static final String FLAG_INFO = "flagInfo";

    private String mInfo;
    private Context context;
    private TextView fai_all1;
    private TextView fai_sucess1;
    private XListView mListView;
    private Xlist_fail_getDeviceAdapter xlist_adapter;
    private SQLiteUtils sqLiteUtils;//数据库操作
    private DatabaseHelper databaseHelper;//数据库句柄
    //本地数据库需要先存储三个字段：device_type设备类型 ，device_mac：MAC地址  ，type上传是否成功 1成功,0失败
    private  final String  TABLE_NAME_LIU="projectCode";//
    private List<ConcurrentHashMap> sqlList = new ArrayList<>();//数据库拿到的ProjectCode下的-暂时没有分ProjectCode，
    private String TAG1 = "peng";
    private static DeviceManagerActivity.UploadedFailListener uploadedFailListener1;//传入可选的projectCode
    private String projectCode;//项目编码
    private TextView fai_all_shang;//未成功上传
    private String userName;//用户名
    private Map NameAddProjectCodemap;//存储用户名和可选的projectCode项目编码
    //已开发的就是，拿到数据库的数据是选择某个projectCode下的设备上传和收藏信息
    private static AdapterBackToDeviceManaAct_big adapterBackToDeviceManaAct_big1;//继续往前抛SetDeviceBean
    /**
     * @param  savedInstanceState
     *@param fragmentRefreshUi  @return
     */
     public  static DeviceMangerFailFragment instantiation(Bundle savedInstanceState, FragmentRefreshUi fragmentRefreshUi
     , AdapterBackToDeviceManaAct_big adapterBackToDeviceManaAct_big){
         fragmentRefreshUi1 = fragmentRefreshUi;
         adapterBackToDeviceManaAct_big1 = adapterBackToDeviceManaAct_big;
           final DeviceMangerFailFragment fragment=new DeviceMangerFailFragment();
         fragment.setArguments(savedInstanceState);
         return  fragment;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_fail_fragement, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CreateTable();//数据库表操作
        //TODO init datas
        initViews(view);
        initDatas();
        initEvent();

    }

    private void CreateTable() {//创建表
        sqLiteUtils = new SQLiteUtils();
        databaseHelper = sqLiteUtils.createDBHelper(context);
    }

    private void initEvent() {
        mListView.setXListViewListener(this);
        fai_all1.setOnTouchListener(new View.OnTouchListener() {
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

        fai_sucess1.setOnTouchListener(new View.OnTouchListener() {
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
    }

    private void initViews(View view) {
        mListView = (XListView) view.findViewById(R.id.xListView_fail);
        mListView.setFootViewHide();
        mListView.setPullLoadEnable(false);
        fai_all1 = (TextView) view.findViewById(fai_all);
        fai_all_shang = (TextView) view.findViewById(R.id.fai_fail1);
        fai_sucess1 = (TextView) getView().findViewById(R.id.fai_sucess);
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
        NameAddProjectCodemap = new HashMap();
        NameAddProjectCodemap= (Map) savedInstanceState.getSerializable("map");//拿到userName和projectCode的键值对
        if (NameAddProjectCodemap.size() != 0) {
            projectCode = (String) NameAddProjectCodemap.get("projectCode");
        }
        List<Map> list = new ArrayList<>();
        list = getFailDeviceListByDeviceCode();//初始化时拿到该projectCode下的数据库中的设备列表信息
        xlist_adapter = new Xlist_fail_getDeviceAdapter(context, list, NameAddProjectCodemap
                , new AdapterBackToDeviceManaAct() {
            @Override
            public void adapterBackToDeviceManaAct(SetDeviceBean setDeviceBean) {
                //继续往前抛，进入DeviceManagerActivity中去
                adapterBackToDeviceManaAct_big1.adapterBackToDeviceManaActBig(setDeviceBean);
            }
        });
        mListView.setAdapter(xlist_adapter);
    }

    //初始化时拿到该projectCode下的数据库中的设备列表信息
    private List<Map> getFailDeviceListByDeviceCode() {
        sqlList = sqLiteUtils.getProjectCodeDeviceList(databaseHelper,projectCode,TABLE_NAME_LIU);//拿下默认projectCode
        //下的设备列表
        HashSet<Map> hashSet = new HashSet<>();
        List<Map> list = new ArrayList<>();
        for (Map sqlMap : sqlList) {
            Log.e(TAG1, "deviceMap:" + sqlMap.get("deviceMac"));
            if (sqlMap.get("type").equals("0")) {//已收藏
                hashSet.add(sqlMap);
            }
            list = new ArrayList<>();
            for (Map map : hashSet) {
                list.add(map);
            }
        }
        fai_all_shang.setText("未上传("+list.size()+")");
        return list;
    }

    private static FragmentRefreshUi fragmentRefreshUi1;


    private void onLoad() {
        mListView.stopRefresh();
        mListView.setRefreshTime("刚刚");
    }


    @Override
    public void onRefresh() {//下拉刷新
        onLoad();
        List<Map> list = new ArrayList<>();
        list = getFailDeviceListByDeviceCode();
        xlist_adapter.setList(list,NameAddProjectCodemap);
        xlist_adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {//上拉加载，暂时不用

    }
     Handler uploadedDelayedHandler = new Handler();//防止 两次查询数据库数据之间的时间太短，有交互sqLiteUtils.getProjectCodeDeviceList
    @Override
    public void uploadedFailClicked(final Map map) {//接收ProjectCode项目编码-popwindow选择ProjectCode后，对应的设备列表
        if (map.size() != 0) {
            NameAddProjectCodemap = new HashMap();
            NameAddProjectCodemap = map;
            projectCode = (String) NameAddProjectCodemap.get("projectCode");

        }
        uploadedDelayedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG1,projectCode + "uploadedFailClicked");
                List<Map> list = new ArrayList<>();
                list = getFailDeviceListByDeviceCode();
                xlist_adapter.setList(list,NameAddProjectCodemap);
                xlist_adapter.notifyDataSetChanged();
            }
        },20);
    }

    public interface FragmentRefreshUi {
        void  refreshUI(int index);
    }
}
