package com.zhongtianli.nebula.model;
import android.content.Context;
import com.zhongtianli.nebula.bean.SetDeviceBean;

/**
 * Created by robin on 2016/5/25.
 */
public interface ISetDevice {
    void nebula_SetDevice(Context context, SetDeviceBean map, OnNebula_SetDeviceListener listener);
}
