package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/11/1.
 */
public class Nebula_GetDevice_ManagerAdapter extends  RecyclerView.Adapter<Nebula_GetDevice_ManagerAdapter.ViewHolder>
        {

            private List<String> projectNames = new ArrayList<>();
            private Context context;
            private MyItemClickListener mItemClickListener;
            private String TA = "robin debug";

            public Nebula_GetDevice_ManagerAdapter(Context context, List<String> projectNames) {
                this.projectNames = projectNames;
                this.context = context;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // 给ViewHolder设置布局文件
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_device_card_view, parent, false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position) {
                // 给ViewHolder设置元素
                holder.mTextView.setText(projectNames.get(position));
                holder.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(projectNames.get(position), position);
                        Log.e(TA, "mItemClickListener.onItemClickL-----projectNames.get(position):" + projectNames.get(position) + "position:" + position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                // 返回数据总数
                return projectNames == null ? 0 : projectNames.size();
            }

            /**
             * 设置Item点击监听
             *
             * @param listener
             */
            public void setOnItemClickListener(MyItemClickListener listener) {
                this.mItemClickListener = listener;
            }


            public interface MyItemClickListener {
                //        public void onItemClick(View view,int postion);
                void onItemClick(String s, int position);
            }

            // 重写的自定义ViewHolder
            public static class ViewHolder
                    extends RecyclerView.ViewHolder {
                public TextView mTextView;

                public ViewHolder(View v) {
                    super(v);
                    mTextView = (TextView) v.findViewById(R.id.name);
                }
            }
        }

