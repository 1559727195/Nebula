package com.zhongtianli.nebula.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

import static cn.ciaapp.sdk.CIAService.context;
import static com.zhongtianli.nebula.activity.LoginActivity.JSON;

/**
 * Created by zhu on 2016/12/13.
 */
public class SetVisitorInfo implements ISetVisitor{
    private String TAG1 = "zhu";
    private  Context context;
    @Override
    public void nebula_SetVisitor(Context context, Map map, final OnNebula_SetVisitorListener listener) {
        //在这里去提交设置新密码的动作
        Log.e(TAG1,"devicemac:"+map.get("devicemac")+",devicetype:"+map.get("devicetype")
        +",devicevalue:"+map.get("devicevalue"));
        this.context = context;
        OkHttpUtils.postString()//新密码提交
                .url("http://nebula.inebula.cn:22711/api/Nebula/Visitor")
                .mediaType(JSON)
                .content(new Gson().toJson(map))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG1,e.getMessage());
                        listener.setVisitorInfoFailed();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG1,"response:"+response);
//                        "Result":"100"
                        JSONObject jsonObject = null;
                        String Result = "";
                        try {
                            jsonObject = new JSONObject(response);
                            Result = jsonObject.getString("Result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (Result.equals("1")) {
                            toast("参数不正确或者不存在");
                            listener.setVisitorInfoFailed();
                        } else if (Result.equals("2")) {
                            toast("参数数据不正确");
                            listener.setVisitorInfoFailed();
                        } else if (Result.equals("100")) {
                            toast("成功");
                            Log.e(TAG1,"response:"+response.toString());
                            listener.setVisitorInfoSuccess(Result);
                        } else if (Result.equals("101")) {
                            listener.setVisitorInfoFailed();
                            toast("失败");
                        } else if (Result.equals("3")) {
                            listener.setVisitorInfoFailed();
                            toast("设备不存在");
                        } else if (Result.equals("4")) {
                            listener.setVisitorInfoFailed();
                            toast("设备断线");
                        } else if (Result.equals("5")) {
                            listener.setVisitorInfoFailed();
                            toast("设备无响应");
                        }
                    }
                });
    }

    private  void toast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
