package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongtianli.nebula.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {



    private Context mContext;


    public RecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.title.setText(dataList.get(position).getTitle());
//        holder.imgView.setTag(dataList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
//            imgView=(ImageView)itemView.findViewById(R.id.imgView);
            title=(TextView)itemView.findViewById(R.id.title);

        }
    }

}