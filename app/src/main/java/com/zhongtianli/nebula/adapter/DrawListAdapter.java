package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongtianli.nebula.R;

import java.util.List;

/**
 * Created by zhu on 2016/10/21.
 */
public class DrawListAdapter extends BaseAdapter {
    private Context context;
    private List<Object> objectLis;
    private  String[] mPlanetTitles;
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

    public DrawListAdapter(Context context, List<Object> objectLis, String[] mPlanetTitles) {
        this.context = context;
        this.objectLis = objectLis;
        this.mPlanetTitles = mPlanetTitles;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objectLis.size();
    }

    @Override
    public Object getItem(int position) {
        return objectLis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
        //观察convertView随ListView滚动情况
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawlayout_list,null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.txt_li = (TextView) convertView.findViewById(R.id.txt_show);
            holder.img_show = (ImageView) convertView.findViewById(R.id.img_list);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        else{
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
        String stringItem = mPlanetTitles[position];
        holder.txt_li.setText(stringItem);
        holder.img_show.setImageResource((Integer) objectLis.get(position));
        return convertView;
    }

    /**存放控件*/
    public final class ViewHolder{
        public ImageView img_show;
        public TextView txt_li;
    }
}


