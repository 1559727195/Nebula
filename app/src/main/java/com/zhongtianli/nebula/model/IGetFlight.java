package com.zhongtianli.nebula.model;

import android.content.Context;

import com.massky.ywx.ackpasslibrary.AckpassClass;

import java.util.Map;

/**
 * Created by robin on 2016/5/25.
 */
public interface IGetFlight {
    void getFlightInfo(String start, String last, OnFlightInfoListener listener);
    void nebula_GetDevice(Context context, Map<String, String> map, OnNebula_GetDeviceListener listener);
}
