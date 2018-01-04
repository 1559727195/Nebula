package com.zhongtianli.nebula.model;

import android.content.Context;

import java.util.Map;

/**
 * Created by robin on 2016/5/25.
 */
public interface IGetLoginProjects {
    void nebula_LoginProjects(boolean isFirst, Context context, Map<String, String> map, OnNebula_GetLoginListener listener);
}
