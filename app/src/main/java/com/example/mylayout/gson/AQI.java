package com.example.mylayout.gson;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm10;
        public String pm12;
        public String qlty;
    }
}
