package com.zhongtianli.nebula.model;

import android.content.Context;

import java.util.Map;

/**
 * Created by zhu on 2016/12/13.
 */
public interface ISetVisitor {
    void nebula_SetVisitor(Context context, Map map, OnNebula_SetVisitorListener listener);
}
