package com.zhongtianli.nebula.model;
import android.content.Context;
import com.google.gson.Gson;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;
import static com.zhongtianli.nebula.activity.LoginActivity.JSON;

/**
 * Created by robin on 2016/5/25.
 */
public class SetDeviceInfo implements ISetDevice {

    @Override
    public void nebula_SetDevice(Context context, SetDeviceBean setDeviceBean, final OnNebula_SetDeviceListener listener) {
            //上传设备名称修改后的设备信息
        //加载设备信息

        OkHttpUtils
                .postString()
                .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_SetDevice")
                .tag(context)
                .mediaType(JSON)
                .content(new Gson().toJson(setDeviceBean))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.setDeviceInfoFailed();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                          listener.setDeviceInfoSuccess(response);
                    }
                });
    }
}


