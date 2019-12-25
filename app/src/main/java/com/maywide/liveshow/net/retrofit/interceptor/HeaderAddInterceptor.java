package com.maywide.liveshow.net.retrofit.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Detail　添加公共请求头
 */
public class HeaderAddInterceptor implements Interceptor {

    private Map<String, String> headers = new HashMap<>();

    public HeaderAddInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        for (String key : headers.keySet()) {
            //设置header的时候，我们有两种方法可选择:使用addHeader()不会覆盖之前设置的header,若使用header()则会覆盖之前的header
            builder.addHeader(key, headers.get(key));
        }
        Request request = builder.build();
        return chain.proceed(request);
        //拦截请求，直接返回chain.proceed(request),拦截响应，Response response=chain.proceef(request),在写 操作相关代码
    }
}
