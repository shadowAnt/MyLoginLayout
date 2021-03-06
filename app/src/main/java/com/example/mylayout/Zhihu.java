package com.example.mylayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShadowAnt on 2017/4/6.
 */

public class Zhihu {
    private String url;
    private String title;
    private String number;
    private String pic;

    public String getPic() {
        return pic;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Zhihu(String url, String title, String number, String pic) {
        Pattern pattern = Pattern.compile("question/(.*?)/");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            this.url = "https://www.zhihu.com/question/" + matcher.group(1);
        } else {
            this.url = url;
        }
        this.title = title + "  ";
        this.number = "回答数: " + number;
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "\n\ntitle: " + title + "number: " + number;
    }
}