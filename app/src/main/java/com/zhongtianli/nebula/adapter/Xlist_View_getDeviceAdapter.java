package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.DeviceDetailInfoActivity;
import com.zhongtianli.nebula.bean.Devices;
import com.zhongtianli.nebula.bean.GetDevice;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.fragment.HomeSecondFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/10/27.
 */

public class Xlist_View_getDeviceAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private Context context;
    private  GetDevice deviceBeen = new GetDevice();
   private List<GetDevice.Devices> list = new ArrayList<>();
    private  String userName;

    private String TA = "robin debug";
    private String TAG1 = "zhu";

    public Xlist_View_getDeviceAdapter(Context context, GetDevice deviceBeen,String userName) {
        this.context = context;
        this.deviceBeen = deviceBeen;
        list = deviceBeen.getDevices();
        layoutInflater= LayoutInflater.from(context);
        this.userName = userName;
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

    public void setList(GetDevice deviceBeen, String userName) {
        this.userName = userName;
        this.deviceBeen = deviceBeen;
        this.list = this.deviceBeen.getDevices();
        //添加一句话.刷新设备列表
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.xlist_device_item,parent,false);
            holder = new ViewHolder();
            holder.btn_arrow = (ImageView) convertView.findViewById(R.id.btn_arrow);
            holder.img_device = (ImageView)convertView.findViewById(R.id.img_device);
            holder.map_write = (TextView)convertView.findViewById(R.id.map_write);
            holder.write_door = (TextView)convertView.findViewById(R.id.write_door);
            holder.baizi_txt = (TextView)convertView.findViewById(R.id.baizi_txt);
            holder.xlist_back = (LinearLayout) convertView.findViewById(R.id.xlist_back);
            //在这里添加item的点击事件
            holder.btn_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击进入设备详细信息activity
                    Log.e(TA,"position:"+position);
                    SetDeviceBean setDeviceBean = new SetDeviceBean();
                    setDeviceBean.setUserName(userName);//设置用户名
                    setDeviceBean.setProjectCode(deviceBeen.getProjectCode());//设置用户编码
                                 list.get(position).getDeviceType();
                    //设置设备的Devices
                    Devices Device = new Devices();
                    Device.setDeviceMac(list.get(position).getDeviceMac());
                    Device.setDeviceName(list.get(position).getDeviceName());
                    Device.setDeviceType(list.get(position).getDeviceType());
                    Device.setDeviceStatus(list.get(position).getDeviceStatus());
                    setDeviceBean.getDevices().add(Device);
//                    setDeviceBean.setDevices(Devices);//设置设备列表
                    Intent intent = new Intent(context,DeviceDetailInfoActivity.class);
                    intent.putExtra("SetDeviceBean",setDeviceBean);
                    context.startActivity(intent);
                }
            });
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.xlist_back.setBackgroundResource(R.drawable.bg1);
        } else {
            holder.xlist_back.setBackgroundResource(R.drawable.bg2);
        }

         switch (list.get(position).getDeviceType()) {
             case "1"://门禁
                 holder.img_device.setImageResource(R.drawable.door);
                 holder.baizi_txt.setText("门禁");
                 break;
             case "2"://摆闸
                 holder.img_device.setImageResource(R.drawable.baizha);
                 holder.baizi_txt.setText("摆闸");
                 break;
             case "3"://电梯
                 holder.img_device.setImageResource(R.drawable.park);
                 holder.baizi_txt.setText("电梯");
                 break;
         }
        holder.map_write.setText(list.get(position).getDeviceMac());
        holder.write_door.setText(list.get(position).getDeviceName());
        return convertView;
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
