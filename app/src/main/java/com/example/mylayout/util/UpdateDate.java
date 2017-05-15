package com.example.mylayout.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ShadowAnt on 2017/4/11.
 */

public class UpdateDate {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    DecimalFormat df = new DecimalFormat("0.00");

    public UpdateDate(final Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }

    public String initJianShen() {
        double length;
        Random random = new Random();
        length = random.nextDouble() * 10;
//        return "  您今天大约走了" + df.format(length) + "km";
        return df.format(length) + "";
    }

    public String initXinLv() {
        int xinlv;
        Random random = new Random();
        xinlv = random.nextInt(70) + 50;
//        return "  您的平均心率大约为：" + xinlv + "次/分";
        return xinlv + "";
    }

    public String initXueTang() {
        double xuetang;
        Random random = new Random();
        xuetang = random.nextDouble() * 10;
//        return "  您的平均血糖值大约为： " + df.format(xuetang) + "mmol/L";
        return df.format(xuetang) + "";
    }

    public String initXueYa() {
        int shou;
        int shu;
        Random shouR = new Random();
        Random shuR = new Random();
        shou = shouR.nextInt(30) + 100;
        shu = shuR.nextInt(20) + 70;
//        return "  您的收缩压/舒张压大约为： " + shou + "/" + shu + "mmHg";
        return shou + "";
    }

    public void update_date() {
        String jianshenTemp = initJianShen();
        String xinlvTemp = initXinLv();
        String xuetangTemp = initXueTang();
        String xueyaTemp = initXueYa();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        JSONObject jsonObject = new JSONObject();
        JSONObject ingredients = new JSONObject();
        try {
            ingredients.put("jianshen", jianshenTemp);
            ingredients.put("xinlv", xinlvTemp);
            ingredients.put("xuetang", xuetangTemp);
            ingredients.put("xueya", xueyaTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put(str, ingredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ans = jsonObject.toString();
//        Log.e("json", ans);

        //TODO 或者字符串存入sp
        String before = pref.getString("shentiData", "{\"2017-05-14 16:53:38\":{\"jianshen\":\"1.69\",\"xinlv\":\"111\",\"xuetang\":\"2.27\",\"xueya\":\"129\"}}");
        ans = ans + "+" + before;
        editor.putString("shentiData", ans);
        editor.apply();

    }
}
