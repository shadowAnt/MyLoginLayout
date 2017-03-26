package com.example.mylayout.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ShadowAnt on 2017/3/25.
 */

public class City extends DataSupport{
    private int id;
    private String cityName;
    private int cityCode;
    private int ProvinceId;

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }
}
