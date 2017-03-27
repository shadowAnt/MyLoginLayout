package com.example.mylayout.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ShadowAnt on 2017/3/27.
 */

public class Weather {
    public AQI aqi;
    public Basic basic;

    @SerializedName("daily_forecast")
    public List<DayForecast> dayForecastList;

    @SerializedName("hourly_forecast")
    public List<HourForecast> hourForecastList;

    public Now now;

    public String status;

    public Suggestion suggestion;
}
