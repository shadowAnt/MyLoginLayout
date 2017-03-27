package com.example.mylayout.gson;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class DayForecast {
    public Astro astro;
    public Cond cond;
    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;
    public Tmp tmp;
    public String uv;
    public String vis;
    public Wind wind;

    public class Astro{
        String mr;
        String ms;
        String sr;
        String ss;
    }
    public class Cond{
        String code_d;
        String code_n;
        String txt_d;
        String txt_n;
    }
    public class Tmp{
        String max;
        String min;
    }
    public class Wind{
        String deg;
        String dir;
        String sc;
        String spd;
    }
}