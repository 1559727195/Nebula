package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.activity.MessageDetailActivity;

import java.util.ArrayList;

/**
 * Created by zhu on 2016/11/2.
 */
public class Xlist_getMessageAdapter extends BaseAdapter{
    private Context context;
    ArrayList<String> list = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private String TAG="robin debug";

    public Xlist_getMessageAdapter(Context context, ArrayList<String> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.xlist_device_message_item, parent, false);
            holder.mes_back = (ImageView)convertView.findViewById(R.id.mes_back);
            if (position == 0 || ( position % 4 == 0)) {
                holder.mes_back.setBackgroundResource(R.color.right_green);//设置背景时在这里
            } else if (position == 1 || ( (position - 1) % 4 == 0)) {
                holder.mes_back.setBackgroundResource(R.color.right_red);//设置背景时在这里
            } else if (position == 2 || ( (position - 2) % 4 == 0)) {
                holder.mes_back.setBackgroundResource(R.color.right_oriange);//设置背景时在这里
            } else if (position == 3 || ( (position - 3) % 4 == 0)) {
                holder.mes_back.setBackgroundResource(R.color.right_zong);//设置背景时在这里
            }
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder)convertView.getTag();//设置文本时在这里

//            holder.tv.setText(datas.get(position).get("name").toString());
//            holder.tv2.setText(datas.get(position).get("sex").toString());
//            holder.tv3.setText(datas.get(position).get("age").toString());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,MessageDetailActivity.class));
            }
        });


        return convertView;
    }

    private class ViewHolder{
        ImageView mes_back;
        TextView map_write;
    }
}
