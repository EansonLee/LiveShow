package com.maywide.liveshow.net.retrofit;

public class Config {
    /**
     * 内网地址
     */
    //public static final String BASE_IP = "http://10.205.22.205:9093";

    /**
     * 外网地址
      */
    public static final String BASE_IP = "http://47.103.157.220:8001";

    /**
     * 本地地址
     */
//    public static final String BASE_IP = "http://180.200.0.70:8888";
    public static final String BASE_URL = BASE_IP+"/mini/";
    public static final long TIMEOUT = 1000;
}
