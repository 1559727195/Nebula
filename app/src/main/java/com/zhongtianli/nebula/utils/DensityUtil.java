package com.zhongtianli.nebula.utils;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by zhu on 2016/10/24.
 */

public class DensityUtil {

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        // 设置DensityDpi
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        return  screenHeight;
    }
}
