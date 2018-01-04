package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.bean.Devices;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.interfaces.AdapterBackToAct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhu on 2016/11/8.
 */
public class Xlist_View_getLan_scan_Adapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<ConcurrentHashMap> list = new ArrayList<>();
    private String TAG = "robin debug";
    private Handler handler = new Handler();
    private String TAG1 = "zhu";
    private String TAG2 = "peng";
    private boolean isLoadMore;
    private Map user;//项目下用户名和该项目下的ProjectCode项目编码
    private String userName;//用户名
    private  String  projectCode;//该项目下的项目编码
    private AdapterBackToAct adapterBackToAct;//back键回来后refresh主界面蓝牙数据
    public Xlist_View_getLan_scan_Adapter(Context context, List<ConcurrentHashMap> list,
                                          Map user, AdapterBackToAct adapterBackToAct) {
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
        this.list = list;
        this.user = user;
        if (user != null) {
            userName = (String) user.get("UserName");//用户名
            projectCode = (String) user.get("ProjectCode");//项目编码
        }
        this.adapterBackToAct = adapterBackToAct;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //当listView第一个Item的HorizontalSrollview被滑动到左边，当下拉刷新时，那个位置被其他list占到这个索引position，只是文字变了，
    //所以为了防止其他list的子项也占用该位置，每次刷新后，就全部归为，以便让新的list来占用位置
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.xlist_device_lan_item,parent,false);
            holder = new ViewHolder();
            holder.btn_arrow = (ImageView) convertView.findViewById(R.id.btn_arrow);
            holder.img_device = (ImageView)convertView.findViewById(R.id.img_device);
            holder.map_write = (TextView)convertView.findViewById(R.id.map_write);
            holder.write_door = (TextView)convertView.findViewById(R.id.write_door);
            holder.baizi_txt = (TextView)convertView.findViewById(R.id.baizi_txt);
            holder.xlist_back = (LinearLayout) convertView.findViewById(R.id.xlist_back);
            //在这里添加item的点击事件
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.xlist_back.setBackgroundResource(R.drawable.bg1);
        } else {
            holder.xlist_back.setBackgroundResource(R.drawable.bg2);
        }

        switch ((String) list.get(position).get("deviceType")) {
            case "01"://门禁
                holder.img_device.setImageResource(R.drawable.door);
                holder.baizi_txt.setText("门禁");
                break;
            case "02"://摆闸
                holder.img_device.setImageResource(R.drawable.baizha);
                holder.baizi_txt.setText("摆闸");
                break;
            case "03"://车库
                holder.img_device.setImageResource(R.drawable.park);
                holder.baizi_txt.setText("车库");
                break;
            case "06"://电梯
                holder.img_device.setImageResource(R.drawable.lev);
                holder.baizi_txt.setText("电梯");
                break;
        }
        holder.map_write.setText((String) list.get(position).get("deviceMac"));//MAC地址设置
        holder.write_door.setText((String) list.get(position).get("deviceName"));//设备名称
        //为每一行添加点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击进入设备详细信息activity
                Log.e(TAG,"position:"+position);
                SetDeviceBean setDeviceBean = new SetDeviceBean();
                setDeviceBean.setUserName(userName);//设置用户名
                setDeviceBean.setProjectCode(projectCode);//设置项目编码
                //设置设备的Devices
                Devices Device = new Devices();
                Device.setDeviceMac((String) list.get(position).get("deviceMac"));
                Device.setDeviceName((String) list.get(position).get("deviceName"));
                Device.setDeviceType((String) list.get(position).get("deviceType"));
                Device.setDeviceStatus("1");//设备状态的话，这里随便写，在线->1
                setDeviceBean.getDevices().add(Device);
                adapterBackToAct.adapterBackToAct(setDeviceBean);//把他传到MainActivity
            }
        });
        return convertView;
    }

    public void setList(List<ConcurrentHashMap> list, Map user) {
        Log.e(TAG,"setList:"+ list);
        this.list = list;
        this.user = user;
        if (user != null) {
            userName = (String) user.get("UserName");//用户名
            projectCode = (String) user.get("ProjectCode");//项目编码
        }
    }

    private class ViewHolder{
        LinearLayout xlist_back;
        ImageView  img_device;
        ImageView btn_arrow;
        TextView map_write;
        TextView write_door;
        TextView baizi_txt;
    }
}
