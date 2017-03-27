package com.example.mylayout.gson;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class HourForecast {
    public Cond cond;
    public String date;
    public String hum;
    public String pop;
    public String pres;
    public String tmp;
    public Wind wind;

    public class Cond{
        String code;
        String txt;
    }
    public class Wind{
        String deg;
        String dir;
        String sc;
        String spd;
    }
}