package com.example.mylayout;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class QueryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        //TODO 得到url
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("URL", "");

        SuitLines suitLines = (SuitLines) findViewById(R.id.QuerySuitlines);
        List<Unit> lines = new ArrayList<>();
        //TODO 把funContent 里面的内容按\n 分割，放到数组中
        for (int i = 0; i < 2; i++) {
            lines.add(new Unit((float)0, i + ""));
        }
        suitLines.feedWithAnim(lines);
    }
}
