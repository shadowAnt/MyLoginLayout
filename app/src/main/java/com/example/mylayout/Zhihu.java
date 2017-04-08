package com.example.mylayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShadowAnt on 2017/4/6.
 */

public class Zhihu {
    private String url;
    private String title;

    public Zhihu(String url, String title) {
        Pattern pattern = Pattern.compile("question/(.*?)/");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            this.url = "https://www.zhihu.com/question/" + matcher.group(1);
        } else {
            this.url = url;
        }
        this.title = title;
    }
}