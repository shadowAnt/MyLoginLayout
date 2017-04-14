package com.example.mylayout;


import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mylayout.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import space.wangjiang.toaster.Toaster;

public class ZhihuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ImageView bingImage;

    private ArrayList<Zhihu> ZhihuList = new ArrayList<>();
    private String responseString = "";
    private ZhihuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);

        bingImage = (ImageView) findViewById(R.id.bing_image);
        Glide.with(this).load(R.drawable.bing_cache).into(bingImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("健康");

        getZhihu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getZhihu() {
        HttpUtil.sendOKHttpResquest("https://www.zhihu.com/explore/recommendations", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.error(ZhihuActivity.this, "获取失败", Toaster.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseString = response.body().string();
                //爬虫处理
                ZhihuList = getNews(responseString);//更新ZhihuList
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_zhihu);
                        layoutManager = new LinearLayoutManager(ZhihuActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new ZhihuAdapter(ZhihuList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public static ArrayList<Zhihu> getNews(String responseString) {
        ArrayList<Zhihu> ZhihuList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?>\\n(([^x00-xff]|.)+?)</a>(.|\\n)+?data-bind-votecount>(.+?)</a>");
        Matcher matcher = pattern.matcher(responseString);
        Boolean isFind = matcher.find();
        while (isFind) {
            //System.out.println("1 " + matcher.group(1) + "\n2  " + matcher.group(2) + "\n5  "+ matcher.group(5));
            //  定义一个知乎对象来存储抓取到的信息
            Zhihu zhihuTemp = new Zhihu(matcher.group(1), matcher.group(2), matcher.group(5));
            // 添加成功匹配的结果
            ZhihuList.add(zhihuTemp);
            // 继续查找下一个匹配对象
            isFind = matcher.find();
        }
        return ZhihuList;
    }
}
