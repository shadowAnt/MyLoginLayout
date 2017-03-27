package com.example.mylayout.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class Basic {
    public String city;
    public String cnty;

    @SerializedName("id")
    public String weatherId;

    public String lat;
    public String lon;
    public Update update;

    public class Update{
        public String loc;
        public String utc;
    }
}
