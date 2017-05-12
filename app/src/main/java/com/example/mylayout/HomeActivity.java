package com.example.mylayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylayout.gson.Weather;
import com.example.mylayout.util.HttpUtil;
import com.example.mylayout.util.ImageFactory;
import com.example.mylayout.util.Post;
import com.example.mylayout.util.UpdateDate;
import com.example.mylayout.util.Utilty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import space.wangjiang.toaster.Toaster;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageView bingPicHome;
    private SharedPreferences pref;
    private List<Fun> funList = new ArrayList<>();
    private FunAdapter adapter;
    private Uri imageUri;
    private String account;
    private Fun[] funs = {
            new Fun("健身录", R.drawable.fun_run, "#5653ef"),
            new Fun("测心率", R.drawable.fun_xinlv, "#7824cc"),
            new Fun("测血糖", R.drawable.fun_xuetang, "#37353A"),
            new Fun("量血压", R.drawable.fun_xueya, "#1E5798"),
            new Fun("消息站", R.drawable.fun_mail, "#37a5e5"),
            new Fun("轻松购", R.drawable.fun_buy, "#576866")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //融为一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_home);

        //读取登陆活动传过来的用户名
        pref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        account = pref.getString("account", "游客");
        navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);//setContentView把View设置为activity_home，但是滑动窗口在nav_view中
        TextView username = (TextView) headerView.findViewById(R.id.username);//重点

        //设置用户头像
        ImageView userIcon = (ImageView) headerView.findViewById(R.id.nav_icon_image);
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        if (outputImage.length() != 0) {
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(HomeActivity.this, "com.example.mylayout.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                ImageFactory imageFactory = new ImageFactory();
                bitmap = imageFactory.ratio(bitmap, 200, 200);
                userIcon.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            userIcon.setImageResource(R.drawable.icon_user);
        }

        username.setText(account);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomeActivity.this, ChangeIconActivity.class);
                startActivityForResult(intent1, 1);
            }
        });

        //首页标题的一些设置
        TextView welcome = (TextView) findViewById(R.id.status);
        welcome.setText(getHello() + ", " + account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取侧拉栏的实例，让ActionBar的返回按钮显示出来，并为按钮换个图片
        //获取滑动菜单的实例，设置默认选中首页，并设置监听器
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_home);
        navView.setNavigationItemSelectedListener(this);

        //卡片式布局
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);//两列
        recyclerView.setLayoutManager(layoutManager);
        for (int i = 0; i < funs.length; i++) {
            funList.add(funs[i]);
            adapter = new FunAdapter(funList);
            recyclerView.setAdapter(adapter);

            //解析天气信息
            String weatherString = pref.getString("weather", null);
            TextView suggestionText = (TextView) findViewById(R.id.suggestion_text);
            TextView degreeText = (TextView) headerView.findViewById(R.id.now_weather);
            if (weatherString != null) {
                Weather weather = Utilty.handleWeatherResponse(weatherString);
                suggestionText.setText("  " + weather.suggestion.flu.txt + "\n  " + weather.suggestion.sport.txt);
                degreeText.setText(weather.basic.city + "  " + weather.now.tmp + "℃");
            } else {
                suggestionText.setText("  暂无天气信息，请首先在滑动菜单获取天气信息");
            }

            //高斯模糊
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            bingPicHome = (ImageView) findViewById(R.id.bing_pic_home);
            String bingPic = prefs.getString("bing_pic", null);
            if (bingPic != null) {
                Glide.with(HomeActivity.this)
                        .load(bingPic)
                        .crossFade(1000)
                        .placeholder(R.drawable.bing_cache)
                        .bitmapTransform(new BlurTransformation(HomeActivity.this, 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(bingPicHome);
            } else {
                loadBingPic();
            }
        }
    }

    private void loadBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpResquest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(HomeActivity.this).load(bingPic).into(bingPicHome);
                    }
                });
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//滑动菜单的点击效果
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_call://一键拨号
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
                item.setCheckable(false);
                break;
            case R.id.nav_weather://获取天气信息
                Intent intent = new Intent(HomeActivity.this, Main2Activity.class);
                startActivity(intent);
                item.setCheckable(false);
                break;
            case R.id.nav_zhihu:
                Toaster.success(this, "正在获取健康专栏", Toaster.LENGTH_SHORT).show();
                item.setCheckable(false);
                Intent intent1 = new Intent(HomeActivity.this, ZhihuActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_location:
                //TODO
                Intent intentLocation = new Intent(HomeActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                item.setCheckable(false);
                break;
            default:
        }
        navView.setCheckedItem(R.id.nav_home);
        mDrawerLayout.closeDrawers();
        return true;
    }

    public String getHello() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        int hour = Integer.parseInt(str);
        //凌晨：0-2点   黎明：4-5点   拂晓：4-6点   清晨：6-7点   早晨：6-8点   上午：8-11点   中午：11-13点   下午：14-17点   晚上：18-22点   傍晚：17-18点   黄昏：16-17点   午夜：23-1点   夜间：19-5点
        if (hour >= 22 && hour <= 6) {
            return "睡觉时间";
        } else if (hour >= 7 && hour <= 8) {
            return "早上好";
        } else if (hour >= 9 && hour <= 11) {
            return "上午好";
        } else if (hour >= 12 && hour <= 14) {
            return "中午好";
        } else if (hour >= 15 && hour <= 19) {
            return "下午好";
        } else {
            return "晚上好";
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//定义菜栏上的点击事件
        switch (item.getItemId()) {
            case android.R.id.home://打开侧拉栏
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.update:
                Toast.makeText(this, "正在上传数据...", Toast.LENGTH_SHORT).show();
                //sendRequestWithOkHttp();
                sendRequestWithPost();
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.connect:
                Toast.makeText(this, "正在更新数据...", Toast.LENGTH_SHORT).show();
                UpdateDate updateDate = new UpdateDate(HomeActivity.this);
                updateDate.update_date();
                break;
            default:
        }
        return true;
    }

    private void sendRequestWithPost() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String xuetang = pref.getString("xuetang", "");
        String jianshen = pref.getString("jianshen", "");
        String xueya = pref.getString("xueya", "");
        String xinlv = pref.getString("xinlv", "");
        RequestBody requestBody = new FormBody.Builder()
                .add("username", "username")
                .add("password", "password")
                .add("xuetang", xuetang)
                .add("jianshen", jianshen)
                .add("xueya", xueya)
                .add("xinlv", xinlv)
                .build();
        String url = "http://192.168.191.1/index.php";
        Post.sendOKHttpResquest(requestBody, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.error(HomeActivity.this, "发送失败!", Toaster.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                System.out.println(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.success(HomeActivity.this, "发送成功!", Toaster.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
                        editor.putString("updateResult", responseData);
                        editor.apply();
                    }
                });
            }
        });
    }

    private void call() {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String callNumber = pref.getString("callNumber", "18856017129");
            Intent intentCall = new Intent(Intent.ACTION_CALL);
            intentCall.setData(Uri.parse("tel:" + callNumber));
            startActivity(intentCall);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toaster.error(this, "你拒绝了拨号权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                View headerView = navView.getHeaderView(0);//setContentView把View设置为activity_home，但是滑动窗口在nav_view中
                ImageView userIcon = (ImageView) headerView.findViewById(R.id.nav_icon_image);
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                if (outputImage.length() != 0) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri = FileProvider.getUriForFile(HomeActivity.this, "com.example.mylayout.fileprovider", outputImage);
                    } else {
                        imageUri = Uri.fromFile(outputImage);
                    }
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ImageFactory imageFactory = new ImageFactory();
                        bitmap = imageFactory.ratio(bitmap, 200, 200);
                        userIcon.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    userIcon.setImageResource(R.drawable.icon_user);
                }
                break;
            default:
        }
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}

