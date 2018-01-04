package com.zhongtianli.nebula.finddreams.netstate;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.finddreams.network.NetUtils;
import com.zhongtianli.nebula.finddreams.receiver.NetReceiver;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;

/**
 * @Description:网络状态条的显示控制
 * @author http://blog.csdn.net/finddreams
 */
public class MainActivity extends AppCompatActivity {

	private NetReceiver mReceiver;
	private ListView listView;
	private RelativeLayout no_network_rl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initReceive();
		initView();
		EventBus.getDefault().register(this);

	}

	private void initView() {
		no_network_rl = (RelativeLayout) findViewById(R.id.net_view_rl);
	}

	private void initReceive() {
		mReceiver = new NetReceiver();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	public void onEventMainThread(NetEvent event) {
		setNetState(event.isNet());
	}

	public void setNetState(boolean netState) {

		if (no_network_rl != null) {
			no_network_rl.setVisibility(netState ? View.GONE : View.VISIBLE);
			no_network_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					NetUtils.startToSettings(MainActivity.this);
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
