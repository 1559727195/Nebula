package com.zhongtianli.nebula.asynck;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DownLoadTask extends AsyncTask<String,Void,byte[]> {
    private  CallerBackListener callerBackListener;
    public DownLoadTask(CallerBackListener callerBackListener) {

        this.callerBackListener=callerBackListener;
    }

    @Override
    protected byte[] doInBackground(String... params) {
       return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        if(callerBackListener!=null){
                callerBackListener.onResponse("");
        }
    }

    public  interface  CallerBackListener{
             void onResponse(String s);
    }
}
