package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhongtianli.nebula.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhu on 2016/11/1.
 */
public class Xlist_success_getDeviceAdapter  extends BaseAdapter{
    private Context context;
   List<Map> list = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    public Xlist_success_getDeviceAdapter(Context context, List<Map> list) {
        this.context = context;
        this.list = list;
        layoutInflater= LayoutInflater.from(context);
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
        if(convertView==null) {
            convertView = layoutInflater.inflate(R.layout.xlist_device_success_item,parent, false);
            holder = new ViewHolder();
            holder.img_device = (ImageView) convertView.findViewById(R.id.img_device);//图标
            holder.write_door = (TextView) convertView.findViewById(R.id.write_door);//door_content,deviceName
            holder.baizi_txt = (TextView) convertView.findViewById(R.id.baizi_txt);//类型->deviceType
            holder.map_write = (TextView) convertView.findViewById(R.id.map_write);//deviceMac
            holder.result_txt = (TextView) convertView.findViewById(R.id.result_txt);//结果
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.write_door.setText((CharSequence) list.get(position).get("deviceName"));//deviceName
        holder.map_write.setText((CharSequence) list.get(position).get("deviceMac"));//deviceMac
        if (list.get(position).get("type").toString().equals("1")) {//代表已上传
            holder.result_txt.setText("已上传");
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
        return convertView;
    }

    public void setList(List<Map> list) {
        this.list = list;
    }

    private class ViewHolder{
        TextView map_write;
        ImageView  img_device;
        TextView result_txt;
        TextView write_door;
        TextView baizi_txt;
    }
}
