package com.example.mylayout.gson;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class Now {
    public Cond cond;
    public String fl;
    public String hum;
    public String pcpn;
    public String pres;
    public String tmp;
    public String vis;
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

