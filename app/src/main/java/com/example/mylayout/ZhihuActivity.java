package com.example.mylayout;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    Button back;
    FloatingActionButton f5Button;
    private ArrayList<Zhihu> ZhihuList = new ArrayList<>();
    private String responseString = "";
    private ZhihuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //融为一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_zhihu);
        //加载每日一图
        bingImage = (ImageView) findViewById(R.id.bing_image);
        Glide.with(this)
                .load("https://www.dujin.org/sys/bing/1920.php")
                .placeholder(R.drawable.bing_cache)//加载过程中的资源
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不进行缓存
                .into(bingImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("健康");
        getZhihu();
        f5Button = (FloatingActionButton) findViewById(R.id.f5);
        f5Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getZhihu();
            }
        });
        back = (Button) findViewById(R.id.back_button);
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
//        Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?>\\n(([^x00-xff]|.)+?)</a>(.|\\n)+?data-bind-votecount>(.+?)</a>");
        Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?>\\n(([^x00-xff]|.)+?)</a>(.|\\n)+?data-bind-votecount>(.+?)</a>" +
                "(.|\\n)+?<img src=\"(.+?)\"(.|\\n)+?</div>\\n</div>\\n</div>\\n</div>");

//        clearfix">
//                <img src="https://pic1.zhimg.com/v2-918454d6591d77f77307037c8e75bc78_200x112.jpg" data-rawwidth
        Matcher matcher = pattern.matcher(responseString);
        Boolean isFind = matcher.find();
        while (isFind) {
            //  定义一个知乎对象来存储抓取到的信息
            Zhihu zhihuTemp = new Zhihu(matcher.group(1), matcher.group(2), matcher.group(5), matcher.group(7));
//            System.out.println("1 " + matcher.group(1) + "\n"
//                    + "2 " + matcher.group(2) + "\n"
//                    + "3 " + matcher.group(3) + "\n"
//                    + "4 " + matcher.group(4) + "\n"
//                    + "5 " + matcher.group(5) + "\n"
//                    + "6 " + matcher.group(6) + "\n"
//                    + "7 " + matcher.group(7) + "\n"
//            );

            // 添加成功匹配的结果
            ZhihuList.add(zhihuTemp);
            // 继续查找下一个匹配对象
            isFind = matcher.find();
        }
        return ZhihuList;
    }
}
