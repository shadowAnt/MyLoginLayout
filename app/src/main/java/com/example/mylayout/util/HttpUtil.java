package com.example.mylayout.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ShadowAnt on 2017/3/22.
 */

public class HttpUtil {
    public static void sendOKHttpResquest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}