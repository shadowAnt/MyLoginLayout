package com.example.mylayout.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by ShadowAnt on 2017/5/15.
 */

public class String2Json {
    public static JSONObject string2json(String shentiString) {
        String[] split = shentiString.split("\\+");
        int len = split.length;
        String[] timeKey = new String[len];
        String[] jianshenArray = new String[len];
        String[] xueyaArray = new String[len];
        String[] xuetangArray = new String[len];
        String[] xinlvArray = new String[len];
        for (int i = 0; i < len; i++) {
            String[] keyString = split[i].split("\":");
            String key = keyString[0];
            String[] keyString1 = key.split("\\{\"");
            Log.d("jsonObject", keyString1[1]);
            timeKey[i] = keyString1[1];
            try {
                JSONObject jsonObject = new JSONObject(split[i]);
                //TODO get
                JSONObject js = jsonObject.getJSONObject(timeKey[i]);
                jianshenArray[i] = js.getString("jianshen");
                xinlvArray[i] = js.getString("xinlv");
                xueyaArray[i] = js.getString("xueya");
                xuetangArray[i] = js.getString("xuetang");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //TODO 组装
        JSONObject jianshenInner = new JSONObject();
        JSONObject xinlvInner = new JSONObject();
        JSONObject xuetangInner = new JSONObject();
        JSONObject xueyaInner = new JSONObject();
        JSONObject iTime = new JSONObject();
        JSONObject outer = new JSONObject();
        for (int i = 0; i < len; i++) {
            try {
                jianshenInner.put(i + "", jianshenArray[i]);
                xinlvInner.put(i + "", xinlvArray[i]);
                xuetangInner.put(i + "", xuetangArray[i]);
                xueyaInner.put(i + "", xueyaArray[i]);
                iTime.put(i + "", timeKey[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outer.put("jianshen", jianshenInner);
                outer.put("xinlv", xinlvInner);
                outer.put("xuetang", xuetangInner);
                outer.put("xueya", xueyaInner);
                outer.put("time", iTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outer;
    }
}
