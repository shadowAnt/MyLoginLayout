package com.example.mylayout;

import java.io.Serializable;

/**
 * Created by ShadowAnt on 2017/3/24.
 */

public class Fun implements Serializable {
    private String name;
    private int imageId;

    public Fun(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

}
