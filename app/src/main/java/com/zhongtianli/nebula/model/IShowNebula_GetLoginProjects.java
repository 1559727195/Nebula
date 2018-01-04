package com.zhongtianli.nebula.model;

import com.zhongtianli.nebula.bean.GetDevice;
import com.zhongtianli.nebula.bean.LoginProjects;

import java.util.ArrayList;

/**
 * Created by zhu on 2016/11/30.
 */
public interface IShowNebula_GetLoginProjects {
    void showNebula_GetLoginProjects(ArrayList<LoginProjects> list, boolean isFirst);
    void showNebula_LoginProjectsError(boolean isFirst);
}
