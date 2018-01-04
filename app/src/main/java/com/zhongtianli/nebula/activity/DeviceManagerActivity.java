package com.zhongtianli.nebula.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.adapter.Nebula_GetDevice_ManagerAdapter;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.finddreams.netstate.NetEvent;
import com.zhongtianli.nebula.finddreams.network.NetUtils;
import com.zhongtianli.nebula.finddreams.receiver.NetReceiver;
import com.zhongtianli.nebula.fragment.DeviceMangerAllFragment;
import com.zhongtianli.nebula.fragment.DeviceMangerFailFragment;
import com.zhongtianli.nebula.fragment.DeviceMangerSuccessFragment;
import com.zhongtianli.nebula.interfaces.AdapterBackToDeviceManaAct_big;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.model.IShowNebula_GetLoginProjects;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.receiver.ConnectionChangeReceiver;
import com.zhongtianli.nebula.utils.FinishUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.zhongtianli.nebula.R.drawable.device;
import static com.zhongtianli.nebula.R.id.text_select;
import static com.zhongtianli.nebula.R.string.login;

/**
 * Created by Administrator on 2016/10/23.
 */
public abstract class DeviceManagerActivity extends AppCompatActivity implements IShowNebula_GetLoginProjects{

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public ArrayList<Fragment> mFragmentList;
    public ViewPager mContentPager;
    private PagerAdapter mPagerAdapter;
    private Toolbar toolbars;
    private RelativeLayout device_manager_search;
    private String TAG = "robin debug";
    private TextView text_select_manager;
    private DeviceMangerAllFragment deviceMangerAllFragment;
    public List<LoginProjects> loginProjectses = new ArrayList<>();//由Main_activity传过来的projectCode列表
    private List<String> ProjectNamelist;//projectCode列表对应项目名称列表
    private String projectCode;//被选择的项目编码
    private DeviceMangerSuccessFragment deviceMangerSuccessFragment;
    private DeviceMangerFailFragment deviceMangerFailFragment;
    private SharedPreferences spCodeProject;//下载ProjectCode下的设备列表，需要将ProjectCode和UserName保存到Sp中
    private String userName;//用户名
    private Map projectCodeAndUserNamemap;//把用户名和可选的projectCode封装在一个HashMap中
    private int REQUEST_CODE_REFRESH_MANA = 0x0002;//intent-》activity之间跳转请求码-跳转回到主界面后，刷新主界面数据
    private SharedPreferences spUserInfo;//存储的是用户名和密码
    private String psd;//密码
    private HashMap mapUserAndPass;
    private String projectName;//可选的项目名
    private FlightInfoPresenter flightInfoPresenter;//用sp中的用户名和密码去拉取projects项目列表
    private NetReceiver mReceiver;//接收网络状况，然后刷新我们的列表
    public boolean isFirstMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_manager);
        FinishUtil.addActivity(this);
        registerReceiver();
        EventBus.getDefault().register(this);
        initDatas();
        initViews();
        initEvent();
    }



    public void onEventMainThread(NetEvent event) {
        setNetState(event.isNet());
    }



    public  int count = 0;//让网络好的时候或者拉取数据失败时，让count = 0;只加载一次数据
    public void setNetState(boolean netState) {

            //在这里加一个如果有网就去刷新UI，即用用户名和密码去项目列表，前提是projectLists为null,
            if (netState) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(count == 0 && loginProjectses.size() == 0) {//有网并且第一次主线程没有拿到loginProjects列表时在去拿
                            count++;
                            flightInfoPresenter.getNebula_GetLoginProjects(DeviceManagerActivity.this, mapUserAndPass, true);
                        }
                    }
                },10);//延时个200ms，方面主线程去拉取projectList列表，如果200ms，还没有拉取到就在这里重新拉取
            }
        }

    private void initEvent() {
        toolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //设备管理-选择touch事件
        device_manager_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                    showPopwindow();
                    return true;
                } else {
                    return false;
                }
            }
        });

        text_select_manager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
					 /*
				  * 在这里添加弹出自下往上popWindow
				  * */
                    showPopwindow();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void showPopwindow() {//弹出popwindow-选择框
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.device_manger_popwindow, null);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.list_device_manager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//水平
        recyclerView.setLayoutManager(linearLayoutManager);

        // 设置ItemAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        recyclerView.setHasFixedSize(true);
        // 初始化自定义的适配器

        /***
         *
         * 下面为device_manager_recycleview的测试数据-后期可以改动
         * **/

        Nebula_GetDevice_ManagerAdapter myAdapter =  new Nebula_GetDevice_ManagerAdapter(this,ProjectNamelist);
        // 为mRecyclerView设置适配器
        recyclerView.setAdapter(myAdapter);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        myAdapter.setOnItemClickListener(new Nebula_GetDevice_ManagerAdapter.MyItemClickListener() {
                                             @Override
                                             public void onItemClick(String item,int position) {
                                                 window.dismiss();
                                                 //项目编码
                                                 projectCode = DeviceManagerActivity.this.loginProjectses.get(position)
                                                         .getProjectCode();
                                                 text_select_manager.setText(item);
                                                 projectCodeAndUserNamemap();//发送和监听projectCode改变
                                             }
                                         }

        );

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(DeviceManagerActivity.this.findViewById(R.id.img_search_manager),
                Gravity.BOTTOM, 0, 0);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });
    }

    private void projectCodeAndUserNamemap() {//发送和监听projectCode改变
        projectCodeAndUserNamemap = new HashMap();//这里projectCode是可变的，所以需要重新封装HashMap
        projectCodeAndUserNamemap.put("userName",userName);
        projectCodeAndUserNamemap.put("projectCode",projectCode);
        if (onButtonClickListener != null) {
            onButtonClickListener.onButtonClicked(projectCodeAndUserNamemap);//点击popwindow把projectCode传
            //到fragment中去
        }
        if (uploadedFailListener != null) {//点击popwindow把projectCode传
            //到failfragment中去
            uploadedFailListener.uploadedFailClicked(projectCodeAndUserNamemap);
        }
        if (uploadedSucListener != null) {//点击popwindow把projectCode传
            //到successfragment中去
            uploadedSucListener.uploadedSucClicked(projectCodeAndUserNamemap);
        }
    }


    private void initDatas() {
        spCodeProject = getSharedPreferences("Nebula_GetDevice", MODE_PRIVATE);
        //用户名
        userName = spCodeProject.getString("UserName", "ni");
        projectName = spCodeProject.getString("ProjectName", "桂花公园");//拿到默认的，写入sp的projectCode优先显示
        projectCode = spCodeProject.getString("ProjectCode", "1111");//被选中的projectCode
        //projectCodeList-由Main_activity传过来的projectCode列表
        loginProjectses = (List<LoginProjects>) getIntent().getSerializableExtra("projectCodeList");
        //下面是拿到设备项目名称列表ProjectName
        ProjectNamelist = new ArrayList<>();

        //从sp中拿到用户名和密码
        spUserInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
//            //手机用户名
//            userName = sp.getString("UserName", "ni");
        //密码
        psd = spUserInfo.getString("Password", "");

        if (loginProjectses.size() > 0) {//在线登录后，由main_activity传过来的loginProject列表
            for (int i = 0; i < loginProjectses.size(); i++) {
                ProjectNamelist.add(loginProjectses.get(i).getProjectName());
            }
        } //没有loginProjectses时，就是离线登录的，离线登录在这里可以等网络变好,离线的话，等到有网时，去网上加载projects列表
        mapUserAndPass = new HashMap();
        mapUserAndPass.put("UserName", userName);
        mapUserAndPass.put("Password", psd);
        flightInfoPresenter = new FlightInfoPresenter(this);

        //把用户名和可选的projectCode封装在一个HashMap中
        projectCodeAndUserNamemap = new HashMap();
        projectCodeAndUserNamemap.put("userName", userName);
        projectCodeAndUserNamemap.put("projectCode", projectCode);

        Bundle bundle;
        mFragmentList = new ArrayList<Fragment>(3);

        bundle = new Bundle();
//        bundle.putString("projectCode",projectCode);//云端加载数据
        bundle.putSerializable("map", (Serializable) projectCodeAndUserNamemap);//云端加载数据
        deviceMangerAllFragment = DeviceMangerAllFragment.instantiation(bundle, onButtonClickListener,
                new DeviceMangerAllFragment.FragmentRefreshUi() {

                    @Override
                    public void refreshUI(int index) {//viewPager切换
                        mContentPager.setCurrentItem(index);
                    }
                });
        onButtonClickListener = (OnButtonClickListener) deviceMangerAllFragment;
//        onButtonClickListener.onButtonClicked(projectCode);//初始化时传过去默认的projectCode
        mFragmentList.add(deviceMangerAllFragment);

        bundle=new Bundle();
        bundle.putSerializable("map", (Serializable) projectCodeAndUserNamemap);//上传成功
        deviceMangerSuccessFragment = DeviceMangerSuccessFragment.instantiation(bundle,
                new DeviceMangerSuccessFragment.FragmentRefreshUi(){

                     @Override
                     public void refreshUI(int index) {
                         mContentPager.setCurrentItem(index);
                     }
                 });
        uploadedSucListener = (UploadedSucListener) deviceMangerSuccessFragment;
//         uploadedSucListener.uploadedSucClicked(projectCode);//初始化时传过去默认的projectCode
        mFragmentList.add(deviceMangerSuccessFragment);

        bundle=new Bundle();
        bundle.putSerializable("map", (Serializable) projectCodeAndUserNamemap);//上传失败
        deviceMangerFailFragment = DeviceMangerFailFragment.instantiation(bundle
                , new DeviceMangerFailFragment.FragmentRefreshUi() {

                    @Override
                    public void refreshUI(int index) {
                        mContentPager.setCurrentItem(index);
                    }
                }, new AdapterBackToDeviceManaAct_big() {
                    @Override
                    public void adapterBackToDeviceManaActBig(SetDeviceBean setDeviceBean) {//把setDeviceBean抛到这来
                        Intent intent = new Intent(DeviceManagerActivity.this,
                                DeviceDetailInfoActivity.class);
                        //添加一个字段判断是哪个activity进入到DeviceDetailInfoActivity中去的
                        intent.putExtra("activity_type","DeviceManagerActivity");
                        intent.putExtra("SetDeviceBean", setDeviceBean);
                        startActivityForResult(intent, REQUEST_CODE_REFRESH_MANA);
                        //intent-》activity之间跳转请求码-跳转回到主界面后，刷新主界面数据
                    }
                });
        uploadedFailListener = (UploadedFailListener) deviceMangerFailFragment;
//        uploadedFailListener.uploadedFailClicked(projectCode);//初始化时传过去默认的projectCode
        mFragmentList.add(deviceMangerFailFragment);
    }

    public void initLoginProjes(ArrayList<LoginProjects> list) {//保存第一次登录的账号和密码,应用被清理后，用老账号和密码在主界面请求项目列表
        if (list != null) {
            loginProjectses = new ArrayList<>();
            loginProjectses = list;
            for (int i = 0; i < loginProjectses.size(); i++){
                ProjectNamelist.add(loginProjectses.get(i).getProjectName());
            }
            //发送和监听projectCode改变
            projectCodeAndUserNamemap();
        }
    }

    private void initViews() {
        text_select_manager = (TextView) findViewById(R.id.text_select_manager); //显示
        text_select_manager.setText(projectName);//被选中哦的projectName
        device_manager_search = (RelativeLayout) findViewById(R.id.device_manager_search);
        toolbars = (Toolbar) findViewById(R.id.toolbar_device_manager);
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
        mContentPager = (ViewPager) findViewById(R.id.mViewPager);
        mPagerAdapter=getPagerAdapter();
        mContentPager.setAdapter(mPagerAdapter);
        mContentPager.setOffscreenPageLimit(3);//设置这句话的好处就是在viewapger下可以同时刷新3个fragment
        mContentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //把未上传的上传成功之后，回来刷新该DeviceManagerActivity
        if (requestCode == REQUEST_CODE_REFRESH_MANA) {
                    //在这里刷新三个Fragment对应的数据列表
                    if (onButtonClickListener != null) {
                        onButtonClickListener.onButtonClicked(projectCodeAndUserNamemap);//点击popwindow把projectCode传
                        //到fragment中去
                    }
                    if (uploadedFailListener != null) {//点击popwindow把projectCode传
                        //到failfragment中去
                        uploadedFailListener.uploadedFailClicked(projectCodeAndUserNamemap);
                    }
                    if (uploadedSucListener != null) {//点击popwindow把projectCode传
                        //到successfragment中去
                        uploadedSucListener.uploadedSucClicked(projectCodeAndUserNamemap);
                    }
        }
    }

    /**
     * 标志位，是否broadcaseReceiver开启，默认为false
     */
    boolean flag=false;

    /**
     * 注册广播
     */
    private  void registerReceiver(){//注册接收服务
        flag=true;//
        mReceiver = new NetReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    private  void unregisterReceiver(){//取消接收服务
        this.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        if(flag){
            flag=false;
            unregisterReceiver();
        }
        super.onDestroy();
    }

    protected  abstract PagerAdapter getPagerAdapter();

    private OnButtonClickListener onButtonClickListener;
    public interface OnButtonClickListener {//测试接口-在这里写一个设置activity多次携带数据返回fragment的接口实现
        void onButtonClicked(Map projectName);//设备管理名称
    }

    //-UploadedSuc
    private UploadedSucListener uploadedSucListener;//上传成功

    public interface UploadedSucListener {//测试接口-在这里写一个设置activity多次携带数据返回fragment的接口实现
        void uploadedSucClicked(Map projectName);//设备管理名称
    }

    private UploadedFailListener uploadedFailListener;//上传失败

    public interface UploadedFailListener {//测试接口-在这里写一个设置activity多次携带数据返回fragment的接口实现
        void uploadedFailClicked(Map projectName);//设备管理名称
    }
}
