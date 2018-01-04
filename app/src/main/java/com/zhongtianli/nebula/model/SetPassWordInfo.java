package com.zhongtianli.nebula.model;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import static com.zhongtianli.nebula.activity.LoginActivity.JSON;

/**
 * Created by zhu on 2016/11/16.
 */
public class SetPassWordInfo implements ISetPassword {
    private String TAG1 = "zhu";
    private  Context context;
    //修该新密码
    @Override
    public void nebula_SetPassword(Context context, final Map map, final OnNebula_SetPasswordListener listener) {
            //在这里去提交设置新密码的动作
        this.context = context;
        OkHttpUtils.postString()//新密码提交
                .url("http://nebula.inebula.cn:22711/api/Nebula/Nebula_SetPassword")
                .mediaType(JSON)
                .content(new Gson().toJson(map))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG1,e.getMessage());
                        listener.setPasswordInfoFailed();
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
                            listener.setPasswordInfoFailed();
                        } else if (Result.equals("2")) {
                            toast("json格式解析失败");
                            listener.setPasswordInfoFailed();
                        } else if (Result.equals("100")) {
                            toast("成功");
                            Log.e(TAG1,"response:"+response.toString());
                            listener.setPasswordInfoSuccess(Result);
                        } else if (Result.equals("101")) {
                            listener.setPasswordInfoFailed();
                            toast("失败，手机号不正确");
                        } else if (Result.equals("102")) {
                            listener.setPasswordInfoFailed();
                            toast("未知错误");
                        }
                    }
                });
    }

    private  void toast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
