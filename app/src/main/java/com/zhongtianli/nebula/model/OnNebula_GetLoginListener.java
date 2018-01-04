package com.zhongtianli.nebula.model;

import com.zhongtianli.nebula.bean.GetDevice;
import com.zhongtianli.nebula.bean.LoginProjects;

import java.util.ArrayList;

/**
 * Created by zhu on 2016/11/30.
 */
public interface OnNebula_GetLoginListener {
    void getLoginInfoSuccess(ArrayList<LoginProjects> list, boolean isFirst);
    void getLoginInfoFailed(boolean isFirst);
}
