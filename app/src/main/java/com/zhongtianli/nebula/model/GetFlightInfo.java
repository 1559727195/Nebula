package com.zhongtianli.nebula.model;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.massky.ywx.ackpasslibrary.AckpassClass;
import com.zhongtianli.nebula.base.GetDeviceBean;
import com.zhongtianli.nebula.bean.GetDevice;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import okhttp3.Call;

import static com.zhongtianli.nebula.activity.LoginActivity.JSON;


/**
 * Created by robin on 2016/5/25.
 */
public class GetFlightInfo implements IGetFlight{


    private String TAG="robin debug";
    private ArrayList<GetDeviceBean> list;
    private GetDevice getDeviceBean = new GetDevice();

    @Override
    public void getFlightInfo(String start, String last, OnFlightInfoListener listener) {
        listener.getFlightInfoSuccess("successfully !");
    }

    @Override//去加载设备信息
    public void nebula_GetDevice(Context context, Map<String, String> map,final OnNebula_GetDeviceListener listener) {
        //加载设备信息
         JSONObject json = new JSONObject();
        list = new ArrayList<>();//初始化设备信息容器
         try{
             if (map != null) {
                 json.put("UserName", map.get("UserName"));
                 json.put("ProjectCode", map.get("ProjectCode"));
             }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.e(TAG,json.toString());

        OkHttpUtils
                .postString()
                .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_GetDevice")
                .tag(context)
                .mediaType(JSON)
                .content(json.toString())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.getDeviceInfoFailed();//加载设备信息失败
                    }

                    @Override
                    public void onResponse(String response, int id) {
                       Log.e(TAG,"response:"+response);//加载设备信息成功
                        getDeviceBean=new Gson().fromJson(response,GetDevice.class);
                            if(getDeviceBean.getResult().equals("100")) {
                                listener.getDeviceInfoSuccess(getDeviceBean);
                            }else if (getDeviceBean.getResult().equals("101")){
                                Log.e(TAG,"失败，项目编号不正确");
                                listener.getDeviceInfoFailed();//加载设备信息失败
                            } else if (getDeviceBean.getResult().equals("102")) {
                                Log.e(TAG,"未知错误");
                                listener.getDeviceInfoFailed();//加载设备信息失败
                            }
                        }
                });
    }
}


