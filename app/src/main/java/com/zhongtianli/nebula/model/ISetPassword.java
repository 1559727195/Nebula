package com.zhongtianli.nebula.model;

import android.content.Context;

import com.zhongtianli.nebula.bean.SetDeviceBean;

import java.util.Map;

/**
 * Created by robin on 2016/5/25.
 */
public interface ISetPassword {
    void nebula_SetPassword(Context context, Map map, OnNebula_SetPasswordListener listener);
}
