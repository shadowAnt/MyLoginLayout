package com.example.mylayout.json;

import android.app.Application;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShadowAnt on 2017/5/13.
 */

public class responseJson implements Serializable {
    public static patInfo self;
    public static List<friend> frinedList;

    public static void setFrinedList(List<friend> frinedList) {
        responseJson.frinedList = frinedList;
    }

    public static void setSelf(patInfo self) {
        responseJson.self = self;
    }
}
