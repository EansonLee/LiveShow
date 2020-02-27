package com.maywide.liveshow.net.retrofit;


import android.content.Context;
import android.text.TextUtils;


import com.maywide.liveshow.net.retrofit.cookie.CookieJarImpl;
import com.maywide.liveshow.net.retrofit.cookie.store.PersistentCookieStore;
import com.maywide.liveshow.net.retrofit.interceptor.HeaderAddInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 10;
    private static volatile RetrofitClient singleton;
    private static volatile RetrofitClient singletonWithUrl;
    private static Context mContext;
    private OkHttpClient client;
    private Retrofit retrofit;
    private static final long MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private PersistentCookieStore cookieStore;

    private RetrofitClient() {

        if (mContext == null) {
            throw new RuntimeException("RetrofitClient must call init() first");
        }
        cookieStore = new PersistentCookieStore(mContext);
        client = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HeaderAddInterceptor(addHeaders()))
                //.cookieJar(new CookieJarImpl(new MemoryCookieStore()))
                .cookieJar(new CookieJarImpl(cookieStore))
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(mContext.getCacheDir(), MAX_SIZE))
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    private RetrofitClient(String url) {

        if (mContext == null) {
            throw new RuntimeException("RetrofitClient must call init() first");
        }
        cookieStore = new PersistentCookieStore(mContext);
        client = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new HeaderAddInterceptor(addHeaders()))
                //.cookieJar(new CookieJarImpl(new MemoryCookieStore()))
                .cookieJar(new CookieJarImpl(cookieStore))
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(mContext.getCacheDir(), MAX_SIZE))
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();

        String baseUrl = url;
        if (TextUtils.isEmpty(url)){
            baseUrl = Config.BASE_URL;
        }
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    public static void init(Context context) {
        if (mContext == null) {

            mContext = context;
        }
    }

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * 添加请求头
     *
     * @return
     */
    private Map<String, String> addHeaders() {
        Map<String, String> headers = new HashMap<>();
        //headers.put("User-Agent","Android/TV");
        return headers;
    }

    public static RetrofitClient getInstance() {
        if (singleton == null) {
            synchronized (RetrofitClient.class) {
                if (singleton == null) {
                    singleton = new RetrofitClient();
                }
            }
        }
        return singleton;
    }

    //含参数单例
    public static RetrofitClient getInstance(String url) {
        if (singletonWithUrl == null) {
            synchronized (RetrofitClient.class) {
                if (singletonWithUrl == null) {
                    singletonWithUrl = new RetrofitClient(url);
                }
            }
        }
        return singletonWithUrl;
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }


    public <T> T api(Class<T> clz) {

        return retrofit.create(clz);
    }
}