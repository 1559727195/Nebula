package com.zhongtianli.nebula.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongtianli.nebula.bean.LoginData;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

import static com.zhongtianli.nebula.activity.LoginActivity.JSON;

/**
 * Created by zhu on 2016/11/30.
 */
public class GetLoginProjects implements IGetLoginProjects {

    @Override
    public void nebula_LoginProjects(final boolean isFirst, Context context, Map<String, String> map, final OnNebula_GetLoginListener listener) {
      String  userName = map.get("UserName").toString();
       String psd = map.get("Password").toString();
        LoginData login = new LoginData(userName, psd);
//        UpUseropen upUseropen = new UpUseropen(token, demac, netTime, "1", "9999");
        Log.e(new Gson().toJson(login) + "这是上传数据", "login1: ");
        OkHttpUtils
                .postString()
                .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_SetLogin")
                .tag(this)
                .mediaType(JSON)
                .content(new Gson().toJson(login))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("yichngchuli" + e, "onError:");
                        listener.getLoginInfoFailed(isFirst);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(response + "这是登陆返回数据", "onResponse: ");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Result = jsonObject.getString("Result");
                            JSONArray Projects = jsonObject.getJSONArray("Projects");
                            ArrayList<LoginProjects> list = new ArrayList<>();
                            for (int i = 0; i < Projects.length(); i++) {
                                LoginProjects loginProjects = new LoginProjects();
                                JSONObject jsonObject_item = Projects.getJSONObject(i);
                                String ProjectCode = jsonObject_item.getString("ProjectCode");
                                String ProjectName = jsonObject_item.getString("ProjectName");
                                String DeviceAdd = jsonObject_item.getString("DeviceAdd");
                                String DeviceEdit = jsonObject_item.getString("DeviceEdit");
                                String DeviceTest = jsonObject_item.getString("DeviceTest");
                                String DeviceDebug = jsonObject_item.getString("DeviceDebug");
                                loginProjects.setProjectCode(ProjectCode);
                                loginProjects.setProjectName(ProjectName);
                                loginProjects.setDeviceAdd(DeviceAdd);
                                loginProjects.setDeviceEdit(DeviceEdit);
                                loginProjects.setDeviceTest(DeviceTest);
                                loginProjects.setDeviceDebug(DeviceDebug);
                                list.add(loginProjects);

                            }
                            if (Result.equals("100")) {
                                listener.getLoginInfoSuccess(list,isFirst);
                            } else{//失败
                                listener.getLoginInfoFailed(isFirst);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
