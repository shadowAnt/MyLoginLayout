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
        public String mr;
        public String ms;
        public String sr;
        public String ss;
    }
    public class Cond{
        public String code_d;
        public String code_n;
        public String txt_d;
        public String txt_n;
    }
    public class Tmp{
        public String max;
        public String min;
    }
    public class Wind{
        public String deg;
        public String dir;
        public String sc;
        public String spd;
    }
}