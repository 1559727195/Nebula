package com.zhongtianli.nebula.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.utils.DensityUtil;
import java.util.List;

/**
 * Created by zhu on 2016/10/24.
 */

public class SelectMedicalCasePopwindow extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView tv;
    private View mMenuView;
    private Context context;
    private Handler callback;

    private ListView mMyListView;
    private String[] items;
    private Button mButton;
    private ChatHistoryAdapter mAdapter;

    public SelectMedicalCasePopwindow(Activity con, List<String> itemList) {
        this(con, itemList.toArray(new String[itemList.size()]));
    }

    public SelectMedicalCasePopwindow(Activity con, String[] items) {
        super(con);
        this.context = con;
        this.items = items;
        int scrheight = DensityUtil.getScreenHeight(context)  / 2;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_medical_case_pop, null);
        mAdapter = new ChatHistoryAdapter();
        mMyListView = (ListView) mMenuView.findViewById(R.id.bithday_layout);

        View listItem = mAdapter.getView(0, null, mMyListView);
        listItem.measure(0, 0);
        int relheight = listItem.getMeasuredHeight();
//        UtilsLog.d(scrheight);
//        UtilsLog.d(relheight);


        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if(relheight * items.length > scrheight){
            ViewGroup.LayoutParams layoutParams = mMyListView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = scrheight;
            mMyListView.setLayoutParams(layoutParams);
            //this.setHeight(scrheight);
        } else {
        }
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);

        // 设置popWindow的显示和消失动画
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.bithday_layout_lin).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        mButton = (Button) mMenuView.findViewById(R.id.btn_cancel);
        mButton.setOnClickListener(this);
        mMyListView.setAdapter(mAdapter);
        mMyListView.setOnItemClickListener(this);
        this.update();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        this.tv = (TextView) parent;
    }

    public void showAtLocationNoSetText(View parent, int gravity, int x, int y, Handler callback) {
        super.showAtLocation(parent, gravity, x, y);
        this.callback = callback;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                this.dismiss();
                if (callback != null) {
                    callback.sendEmptyMessage(0);
                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (callback != null) {
            Message msg = callback.obtainMessage(1, position, 0);
            callback.sendMessage(msg);
        }
        dismiss();
    }

    class ChatHistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatHistoryHolder holder;
            if (convertView == null) {
                holder = new ChatHistoryHolder();
                convertView = View.inflate(context, R.layout.view_chat_select_case_item, null);
                holder.clnic_doctor_item_time_tv = (TextView) convertView.findViewById(R.id.btn_myquestion_first);
                convertView.setTag(holder);
            } else {
                holder = (ChatHistoryHolder) convertView.getTag();
            }
            holder.clnic_doctor_item_time_tv.setText(items[position]);
            return convertView;
        }

        class ChatHistoryHolder {
            TextView clnic_doctor_item_time_tv;

        }

    }
}