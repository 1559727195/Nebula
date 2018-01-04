package com.zhongtianli.nebula.model;
import com.zhongtianli.nebula.bean.GetDevice;

/**
 * Created by robin on 2016/5/25.
 */
public interface OnNebula_GetDeviceListener {
    void getDeviceInfoSuccess(GetDevice list);
    void getDeviceInfoFailed();
}

