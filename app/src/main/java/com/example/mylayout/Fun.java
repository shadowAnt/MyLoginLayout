package com.example.mylayout;

import java.io.Serializable;

/**
 * Created by ShadowAnt on 2017/3/24.
 */

public class Fun implements Serializable {
    private String name;
    private int imageId;
    private String colorString;

    public Fun(String name, int imageId, String colorString) {
        this.name = name;
        this.imageId = imageId;
        this.colorString = colorString;
    }

    public String getColorString() {
        return colorString;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

}
