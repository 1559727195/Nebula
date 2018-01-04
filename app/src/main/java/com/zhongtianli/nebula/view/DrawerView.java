package com.zhongtianli.nebula.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.library.SlidingMenu;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.AboutActivity;
import com.zhongtianli.nebula.activity.DeviceManagerViewPagerActivity;
import com.zhongtianli.nebula.activity.DeviceWorkActivity;
import com.zhongtianli.nebula.activity.LoginActivity;
import com.zhongtianli.nebula.activity.MessageActivity;
import com.zhongtianli.nebula.activity.SettingsActivity;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.interfaces.LoginProjectsFirstListner;
import com.zhongtianli.nebula.interfaces.OnMainLanYaOverListener;
import com.zhongtianli.nebula.main.Main_New_Activity;
import com.zhongtianli.nebula.utils.FinishUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.zhongtianli.nebula.R.id.device_about;
import static com.zhongtianli.nebula.R.id.device_quit;
import static com.zhongtianli.nebula.R.string.login;

/**
 *
 * */
public class DrawerView  implements OnClickListener{
	private final Activity activity;
	SlidingMenu localSlidingMenu;
	private SwitchButton night_mode_btn;
	private TextView night_mode_text;
	private RelativeLayout device_worker;
	private RelativeLayout device_manager;
	private RelativeLayout device_sett;
	private RelativeLayout device_messagea;
	private RelativeLayout device_abouts;
	private RelativeLayout device_quits;
	private String TAG = "zhu";
	private String userName;//用户名
	private  String projectCode;//可选的项目编码
	private  List<LoginProjects> loginProjectses = new ArrayList<>();//由Main_activity传过来的projectCode列表
    private  OnMainLanYaOverListener onMainLanYaOverListener;//监听MainActivity蓝牙扫描结束之前进入DeviceworkActivity事件
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private SharedPreferences spCodeProject;//没网，从sp中拿用户名,给userName，有网就直接从MainActivity中传过来map对象

	public DrawerView(Activity activity, OnMainLanYaOverListener onMainLanYaOverListener) {
		this.activity = activity;
		this.onMainLanYaOverListener = onMainLanYaOverListener;
		sp = activity.getSharedPreferences("userInfo", MODE_PRIVATE);//如果退出就将boolean改为false
	}

	public SlidingMenu initSlidingMenu(Map<String, String> map, List<LoginProjects> loginProjectses) {
		if (loginProjectses != null) {//说明有网
			this.userName = map.get("UserName");
			this.projectCode = map.get("ProjectCode");//可选的项目编码
			this.loginProjectses = loginProjectses;
		} else {
			//没网，从sp中拿用户名,给userName，有网就直接从MainActivity中传过来map对象
			spCodeProject = activity.getSharedPreferences("Nebula_GetDevice", MODE_PRIVATE);
			userName = spCodeProject.getString("UserName","111");
		}
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);
//		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		localSlidingMenu.setFadeDegree(0.35F);//SlidingMen
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//ʹSlidingMen
		localSlidingMenu.setMenu(R.layout.left_drawer_fragment_new);//
		localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {
					}
				});
		localSlidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			
			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
			}
		});
		initView(userName);
		return localSlidingMenu;
	}

	private void initView(String userName) {
      //下面是注册 slideMenu事件响应方法
		device_worker =(RelativeLayout)localSlidingMenu.findViewById(R.id.device_worker);
		device_worker.setOnClickListener(this);
		device_manager =(RelativeLayout)localSlidingMenu.findViewById(R.id.device_manager);
		device_manager.setOnClickListener(this);
		device_sett =(RelativeLayout)localSlidingMenu.findViewById(R.id.device_set);
		device_sett.setOnClickListener(this);
		device_messagea =(RelativeLayout)localSlidingMenu.findViewById(R.id.device_message);
		device_messagea.setOnClickListener(this);
		device_abouts =(RelativeLayout)localSlidingMenu.findViewById(device_about);
		device_abouts.setOnClickListener(this);
		device_quits =(RelativeLayout)localSlidingMenu.findViewById(device_quit);
		device_quits.setOnClickListener(this);

		//user_name-用户名
		TextView user_name = (TextView) localSlidingMenu.findViewById(R.id.user_name);
		user_name.setText(userName);
	}

	@Override
	public void onClick(View v) {//这里是响应slideMenu -item点击进入其他actuivity的入口地方
		switch (v.getId()) {
			case R.id.device_worker:
				if (onMainLanYaOverListener != null) {
					onMainLanYaOverListener.onMainLanYaOver();//监听MainActivity蓝牙扫描结束之前进入DeviceworkActivity事件
				}
				Intent intentDevi = new Intent(activity,DeviceWorkActivity.class);
//				intentDevi.putExtra("ProjectCode",projectCode);//通过写入sp的形式来，不要通过initSlideMenu()
				//如果多次initSlideMenu()将会导致initSlidemenu溢出
				activity.startActivity(intentDevi);
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.device_manager:
//				loginProjectses
				Intent intent = new Intent(activity, DeviceManagerViewPagerActivity.class);
				intent.putExtra("projectCodeList", (Serializable) loginProjectses);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.device_set:
				activity.startActivity(new Intent(activity,SettingsActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.device_message:
				activity.startActivity(new Intent(activity, MessageActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case device_about:
				activity.startActivity(new Intent(activity, AboutActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case device_quit: //退出应用
				editor = sp.edit();
				editor.putBoolean("boolean", false);//保存为false时，退出后在登录就是login界面了
				editor.putString("UserName", "");
				editor.putString("Password", "");//退出时清空用户名和密码sp
				editor.commit();
				FinishUtil.finishActivity();//
				activity.startActivity(new Intent(activity, LoginActivity.class));//调到登录界面
				break;
		}
	}
}
