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
import android.widget.Toast;

import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.interfaces.AdapterRefreshList;
import com.zhongtianli.nebula.interfaces.IShowAdapterList;
import com.zhongtianli.nebula.view.ScrollViewCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhu on 2016/11/8.
 */
public class Xlist_View_getDevice_scan_Adapter extends BaseAdapter implements
        IShowAdapterList {

    private  LayoutInflater layoutInflater;
    private Context context;
    private List<ConcurrentHashMap> list = new ArrayList<>();
    private  AckpassClass mAckpassClass;
    private String TAG = "robin debug";
    private String TAG1 = "zhu";
    private String TAG2 = "peng";
    private   AdapterRefreshList adapterRefreshList;
    private Map swipeCodeMap;
    private  LoadingDialogToDeviceAct loadingDialogToDeviceAct;

    public Xlist_View_getDevice_scan_Adapter(Context context, List<ConcurrentHashMap> list, AckpassClass mAckpassClass,
                                             AdapterRefreshList adapterRefreshList,
                                             LoadingDialogToDeviceAct loadingDialogToDeviceAct) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.mAckpassClass = mAckpassClass;
        this.adapterRefreshList = adapterRefreshList;
        this.loadingDialogToDeviceAct = loadingDialogToDeviceAct;
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
                    convertView = layoutInflater.inflate(R.layout.device_item_door, parent, false);
                    holder1 = new ViewHolder1();
                    holder1.txt_sh = (TextView) convertView.findViewById(R.id.txt_sh);//设备名称
                    holder1.scroll_out = (TextView) convertView.findViewById(R.id.scroll_out);//按钮名称
                    holder1.img_dot = (ImageView) convertView.findViewById(R.id.img_dot);
                    holder1.offswitch_id = (ImageView) convertView.findViewById(R.id.offswitch_id);//按钮-开门
                    holder1.mac_write = (TextView) convertView.findViewById(R.id.mac_write);//mac 名称
                    convertView.setTag(R.id.tag_first, holder1);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.device_item_test, parent, false);
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
                holder1.offswitch_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//点击事件-电梯
                        final String paramId = "11111111";//为4字节卡号，用16进制字符串表示数据
                        final String paramFloor;//打开楼层
                        final String paramInout;//打开进或出
                        String deviceType = (String) list.get(position).get("deviceType");
                         String deviceMac = (String) list.get(position).get("deviceMac");
                        deviceMac = deviceMac.toUpperCase();//转换成大写，方便控制
                        Log.e(TAG1, "onClick: "+position);
                        if (deviceType.equals("06")) {         //电梯
                            paramFloor = "000A";                //选择10楼
                            paramInout = "0010";
                        } else if (deviceType.equals("02")) {         //摆闸-进
                            paramFloor = "0000";
                            paramInout = "0001";
                        } else if (deviceType.equals("03")) {   //车库 -进
                            paramFloor = "0000";
                            paramInout = "0001";
                        } else {
                            paramFloor = "0000";//
                            paramInout = "0010";
                        }
                        try {
                            Log.e(TAG2, deviceMac + "," + paramId + "," + paramFloor + "," + paramInout + ",->开");
                            final String finalDeviceMac = deviceMac;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
//                            Log.e(TAG2,""+deviceMac+","+paramId+","+paramFloor+",-开111"+ paramInout);
//                            Toast.makeText(context,""+deviceMac+","+paramId+","+paramFloor+",-开"+ paramInout,Toast.LENGTH_SHORT).show();
                                    final String debugLog = finalDeviceMac+","+paramId+","+paramFloor+","+paramInout;//调试信息
                                    if (mAckpassClass != null) {//
                                        try {
                                            Log.e(TAG2, finalDeviceMac + "," + paramId + "," + paramFloor + "," + paramInout + ",->开");
                                            mAckpassClass.OpenDevice(finalDeviceMac, paramId, paramFloor, paramInout);
                                        }catch (Exception ex){
                                            loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                        }
                                        loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                    } else {
                                        adapterRefreshList.refreshList(Xlist_View_getDevice_scan_Adapter.this);
                                        new Handler().postDelayed(new Runnable() {//延时100毫秒方便，重新拿到mAckpassClass对象
                                            @Override
                                            public void run() {
                                                try {
                                                    mAckpassClass.OpenDevice(finalDeviceMac, paramId, paramFloor, paramInout);
                                                }catch (Exception ex){
                                                    loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                                }
                                                loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                            }
                                        }, 200);
                                    }
                                    Looper.loop();
                                }
                            }
                            ).start();
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
                final String paramId = "11111111";//为4字节卡号，用16进制字符串表示数据
                final String[] paramFloor = new String[1];//打开楼层
                final String[] paramInout = new String[1];//打开进或出
                final String deviceType = (String) list.get(position).get("deviceType");
                String deviceMac = (String) list.get(position).get("deviceMac");
               final String deviceMac1 = deviceMac.toUpperCase();
                //左右移动后，点击开或关按钮
                holder2.frame_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//打开蓝牙设备
                        finalHolder.img_dot.setImageResource(R.drawable.bot_dark);//打开后圆点变灰色
                        finalHolder.txt_sh.setTextColor(context.getResources().getColor(R.color.selected_un));

                        if (deviceType.equals("06")) {         //电梯
                            paramFloor[0] = "000A";                //选择10楼
                            paramInout[0] = "0010";
                        } else if (deviceType.equals("02")) {         //摆闸-进
                            paramFloor[0] = "0000";
                            paramInout[0] = "0001";
                        } else if (deviceType.equals("03")) {   //车库 -进
                            paramFloor[0] = "0000";
                            paramInout[0] = "0001";
                        } else {
                            paramFloor[0] = "0000";//
                            paramInout[0] = "0010";
                        }
                        try {
                            Log.e(TAG2, deviceMac1 + "," + paramId + "," + paramFloor[0] + "," + paramInout[0] + ",->开");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
//                            Log.e(TAG2,""+deviceMac+","+paramId+","+paramFloor+",-开111"+ paramInout);
//                            Toast.makeText(context,""+deviceMac+","+paramId+","+paramFloor+",-开"+ paramInout,Toast.LENGTH_SHORT).show();
                                    final String debugLog = deviceMac1+","+paramId+","+paramFloor[0]+","+paramInout[0];//调试信息
                                    if (mAckpassClass != null) {//
                                        try {
                                            mAckpassClass.OpenDevice(deviceMac1, paramId, paramFloor[0], paramInout[0]);
                                        }catch (Exception ex) {
                                            loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                        }
                                        loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                    } else {
                                        adapterRefreshList.refreshList(Xlist_View_getDevice_scan_Adapter.this);
                                        new Handler().postDelayed(new Runnable() {//延时100毫秒方便，重新拿到mAckpassClass对象
                                            @Override
                                            public void run() {
                                                try {
                                                    mAckpassClass.OpenDevice(deviceMac1, paramId, paramFloor[0], paramInout[0]);
                                                }catch (Exception ex) {
                                                    loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                                }
                                                loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                            }
                                        }, 200);
                                    }
                                    Looper.loop();
                                }
                            }
                            ).start();
                        } catch (Exception ex) {
                            throw ex;
                        }
                    }
                });
                holder2.frame_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//关
                        Log.e(TAG1, "关闭设备");
                        finalHolder.img_dot.setImageResource(R.drawable.bot);//关闭后变成原点变亮色
                        finalHolder.txt_sh.setTextColor(context.getResources().getColor(R.color.selected_txt));//文本颜色变化
                        if (deviceType.equals("06")) {         //电梯
                            paramFloor[0] = "000A";                //选择10楼
                            paramInout[0] = "0010";
                        } else if (deviceType.equals("02")) {         //摆闸-出
                            paramFloor[0] = "0000";
                            paramInout[0] = "0000";
                        } else if (deviceType.equals("03")) {   //车库 -出
                            paramFloor[0] = "0000";
                            paramInout[0] = "0000";
                        } else {
                            paramFloor[0] = "0000";
                            paramInout[0] = "0010";
                        }
                        try {
                            Log.e(TAG2, deviceMac1 + "," + paramId + "," + paramFloor[0] + "," + paramInout[0] + ",关->");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
//                            Toast.makeText(context,""+deviceMac+","+paramId+","+paramFloor+",-关"+ paramInout,Toast.LENGTH_SHORT).show();
                                    final String debugLog = deviceMac1+","+paramId+","+paramFloor[0]+","+paramInout[0];//调试信息
                                    if (mAckpassClass != null) {//
                                        try {
                                            mAckpassClass.OpenDevice(deviceMac1, paramId, paramFloor[0], paramInout[0]);
                                        }catch (Exception ex) {
                                            loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                        }
                                        loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                    } else {
                                        adapterRefreshList.refreshList(Xlist_View_getDevice_scan_Adapter.this);
                                        new Handler().postDelayed(new Runnable() {//延时100毫秒方便，重新拿到mAckpassClass对象
                                            @Override
                                            public void run() {
                                                try {
                                                    mAckpassClass.OpenDevice(deviceMac1, paramId, paramFloor[0], paramInout[0]);
                                                }catch (Exception ex) {
                                                    loadingDialogToDeviceAct.loadingDialogToDevAct(ex.toString());//发送打开蓝牙设备时的异常信息
                                                }
                                                loadingDialogToDeviceAct.loadingDialogToDevAct(debugLog);//打开加载Dialog
                                            }
                                        }, 200);
                                    }
                                    Looper.loop();
                                }
                            }
                            ).start();
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

    public void setList(List<ConcurrentHashMap> list) {
        Log.e(TAG,"setList:"+ list);
        this.list = list;
    }

    @Override
    public void toDeviceAct(AckpassClass ackpassClass) {//重新拿到AckpassClass对象
        this.mAckpassClass = ackpassClass;
        Log.e(TAG1,"toDeviceAct-》ackpassClass"+ackpassClass);
    }

    public void setSwipeCodeMap(ConcurrentHashMap swipeCodeMap) {//在这里处理这个swipeCodeMap对象需要几步:
        this.swipeCodeMap = swipeCodeMap;
        //首先拿到此map对象中的MAC
        //然后遍历原来list<map>中有此MAC地址的map没有
       for (ConcurrentHashMap map : list) {
           if(map.get("deviceMac").toString().contains(swipeCodeMap.get("deviceMac").toString())){//原来列表已经有此MAC地址
               Toast.makeText(context,"此MAC地址已经存在",Toast.LENGTH_SHORT).show();
           } else {//没有就添加进去
               list.add(swipeCodeMap);
               break;//跳出for循环
           }
       }
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
    public interface  LoadingDialogToDeviceAct{
        void loadingDialogToDevAct(String debugLog);
    }
}
