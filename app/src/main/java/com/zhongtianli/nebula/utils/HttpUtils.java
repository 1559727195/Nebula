package com.zhongtianli.nebula.utils;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

	private static final String TAG = "robin debug";

	public static byte[] LoadDataForHttpClient(String dataUrl) {
		Log.i(TAG, "" + dataUrl);
		try {
			Log.i(TAG, "dataUrl:" + dataUrl);
			URL url = new URL(dataUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(13000);

		    httpURLConnection.connect();
		    ByteArrayOutputStream bos=new ByteArrayOutputStream();
		    Log.i(TAG, "ResponseCode:" +httpURLConnection.getResponseCode());
		    if (httpURLConnection.getResponseCode() == 200) {
		        InputStream inputStream = httpURLConnection.getInputStream();
				Log.i(TAG,"available inputstream:"+inputStream.available());
		        byte[] buffer =new byte[4096];
		        int line=0;
		        while((line=inputStream.read(buffer))!=-1){
		            bos.write(buffer, 0, line);
					bos.flush();
		        }

		        return bos.toByteArray();
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		}

		return null;

	}
}
