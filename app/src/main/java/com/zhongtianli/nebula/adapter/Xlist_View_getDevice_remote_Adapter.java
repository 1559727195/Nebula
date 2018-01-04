package com.zhongtianli.nebula.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.view.ScrollViewCustom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhu on 2016/11/8.
 */
public class Xlist_View_getDevice_remote_Adapter extends BaseAdapter {

    private  LayoutInflater layoutInflater;
    private Context context;
    private List<Map> list = new ArrayList<>();
    private String TAG = "robin debug";
    private String TAG1 = "zhu";
    private String TAG2 = "peng";
    private Map swipeCodeMap;
    private  RemoteVisitorToDeviceAct remoteVisitorToDeviceAct;//远程控制设备打开-访客列表

    public Xlist_View_getDevice_remote_Adapter(Context context, List<Map> list,
                                               RemoteVisitorToDeviceAct remoteVisitorToDeviceAct) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.remoteVisitorToDeviceAct = remoteVisitorToDeviceAct;
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


    /**
     * 获得itemView的type
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if ((int)list.get(position).get("type") == 0) {
            result = 0;
        } else if ((int)list.get(position).get("type") == 1) {
            result = 1;
        }
        return result;
    }

    /**
     * 获得有多少中view type
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //当listView第一个Item的HorizontalSrollview被滑动到左边，当下拉刷新时，那个位置被其他list占到这个索引position，只是文字变了，
    //所以为了防止其他list的子项也占用该位置，每次刷新后，就全部归为，以便让新的list来占用位置
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //创建两种不同种类的viewHolder变量
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        //根据position获得View的type
        int type = getItemViewType(position);
        if (convertView == null) {
            Log.e(TAG1,type+"------convertView"+"-//根据不同的type 来inflate不同的item layout");
            //实例化
            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key
            switch (type) {
                case 0:
                    convertView = layoutInflater.inflate(R.layout.device_item_door_remote, parent, false);
                    holder1 = new ViewHolder1();
                    holder1.txt_sh = (TextView) convertView.findViewById(R.id.txt_sh);//设备名称
                    holder1.scroll_out = (TextView) convertView.findViewById(R.id.scroll_out);//按钮名称
                    holder1.img_dot = (ImageView) convertView.findViewById(R.id.img_dot);
                    holder1.offswitch_id = (ImageView) convertView.findViewById(R.id.offswitch_id);//按钮-开门
                    holder1.mac_write = (TextView) convertView.findViewById(R.id.mac_write);//mac 名称
                    convertView.setTag(R.id.tag_first, holder1);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.device_item_test_remote, parent, false);
                    holder2 = new ViewHolder2();
                    holder2.scrollView = (ScrollViewCustom) convertView.findViewById(R.id.scrollView_id);
                    holder2.img_dot = (ImageView) convertView.findViewById(R.id.img_dot);
                    holder2.txt_sh = (TextView) convertView.findViewById(R.id.txt_sh);
                    holder2.scroll_in = (TextView) convertView.findViewById(R.id.scroll_in);//进
                    holder2.scroll_out = (TextView) convertView.findViewById(R.id.scroll_out);//出
                    holder2.frame_open = (FrameLayout) convertView.findViewById(R.id.frame_open);//开
                    holder2.frame_close = (FrameLayout) convertView.findViewById(R.id.frame_close);//关
                    holder2.mac_write = (TextView) convertView.findViewById(R.id.mac_write);//deviceMac
                    convertView.setTag(R.id.tag_second, holder2);
                    break;
            }

        } else {
            //根据不同的type来获得tag
            Log.e(TAG1,type+"------type"+"-//根据不同的type来获得tag");
            switch (type) {
                case 0://第一种
                    holder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                break;

                case 1://第二种
                    holder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                    break;
            }
        }

        //根据不同的type设置数据
        switch (type) {
            case  0:
                String deviceType_first = (String) list.get(position).get("deviceType");
                //        //判断设备类型
                switch (deviceType_first) {//这边负责显示
                    case "06"://梯控
                        holder1.scroll_out.setText("10");//测试数据-就先是10楼
                        break;
                    case "01"://门禁
                        holder1.scroll_out.setText("开门");//测试数据-就先是10楼
                        Log.e(TAG1,holder1.scroll_out.getText()+"------");
                        break;
                    case "04"://车位锁
                        holder1.scroll_out.setText("其他");//测试数据-就先是10楼
                        break;
                    case "05"://电子锁
                        holder1.scroll_out.setText("其他");//测试数据-就先是10楼
                        break;
                    case "07"://呼梯
                        holder1.scroll_out.setText("呼梯");//测试数据-就先是10楼
                        break;
                }
                String deviceName_one = (String) list.get(position).get("deviceName");//设备名称
                String deviceMac_one = (String) list.get(position).get("deviceMac");//设备MAC
                holder1.txt_sh.setText(deviceName_one);//设备名称
                holder1.mac_write.setText(deviceMac_one);//设备mac
               final String[] devicevalue_one = new String[1];//打开楼层或门禁
                holder1.offswitch_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击事件-电梯
                        String deviceType = (String) list.get(position).get("deviceType");
                         String deviceMac = (String) list.get(position).get("deviceMac");
                        String deviceType_one = deviceType;
                        Log.e(TAG1, "onClick: "+position);
                        if (deviceType.equals("06")) {         //电梯
                            devicevalue_one[0] = "10";//0~255楼
                            deviceType_one = "6";
                        }  else {//门禁,或其他
                            devicevalue_one[0] = "0"; //门禁或者其他
                            deviceType_one= "1";
                        }
                        try {
                            final Map map =new HashMap();//存储http控制的字段，devicemac,devicetype,devicevalue
                            map.put("devicemac",deviceMac);
                            map.put("devicetype",deviceType_one);
                            map.put("devicevalue",devicevalue_one[0]);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    remoteVisitorToDeviceAct.remoteVisitorToDeviceAct(map);//把远程控制的map传到DeviceRemoteWorkActivity中去
                                    Looper.loop();
                                }
                            }).start();
                        } catch (Exception ex) {
                            throw ex;
                        }
                    }
                });
                break;
            case  1:
                String deviceType_second = (String) list.get(position).get("deviceType");
                Log.e(TAG1,deviceType_second+"------deviceType_second");
                switch (deviceType_second) {//这边负责显示
                    case "02"://摆闸
                        holder2.scroll_in.setText("开");
                        holder2.scroll_out.setText("关");//测试数据-就先是10楼
                        Log.e(TAG1, holder2.scroll_out.getText()+"------ holder2.scroll_out");
                        break;
                    case "03"://车库
                        holder2.scroll_in.setText("进");
                        holder2.scroll_out.setText("出");//测试数据-就先是10楼
                        break;
                }
                    holder2.scrollView.smoothScrollTo((int) -holder2.scrollView.getX(), 0);//恢复到左边
                    Log.e(TAG1, "//我点击它-滑到左边");
                    holder2.scrollView.startScrollerTask();//我监听点击后他的滚动距离-加上这句话，每次刷新就会关闭，所有

                //shezhi蓝牙扫描信息
                String deviceName_two = (String) list.get(position).get("deviceName");
                String deviceMac_two =  (String) list.get(position).get("deviceMac");//deviceMac
                holder2.txt_sh.setText(deviceName_two);//设备名称
                holder2.mac_write.setText(deviceMac_two);//deviceMac
                final ViewHolder2 finalHolder = holder2;
                final String[] devicevalue_two = new String[1];//车库或摆闸
                final String deviceType = (String) list.get(position).get("deviceType");
               final String deviceMac = (String) list.get(position).get("deviceMac");
                //左右移动后，点击开或关按钮
                holder2.frame_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//打开蓝牙设备
                        finalHolder.img_dot.setImageResource(R.drawable.bot_dark);//打开后圆点变灰色
                        finalHolder.txt_sh.setTextColor(context.getResources().getColor(R.color.selected_un));
                        String deviceType_two = deviceType;
                        if (deviceType.equals("02")) {         //摆闸-进
                            devicevalue_two[0] = "1";
                            deviceType_two = "2";
                        } else if (deviceType.equals("03")) {   //车库 -开
                            devicevalue_two[0] = "1";
                            deviceType_two = "3";
                        }
                        try {
                            final Map map =new HashMap();//存储http控制的字段，devicemac,devicetype,devicevalue
                            map.put("devicemac",deviceMac);
                            map.put("devicetype",deviceType_two);
                            map.put("devicevalue",devicevalue_two[0]);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    remoteVisitorToDeviceAct.remoteVisitorToDeviceAct(map);//把远程控制的map传到DeviceRemoteWorkActivity中去
                                    Looper.loop();
                                }
                            }).start();
                        } catch (Exception ex) {
                            throw ex;
                        }
                    }
                });
                holder2.frame_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//关
                        Log.e(TAG1, "关闭设备");
                        String deviceType_two = deviceType;
                        finalHolder.img_dot.setImageResource(R.drawable.bot);//关闭后变成原点变亮色
                        finalHolder.txt_sh.setTextColor(context.getResources().getColor(R.color.selected_txt));//文本颜色变化
                        if (deviceType.equals("02")) {         //摆闸-出
                            devicevalue_two[0] = "2";
                            deviceType_two = "2";
                        } else if (deviceType.equals("03")) {   //车库 -关
                            devicevalue_two[0] = "2";
                            deviceType_two = "3";
                        }
                        try {
                            final Map map =new HashMap();//存储http控制的字段，devicemac,devicetype,devicevalue
                            map.put("devicemac",deviceMac);
                            map.put("devicetype",deviceType_two);
                            map.put("devicevalue",devicevalue_two[0]);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    remoteVisitorToDeviceAct.remoteVisitorToDeviceAct(map);//把远程控制的map传到DeviceRemoteWorkActivity中去
                                    Looper.loop();
                                }
                            }).start();
                        } catch (Exception ex) {
                            throw ex;
                        }
                    }
                });

                holder2.scrollView.setOnTouchListener(new View.OnTouchListener()//监听水平左右滑屏事件
                {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (finalHolder.scrollView.getScrollX() == 0) {//  holder.scrollView.getScrollX();//如果== 0就是滑到左边了
                                finalHolder.scrollView.smoothScrollTo((int) v.getX(), 0);//-滑到右边
                            } else {
                                finalHolder.scrollView.smoothScrollTo((int) -v.getX(), 0);//-滑到左边
                            }
                            finalHolder.scrollView.startScrollerTask();//我监听点击后他的滚动距离
                        }
                        return false;
                    }
                });

                holder2.scrollView.setOnScrollStopListner(new ScrollViewCustom.OnScrollStopListner() {
                    public void onScrollToRightEdge() {//打开设备
                    }

                    public void onScrollToMiddle() {
                    }

                    public void onScrollToLeftEdge() {//关闭设备
                    }

                    public void onScrollStoped() {
                    }
                });
                break;
        }

        return convertView;
    }

    private class ViewHolder1 {
        ImageView img_dot;
        ImageView offswitch_id;
        TextView txt_sh;
         TextView scroll_out;//
        TextView mac_write;//mac
    }

    private class ViewHolder2 {
        ScrollViewCustom  scrollView;
        ImageView img_dot;
        TextView txt_sh;
         TextView scroll_in;//进
        TextView scroll_out;
        FrameLayout frame_open;
        FrameLayout frame_close;
        TextView mac_write;//deviceMac
    }

    //在这里搞一个点击蓝牙开关-加载的Dialog弹出框
    public interface RemoteVisitorToDeviceAct{
        void  remoteVisitorToDeviceAct(Map map);
    }
}
