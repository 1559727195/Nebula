package com.zhongtianli.nebula.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.library.SlidingMenu;
import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.massky.ywx.ackpasslibrary.OnOpenDeviceListener;
import com.massky.ywx.ackpasslibrary.OnScanListener;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.DeviceDetailInfoActivity;
import com.zhongtianli.nebula.adapter.Nebula_GetDeviceAdapter;
import com.zhongtianli.nebula.adapter.Xlist_View_getDevice_scan_Adapter;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.finddreams.netstate.NetEvent;
import com.zhongtianli.nebula.finddreams.network.NetUtils;
import com.zhongtianli.nebula.finddreams.receiver.NetReceiver;
import com.zhongtianli.nebula.fragment.HomeFragment;
import com.zhongtianli.nebula.fragment.HomeSecondFragment;
import com.zhongtianli.nebula.interfaces.AdapterBackToAct;
import com.zhongtianli.nebula.interfaces.FragmentRefreshUi;
import com.zhongtianli.nebula.interfaces.OnMainLanYaOverListener;
import com.zhongtianli.nebula.model.IShowNebula_GetLoginProjects;
import com.zhongtianli.nebula.myzxingbar.qrcodescanlib.CaptureActivity;
import com.zhongtianli.nebula.presenter.FlightInfoPresenter;
import com.zhongtianli.nebula.receiver.ConnectionChangeReceiver;
import com.zhongtianli.nebula.sqlite.DatabaseHelper;
import com.zhongtianli.nebula.sqlite.SQLiteUtils;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhongtianli.nebula.view.DrawerView;
import com.zhongtianli.nebula.view.IShowFragmentView;
import com.zhongtianli.nebula.view.NavigationDrawerFragment;
import com.zhongtianli.nebula.view.PullToLoadView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

import static com.zhongtianli.nebula.R.menu.main;
import static com.zhongtianli.nebula.R.string.login;

@SuppressLint("NewApi")
public class Main_New_Activity extends AppCompatActivity implements
		View.OnClickListener,OnScanListener,IShowNebula_GetLoginProjects {

	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private PullToLoadView mPullToLoadView;
	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private ImageView select_img;
	private FlightInfoPresenter flightInfoPresenter;
	private Fragment mFragHome;
	private Fragment mFragMe;
	private IShowFragmentView iShowFragmentView1;
	private List<LoginProjects> loginProjectses =new ArrayList<>();
	private SharedPreferences sp;
	private SharedPreferences spCodeProject;
	private Map<String, String> projectCodeAndUserNamemap;
	private String TAG="robin debug";
	private List<String> ProjectNamelist;
	private TextView text_select;
	private String userName="";
	protected SlidingMenu side_drawer;//侧滑选择
	private FrameLayout container;
	private ImageView img_search1;
	private  ArrayList<ConcurrentHashMap> success = new ArrayList<>();
	private  ArrayList<Map> qR_success = new ArrayList<>();//临时存储二维码的容器
	private RelativeLayout relati_img_search;
	private RelativeLayout toolbar_layout;
	private ImageView toolbar_scanel;
	private boolean isRefresh;
	private Xlist_View_getDevice_scan_Adapter xlist_adapter;
	private AckpassClass mAckpassClass;//蓝牙jar包
//	private ArrayList<HashMap> listMap = new ArrayList<>();//存储蓝牙扫描的设备信息
	private Set<ConcurrentHashMap> setMap = new HashSet<ConcurrentHashMap>();////存储蓝牙扫描的设备信息
	private String TAG1 = "zhu";
	Handler scanHandler = new Handler();
	private int REQUEST_CODE_SCAN = 0x0000;//intent-》activity之间跳转请求码
	private int REQUEST_CODE_REFRESH = 0x0001;//intent-》activity之间跳转请求码-跳转回到主界面后，刷新主界面数据
	private static final String DECODED_CONTENT_KEY = "codedContent";//扫码的内容
//	private boolean isSwipeCode;//二维码-扫描后进入的listView列表，刷新时，不进行实际刷新
	private SQLiteUtils sqLiteUtils;//数据库操作
	private DatabaseHelper databaseHelper;//数据库句柄
	//本地数据库需要先存储三个字段：device_type设备类型 ，device_mac：MAC地址  ，type上传是否成功 1成功,0失败
	private  final String  TABLE_NAME_LIU="projectCode";//
	private SharedPreferences.Editor editor_code;
	private  String projectCode;//可选的项目及编码
	private RelativeLayout rel_right_cr;//二维码扫描
	private String psd;//密码
	private NetReceiver mReceiver;//接收网络状况，然后刷新我们的列表
	private RelativeLayout no_network_rl;
	private Map mapUserAndPass = new HashMap();
	private boolean isFirstMain;//首次成功拿到数据
	private String projectName;//项目名称

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_topdrawer);
		FinishUtil.addActivity(this);
		CreateTable();//数据库表操作
		registerReceiver();//注册实时网络监听广播服务
		EventBus.getDefault().register(this);
		initViews();
		initData();//初始化数据
		initEvent();
	}

	public void onEventMainThread(NetEvent event) {//接收网络状况，然后刷新我们的列表,实时监视网络变化
		setNetState(event.isNet());
	}

	public void setNetState(boolean netState) {

		if (no_network_rl != null) {
			no_network_rl.setVisibility(netState ? View.GONE : View.VISIBLE);
			//在这里加一个如果有网就去刷新UI，即用用户名和密码去项目列表，前提是projectLists为null,
			if (netState) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(isFirstMain && loginProjectses == null) {//有网并且第一次主线程没有拿到loginProjects列表时在去拿
							flightInfoPresenter.getNebula_GetLoginProjects(Main_New_Activity.this, mapUserAndPass, false);
							isFirstMain = false;
						}
					}
				},50);//延时个200ms，方面主线程去拉取projectList列表，如果200ms，还没有拉取到就在这里重新拉取
			}

			no_network_rl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					NetUtils.startToSettings(Main_New_Activity.this);
				}
			});
		}
	}

	private static final int REQUEST_FINE_LOCATION=0;
	private void mayRequestLocation() {//提权防止蓝牙在高版本跑不了,目的是让蓝牙设备能够在4.0,5.0，和6.0上的手机跑起来
		int versionint = Integer.parseInt(android.os.Build.VERSION.SDK);
		Log.e(TAG,"versionint:"+versionint);
		if (versionint >= 23) {
			//int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(Main_New_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
			if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
				//判断是否需要 向用户解释，为什么要申请该权限
				if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
					Toast.makeText(Main_New_Activity.this,"ble_need", Toast.LENGTH_SHORT).show();
				ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_FINE_LOCATION);
				return;
			}else{
				scanDevice();
			}
		}
		else if (versionint < 23 && versionint > 20){
			// scanLeDevice5(true);
			scanDevice();
		}
		else {
			scanDevice();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_FINE_LOCATION:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// The requested permission is granted.
					// scanLeDevice5(true);
					scanDevice();
				} else{
					// The user disallowed the requested permission.
					Toast.makeText(Main_New_Activity.this,"requested permission fail", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void initLanYa() {//蓝牙初始化
		initSwipeScan();
		BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
		if(blueadapter.isEnabled()){//true表示已经开启，false表示蓝牙并没启用。
			Log.e(TAG1,"blueadapter.isEnabled()");
			mayRequestLocation();//蓝牙扫描设备
		} else {//蓝牙没有开启或正在开启中
			scanHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mayRequestLocation();
					Log.e(TAG1,"blueadapter.isUnEnabled()");
				}
			},1000);//延时一秒执行
		}
	}

	private void initSwipeScan() {
		mAckpassClass =new AckpassClass(this);
		mAckpassClass.setOnScanListener(this);
		mAckpassClass.setOnOpenDeviceListener(new OnOpenDeviceListener() {
			@Override
			public void OnOpenDevice(int status) {
				switch (status) {
					case 0:
						toast("成功");
						break;
					case 1:
						toast("失败");
						break;
					case 2:
						toast("连接设备失败");
						break;
					case 3:
						toast("参数错误");
						break;
					case 4:
						toast("其他异常");
						break;
				}
			}
		});
			//检查一下如果不支持BLE，直接退出
			if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
				Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
				finish();
			}

			if (!mAckpassClass.Initialize()){//没注册成功
				Toast.makeText(this, "Initialize fail", Toast.LENGTH_SHORT).show();
				finish();
			}
	}

	private void toast(String content) {//
		Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
	}

	private void scanDevice() {//扫描蓝牙
		mAckpassClass.StartScan();//初始时开始扫描
		mHandler.postDelayed(r,0);//延时100毫秒
	}

	int  i = 0;
	// ler类的postDelayed方法：
	Handler mHandler = new Handler();
	private int scanTime = 1000;//蓝牙初始扫描时间
	Runnable r = new Runnable() {

		@Override
		public void run() {
			//do something
			//每隔1s循环执行run方法
			i++;
			if (i > scanTime) {
				i = 0;
				scanHandlers.sendEmptyMessage(0);
			}
			Log.e(TAG,i+"");
			mHandler.postDelayed(this, 1);
		}
	};

	private boolean canAddDel;//防止 listMap删除其中重复的元素时，其他地方还在给它添加新元素，
	// 定义Handler
	final Handler scanHandlers = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//handler处理消息
			if (msg.what == 0) {
				mAckpassClass.StopScan();//停止扫描
				Log.e(TAG1,setMap.size()+"->setMap");
				canAddDel = true;//防止 listMap删除其中重复的元素时，其他地方还在给它添加新元素，
//				HashSet<Map> hs = new HashSet<Map>(setMap); //此时已经去掉重复的数据保存在hashset中
				//在这里选ProjectCode时，设备deviceMac容易在该ProjectCode下重复,所以在此遍历列表,去除重复deviceMac
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						canAddDel = false;
					}
				},20);
				mHandler.removeCallbacks(r);
				Log.e(TAG1,setMap.size()+"->HashSet");
				Log.e(TAG,"貌似是时间到了");
				ArrayList<ConcurrentHashMap> list = new ArrayList<>();
				for (ConcurrentHashMap map : setMap) {
					list.add(map);
				}
				if (list.size() == 0) {//说明没有扫到设备，返回homeFragment
					//失败恢复第一个fragment状态
					setSelect(0);//加载设备信息失败，切换回第一个HomeFragment碎片
					Log.e(TAG,"加载设备信息失败");
					iShowFragmentView1.showFailedError();
				} else {//说明扫到设备，进入第二个SecondFragment
					success = list;
					//在这里我要从数据库中拿已经保存和上传的deviceName数据，去更新蓝牙设备的名字，根据deviceMac
					sqlLanDeviceMac();//匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
					//和蓝牙扫描到的deviceMac相等
					setSelect(1);//切换到第二个fragment来显示数据
					if (isRefresh) {
						isRefresh = false;
					} else {
//						Toast.makeText(Main_New_Activity.this, "下载设备信息成功", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};

	private void sqlLanDeviceMac() {//匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
		List<ConcurrentHashMap> sqlList = new ArrayList<>();//存储数据库中的设备信息
        sqlList = sqLiteUtils.getProjectCodeDeviceList(databaseHelper,projectCode,TABLE_NAME_LIU);//拿到可选项目编码projectCode下的设备列表
		success = (ArrayList<ConcurrentHashMap>) getIntersection(success,sqlList);//从数据库中拿到的deviceMac
	}

	//把两个list里边相同的数据取出
	public  List<ConcurrentHashMap> getIntersection(List<ConcurrentHashMap> success,
										  List<ConcurrentHashMap> sq1List) {
		//拿到success里下的所有mac地址
		List<ConcurrentHashMap> result = new ArrayList<ConcurrentHashMap>();
		for (int j = 0; j < success.size(); j++) {//遍历list1
		     for (int i = 0; i < sq1List.size(); i++) {
				 if (success.get(j).get("deviceMac").equals(sq1List.get(i).get("deviceMac"))) {//从数据库中拿到的deviceMac
					 //和蓝牙扫描到的deviceMac相等
					 success.get(j).put("deviceName",sq1List.get(i).get("deviceName"));
				 }
			 }
		}
		result = success;
		return result;
	}

	private void CreateTable() {//创建表
		sqLiteUtils = new SQLiteUtils();
		databaseHelper = sqLiteUtils.createDBHelper(this);
	}

	private void initData() {
		Intent intent = getIntent();
		spCodeProject = getSharedPreferences("Nebula_GetDevice", MODE_PRIVATE);
		//获取写入设备下项目编码的sp-editor
		editor_code = spCodeProject.edit();

		//拿到Projects项目列表
		loginProjectses = (ArrayList<LoginProjects>) intent.getSerializableExtra("list");
		//从sp中拿到用户名和密码
		sp = getSharedPreferences("userInfo", MODE_PRIVATE);
		//手机用户名
		userName = sp.getString("UserName","ni");
		//密码
		psd = sp.getString("Password","");
		//下面是拿到设备项目名称列表ProjectName
		ProjectNamelist = new ArrayList<>();
		if (loginProjectses != null) {//第一次登录
			for (int i = 0; i < loginProjectses.size(); i++){
				ProjectNamelist.add(loginProjectses.get(i).getProjectName());
			}
			projectCodeAndUserNamemap = new HashMap<>();//初始化二.	下载设备信息（Nebula_GetDevice）json容器对象
			projectCodeAndUserNamemap.put("UserName",userName);
			String ProjectCode = Main_New_Activity.this.loginProjectses.get(0)
					.getProjectCode();
			projectCodeAndUserNamemap.put("ProjectCode",ProjectCode);//拿到默认第一个项目下的项目编码
			this.projectCode = ProjectCode;//首先projectCode下
			//添加一个测试数据
			projectName = loginProjectses.get(0).getProjectName();
			text_select.setText(projectName);//初始化项目名
			editor_code.putString("UserName",userName);
			editor_code.putString("ProjectCode",projectCode);
			editor_code.putString("ProjectName",projectName);
			//提交当前数据
			editor_code.commit();
//			if(!initSlideMenuSp.getBoolean("OneTime",false))//说明initSlideMenu还没有被执行
			initSlidingMenu();//初始化左侧侧滑菜单,不会被执行第二次
		} else {//说明已经上次成功登录，本次保存登录状态，在在去请求数据
			mapUserAndPass = new HashMap();
			mapUserAndPass.put("UserName",userName);
			mapUserAndPass.put("Password",psd);

			//从sp中拿上次退出应用前最后一次可选的projectCode和projectName
			projectCode = spCodeProject.getString("ProjectCode","1111");//被选中的projectCode
			projectName = spCodeProject.getString("ProjectName","桂花公园");
			text_select.setText(projectName);//被选中哦的projectName

			flightInfoPresenter = new FlightInfoPresenter(this);
			flightInfoPresenter.getNebula_GetLoginProjects(this, mapUserAndPass,true);
		}
	}

	//初始化左侧滑屏菜单
	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this, new OnMainLanYaOverListener() {
			@Override
			public void onMainLanYaOver() {
				//MainActivity要进入DeviceWorkActivity中去了
				if (mAckpassClass != null) {
					mAckpassClass.StopScan();
				}
			}
		}).initSlidingMenu(projectCodeAndUserNamemap,loginProjectses);//传个用户名过去userName
	}

	private void initEvent() {
		LinearLayoutManager manager = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		relati_img_search.setClickable(true);
		relati_img_search.setOnTouchListener(new View.OnTouchListener() {
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

		text_select.setOnTouchListener(new View.OnTouchListener() {//点击项目其他区域，也弹出popWindow
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
		success = new ArrayList<>();
		setSelect(0);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {//侧滑home键-左上角
			@Override
			public void onClick(View v) {
				if (side_drawer != null) {
					if (side_drawer.isMenuShowing()) {
						side_drawer.showContent();
					} else {
						side_drawer.showMenu();
					}
				} else {
					initSlidingMenu();//初始化左侧侧滑菜单
					if (side_drawer.isMenuShowing()) {
						side_drawer.showContent();
					} else {
						side_drawer.showMenu();
					}
				}
			}
		});

		//二维码扫描
		toolbar_scanel.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作
					// 解码:将二维码图片解码 返回解码结果
					Intent intent = new Intent(Main_New_Activity.this , CaptureActivity.class);
					startActivityForResult(intent, REQUEST_CODE_SCAN);
					initSwipeScan();//蓝牙注册初始化
					return true;
				}else {
					return false;
				}
			}
		});//二维码扫描
		rel_right_cr.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {//监听侧滑的action动作
					// 解码:将二维码图片解码 返回解码结果
					Intent intent = new Intent(Main_New_Activity.this , CaptureActivity.class);
					startActivityForResult(intent, REQUEST_CODE_SCAN);
					initSwipeScan();//蓝牙注册初始化
					return true;
				}else {
					return false;
				}
			}
		});
	}

	@Override//扫到的二维码
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {
				//解码的url
				String content = data.getStringExtra(DECODED_CONTENT_KEY);// http://weixin.qq.com/r/bWTZwbXEsOjPrfGi9zF-
				//试扫微信的二维码
				Log.e(TAG1,content);
				swipeCode(content);//扫码代码
			}
		} else  if (requestCode == REQUEST_CODE_REFRESH) {//说明deviceName修改了，回到主界面
			//来刷新主界面数据
			//scanTime = 500;//防止mainActi还没有刷新完，就进入到deviceWorkActivity中去刷新它的列表
			new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					reFreshDeviceInfo(1000);//刷新蓝牙列表->在子activity返回到主acti后，刷新主activity列表
					Looper.loop();
				}
			}).start();
		}
	}

	private void swipeCode(String content) {
		//把这个二维码进行验证，然后添加到list中
		String  testContent = "41636B50617373\n" +
                "0FFF6206\n" +
                "414B500000E2\n" +
                "01\n" +
                "0100000000";//测试二维码数据--长度46
		//先全部转换为小写
		testContent = content.toLowerCase();
		String contentNew = replaceBlank(testContent);//去除换行符和空格
		String identifierName = contentNew.substring(0,14);//识别符
		Log.e(TAG1,"identifierName:"+identifierName);
		long length = contentNew.length();
		Log.e(TAG1,"length:"+length);
		if (identifierName.contains("41636b50617373") && contentNew.length() == 46) {
//满足条件进一步，处理
            //拿到MAC 地址
            String deviceMac = contentNew.substring(22,34);//MAC地址
			StringBuilder  sb = new StringBuilder (deviceMac);
			sb.insert(10, ":");
			sb.insert(8, ":");
			sb.insert(6, ":");
			sb.insert(4, ":");
			sb.insert(2, ":");
			String mACStrNew = sb.toString();
			String deviceType = contentNew.substring(34,36);//设备类型
            String paramId="11111111";//为4字节卡号，用16进制字符串表示数据
			ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
            map.put("deviceMac", mACStrNew);//-41:4B:50:00:00:66
            map.put("deviceName", identifierName);//AckPass
            map.put("deviceType", deviceType);//01
            map.put("paramId", paramId);//先添加这4个字段
            map.put("type",listTypeOfItem(deviceType));//为ListView添加不同的Item布局文件
			boolean isQrItem = false;//是不是蓝牙扫描的item
			int count = 0;
			if (success.size() == 0) {//说明还没有搜索蓝牙设备
				success.add(0,map);
				sqlLanDeviceMac();//匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
			} else {//说明有搜到蓝牙数据
				for (Map sus : success) {//
					if (!sus.get("deviceMac").equals(map.get("deviceMac"))) {//说明二维码扫到的设备，
						//蓝牙已经扫到
						count++;
						isQrItem = true;
					}
				}
				if (isQrItem && count == success.size()) {//说明全部都不等，则说明二维码扫描的为新设备
					success.add(0,map);
					sqlLanDeviceMac();//匹配蓝牙设备和数据库中，设备deviceName根据deviceMac来比较
					isQrItem = false;
				}
			}
                setSelect(1);
        } else {
            toast("非Ackpass产品");
        }
	}

	//去除字符串中的换行符空格等
	public  String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	private void initViews() {
		no_network_rl = (RelativeLayout) findViewById(R.id.net_view_rl);//网络不好时，弹出提示框
		toolbar_scanel = (ImageView) findViewById(R.id.toolbar_scanel); //二维码扫描
		rel_right_cr = (RelativeLayout) findViewById(R.id.rel_right_cr);//二维码扫描
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		container = (FrameLayout) findViewById(R.id.container);
		text_select = (TextView) findViewById(R.id.text_select);
		img_search1 = (ImageView) findViewById(R.id.img_search);
		relati_img_search = (RelativeLayout) findViewById(R.id.relati_img_search);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar侧滑菜单
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//fragment切换
	public void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		switch (i) {
			case 0:
				hideFragment(transaction);
				if (mFragHome == null) {
					mFragHome = HomeFragment.newInstance(new FragmentRefreshUi() {
						@Override
						public void refreshUI(IShowFragmentView iShowFragmentView) {//homeFragment点击搜索时，响应的事件
							//fragment刷新ui->activity
							new Thread(new Runnable() {//在这里添加异步，目的是防止打开蓝牙时，出现黑屏
								@Override
								public void run() {
									Looper.prepare();
									if (!canAddDel) {//防止listMap删除其中重复元素时，其他地方还在给它添加新元素
										setMap = new HashSet<>();
									}
									scanTime = 700;//HomeFragment切换到SecondFragment时，时间为1000

									initLanYa();
//									isSwipeCode = false;//是HomeFragment进入到SecondHomeFragment时搜索本地的下拉刷新，,非二维码下拉刷新
									Looper.loop();
								}
							}).start();
						iShowFragmentView1 = iShowFragmentView;
						}
					});
					transaction.add(R.id.container, mFragHome);
				} else {
					transaction.show(mFragHome);
				}
				break;
			case 1:
				hideFragment(transaction);
				if (projectCodeAndUserNamemap == null) {//map里面存储的是可选的projectCode和userName,为null，说明是离线登录的,从sp中拿默认的projectCode
					projectCodeAndUserNamemap = new HashMap<>();
					projectCodeAndUserNamemap.put("ProjectCode",projectCode);
					projectCodeAndUserNamemap.put("UserName",userName);
				}
                if (mFragMe == null) {
					ArrayList<ConcurrentHashMap> successl;
					mFragMe = HomeSecondFragment.newInstance(this, new HomeSecondFragment.FragmentRefreshList() {//下拉刷新列表
						@Override
						public void refreshList() {
							new Thread(new Runnable() {
								@Override
								public void run() {
									Looper.prepare();
									reFreshDeviceInfo(1000);//刷新蓝牙列表
									Looper.loop();
								}
							}).start();
						}
					}, new AdapterBackToAct() {//back键回来后refresh主界面蓝牙数据
						@Override
						public void adapterBackToAct(SetDeviceBean setDeviceBean) {
							Intent intent = new Intent(Main_New_Activity.this,
									DeviceDetailInfoActivity.class);
							intent.putExtra("SetDeviceBean", setDeviceBean);
							//添加一个字段判断是哪个activity进入到DeviceDetailInfoActivity中去的
							intent.putExtra("activity_type","Main_NewActivity");
							startActivityForResult(intent, REQUEST_CODE_REFRESH);
							//intent-》activity之间跳转请求码-跳转回到主界面后，刷新主界面数据
						}
					});
					Bundle bundle = new Bundle();
					if (success == null) {
						successl= new ArrayList<ConcurrentHashMap>();//实例化防报空指针
					} else {
						successl = success;
					}

					bundle.putSerializable("user", (Serializable) projectCodeAndUserNamemap);
					bundle.putSerializable("GetDevice", (Serializable) successl);
					mFragMe.setArguments(bundle);
					transaction.add(R.id.container, mFragMe);
                } else {
                    transaction.show(mFragMe);
                }
				onButtonClickListener = (OnButtonClickListener)mFragMe;
				onButtonClickListener.onButtonClicked(success,projectCodeAndUserNamemap);
				break;
		}
		transaction.commitAllowingStateLoss();
	}

	private void reFreshDeviceInfo(int scanTime) {//刷新蓝牙列表
//		if (!isSwipeCode) { //二维码的数据item下拉刷新时,不进行刷新
            isRefresh = true;//正在刷新-如果提示，则提示刷新成功或失败
			this.scanTime = scanTime;//扫码时扫描时间为1000
            //在这里刷新项目下的设备列表
            if ((System.currentTimeMillis() - mExitTime) > 2000 && !mHandler.hasMessages(0)) {
                if (!canAddDel) {//防止listMap删除其中重复元素时，其他地方还在给它添加新元素
//                    listMap = new ArrayList<>();
					setMap = new HashSet<>();
                }
				//刷新的时候如果蓝牙被手动关掉，需要在这里重启
				BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
				if(blueadapter.isEnabled()){//true表示已经开启，false表示蓝牙并没启用。
					Log.e(TAG1,"blueadapter.isEnabled()");
					mayRequestLocation();//蓝牙扫描设备
				} else {//蓝牙没有开启或正在开启中
					new Thread(new Runnable() {//在这里添加异步，目的是防止打开蓝牙时，出现黑屏
						@Override
						public void run() {
							Looper.prepare();
							initSwipeScan();
							scanHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									mayRequestLocation();
									Log.e(TAG1,"blueadapter.isUnEnabled()");
								}
							},1000);//延时一秒执行
							Looper.loop();
						}
					}).start();
				}
                mExitTime = System.currentTimeMillis();
            } else {
                //防止listView刷新过于频繁
            }
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (mFragHome != null) {
			transaction.hide(mFragHome);
		}
		if (mFragMe != null) {
			transaction.hide(mFragMe);
		}
	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.getdevice_recycler_view, null);
           RecyclerView  recyclerView= (RecyclerView) view.findViewById(R.id.list);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//水平
		recyclerView.setLayoutManager(linearLayoutManager);

		// 设置ItemAnimator
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		// 设置固定大小
		recyclerView.setHasFixedSize(true);
		// 初始化自定义的适配器
		Log.e(TAG,"ProjectNamelist:"+ProjectNamelist.size());
		Nebula_GetDeviceAdapter myAdapter =  new Nebula_GetDeviceAdapter(this,ProjectNamelist);
		// 为mRecyclerView设置适配器
		recyclerView.setAdapter(myAdapter);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		final String userNumber = userName;
		myAdapter.setOnItemClickListener(new Nebula_GetDeviceAdapter.MyItemClickListener() {
											 @Override
											 public void onItemClick(String item,int position) {
												 Log.e(TAG,"popwindow里面的item被点击了"+item);
												 projectName = item;
												 text_select.setText(item);
												 window.dismiss();
												 //根据项目编码和用户名和-得到该项目下的设备信息
												 projectCodeAndUserNamemap = new HashMap<>();
												 if (loginProjectses.size() != 0) {
													 projectCodeAndUserNamemap.put("UserName",userNumber);
													 String ProjectCode = Main_New_Activity.this.loginProjectses.get(position)
															 .getProjectCode();
													 projectCodeAndUserNamemap.put("ProjectCode",ProjectCode);
													 projectCode = ProjectCode;//可选下的projectCode
													 //下载ProjectCode下的设备列表，需要将ProjectCode和UserName保存到Sp中
													 editor_code.putString("UserName",userNumber);
													 editor_code.putString("ProjectCode",projectCode);
													 editor_code.putString("ProjectName", projectName);
													 editor_code.commit();
													 //方便在设备管理里去下载ProjectCode下的设备信息
//													 SendBroadCastToDeviceWorkAct(ProjectCode);//传可选的ProjectCode到DeviceWorkActivity中去
												 }

//												 Log.e(TAG,"UserName:"+userNumber+";ProjectCode:"+MainActivity.this.loginProjectses.get(position)
//														 .getProjectCode());
												 //如果项目item被选择，则fragment回到HomeFragment",以供在次下载该项目下的设备信息
												 setSelect(0);//加载设备信息失败，切换回第一个HomeFragment碎片
												 Log.e(TAG,"setSelect(0):"+item);
												 if (iShowFragmentView1 != null) {
													 iShowFragmentView1.showFailedError();//加一个判断防止iShowFragmentView1该对象为null
												 }
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
		window.showAtLocation(Main_New_Activity.this.findViewById(R.id.img_search),
				Gravity.BOTTOM, 0, 0);
		//popWindow消失监听方法
		window.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});
	}

	private  OnButtonClickListener onButtonClickListener;

	@Override
	public void OnScan(String deviceMac,String deviceName,String deviceType) {//扫描
		Log.e(TAG, "deviceMac:" + deviceMac + " deviceName:" + deviceName + " deviceType:" + deviceType);
		//扫描个2秒，然后停掉，然后下拉刷新时，在扫个两秒
		String paramId="11111111";//为4字节卡号，用16进制字符串表示数据
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
		deviceMac = deviceMac.toLowerCase();
		map.put("deviceMac", deviceMac);
		map.put("deviceName", deviceName);
		map.put("deviceType", deviceType);
		map.put("paramId", paramId);//先添加这4个字段
		map.put("type",listTypeOfItem(deviceType));//为ListView添加不同的Item布局文件
		if(!canAddDel){//防止listMap在删除其中重复的元素时，还在给它添加新元素
//			listMap.add(map);
			setMap.add(map);
		}
	}

	private int listTypeOfItem(String deviceType) {//扫描结果列表
		int type = 0;
		switch (deviceType) {//这边负责显示
			case "06"://梯控
				type = 0;
				break;
			case "01"://门禁
				type = 0;
				break;
			case "02"://摆闸
				type = 1;
				break;
			case "03"://车库
				type = 1;
				break;
			case "04"://车位锁
				type = 0;
				break;
			case "05"://电子锁
				type = 0;
				break;
			case "07"://呼梯
				type = 0;
				break;
		}
		return   type;
	}

	@Override
	public void showNebula_GetLoginProjects(ArrayList<LoginProjects> list, boolean isFirst) {//保存第一次登录的账号和密码,应用被清理后，用老账号和密码在主界面请求项目列表
		initLoginProjes(list);
		isFirstMain = isFirst;//首次成功拿到数据
		if (isFirst) {
			initSlidingMenu();//初始化左侧侧滑菜单
		}
	}

	private void initLoginProjes(ArrayList<LoginProjects> list) {//保存第一次登录的账号和密码,应用被清理后，用老账号和密码在主界面请求项目列表
		if (list != null) {
			loginProjectses = list;
			for (int i = 0; i < loginProjectses.size(); i++){
				ProjectNamelist.add(loginProjectses.get(i).getProjectName());
			}
			projectCodeAndUserNamemap = new HashMap<>();//初始化二.	下载设备信息（Nebula_GetDevice）json容器对象
			projectCodeAndUserNamemap.put("UserName",userName);
			projectCodeAndUserNamemap.put("ProjectCode",projectCode);//拿到默认第一个项目下的项目编码
			text_select.setText(projectName);//被选中哦的projectName
		}
	}

	@Override
	public void showNebula_LoginProjectsError(boolean isFirst) {//登录的用户名和密码是在主界面去拿项目列表
		toast("没有网络");
		//没有网时，从sp中拿默认被选中的projectCode和projectName
	    projectCode = spCodeProject.getString("ProjectCode","1111");//被选中的projectCode
		text_select.setText(spCodeProject.getString("ProjectName","桂花公园"));//被选中哦的projectName

		isFirstMain = isFirst;//首次成功拿到数据
		if (isFirst) {
			initSlidingMenu();
		}
	}

	public interface OnButtonClickListener {//测试接口-在这里写一个设置activity多次携带数据返回fragment的接口实现
		void onButtonClicked(ArrayList<ConcurrentHashMap> success, Map user);
	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(side_drawer.isMenuShowing() ||side_drawer.isSecondaryMenuShowing()){
				side_drawer.showContent();
			}else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "点击退出应用程序",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
//					moveTaskToBack(false);//两次点击退出应用程序后，程序在后台跑，activity没有被销毁
					finish();
					return true;
				}
			}
			return true;
		}
		//
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		//退出应用前保存最后一次可选的projectCode和projectName到sp中
		editor_code.putString("UserName",userName);
		editor_code.putString("ProjectCode",projectCode);
		editor_code.putString("ProjectName",projectName);
		editor_code.commit();
		Log.e(TAG,"unregisterReceiver----------");
		super.onDestroy();
	}
}
