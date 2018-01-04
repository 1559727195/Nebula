package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhu on 2016/11/4.
 */
public class Xlist_all_getDeviceAdapter extends BaseAdapter {
    private Context context;
    private  List<Map> list = new ArrayList<>();
    private  String userName;
    private final LayoutInflater layoutInflater;
    private String TA = "robin debug";
    private String TAG1 = "zhu";

    public Xlist_all_getDeviceAdapter(Context context, List<Map> list) {
        this.context = context;
        this.list = list;
        layoutInflater= LayoutInflater.from(context);
    }

    public void setList(List<Map> list, String userName) {
        this.userName = userName;
        this.list = list;
        //添加一句话.刷新设备列表
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.xlist_device_all_item, parent, false);
            holder = new ViewHolder();
            holder.img_device = (ImageView) convertView.findViewById(R.id.img_device);//图标
            holder.write_door = (TextView) convertView.findViewById(R.id.write_door);//door_content,deviceName
            holder.baizi_txt = (TextView) convertView.findViewById(R.id.baizi_txt);//类型->deviceType
            holder.map_write = (TextView) convertView.findViewById(R.id.map_write);//deviceMac
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.write_door.setText((CharSequence) list.get(position).get("deviceName"));//deviceName
        holder.map_write.setText((CharSequence) list.get(position).get("deviceMac"));//deviceMac
        switch ((String) list.get(position).get("deviceType")) {

            case "1"://门禁
                holder.img_device.setImageResource(R.drawable.door);
                holder.baizi_txt.setText("门禁");
                break;
            case "2"://摆闸
                holder.img_device.setImageResource(R.drawable.baizha);
                holder.baizi_txt.setText("摆闸");
                break;
            case "3"://车库
                holder.img_device.setImageResource(R.drawable.park);
                holder.baizi_txt.setText("车库");
                break;
            case "6"://电梯
                holder.img_device.setImageResource(R.drawable.lev);
                holder.baizi_txt.setText("电梯");
                break;
        }
        return convertView;
    }

    private class ViewHolder{
        TextView map_write;//deviceMac
        ImageView img_device;
        TextView write_door;
        TextView baizi_txt;
    }
}
