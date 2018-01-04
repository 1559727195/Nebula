package com.zhongtianli.nebula.netutil;
import com.squareup.okhttp.OkHttpClient;
import com.zhongtianli.nebula.utils.UrlData;
import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by admin on 2016/1/9.
 */
public class MyRetrofit {
    static final int CONNECT_TIMEOUT_MILLIS = 60* 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 60 * 1000; // 20s
    private TestApi testApi;
    /**
     * 用于Stethoscope调试的ttpClient
     */
    public static OkHttpClient getOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return client;
    }

    public  MyRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://121.41.1.0:8320/dahua/")
//                .baseUrl("http://fxapp.dahuatech.com:8041/")
                .baseUrl(UrlData.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkClient())
                .build();
        testApi = retrofit.create(TestApi.class);
    }

    public TestApi getGirlApi() {
        return testApi;
    }
}

