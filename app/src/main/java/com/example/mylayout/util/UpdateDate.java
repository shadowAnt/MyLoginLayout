package com.example.mylayout.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
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
        return "  您今天大约走了" + df.format(length) + "km";
    }

    public String initXinLv() {
        int xinlv;
        Random random = new Random();
        xinlv = random.nextInt(70) + 50;
        return "  您的平均心率大约为：" + xinlv + "次/分";
    }

    public String initXueTang() {
        double xuetang;
        Random random = new Random();
        xuetang = random.nextDouble() * 10;
        return "  您的平均血糖值大约为： " + df.format(xuetang) + "mmol/L";
    }

    public String initXueYa() {
        int shou;
        int shu;
        Random shouR = new Random();
        Random shuR = new Random();
        shou = shouR.nextInt(30) + 100;
        shu = shuR.nextInt(20) + 70;
        return "  您的收缩压/舒张压大约为： " + shou + "/" + shu + "mmHg";
    }

    public void update_date() {
        editor.putString("jianshen", initJianShen());
        editor.putString("xinlv", initXinLv());
        editor.putString("xuetang", initXueTang());
        editor.putString("xueya", initXueYa());
        editor.apply();
    }
}
