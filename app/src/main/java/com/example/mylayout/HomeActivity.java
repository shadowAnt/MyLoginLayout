package com.example.mylayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private String account;

    private Fun[] funs = {
            new Fun("健身录", R.drawable.fun_run),
            new Fun("测心率", R.drawable.fun_xinlv),
            new Fun("测血糖", R.drawable.fun_xuetang),
            new Fun("量血压", R.drawable.fun_xueya),
            new Fun("消息站", R.drawable.fun_mail),
            new Fun("轻松购", R.drawable.fun_buy)
    };
    private List<Fun> funList = new ArrayList<>();
    private FunAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        TextView username = (TextView)headerView. findViewById(R.id.username);
        username.setText(account);

        TextView welcome = (TextView) findViewById(R.id.status);
        welcome.setText(welcome.getText()+"\n欢迎 用户:"+account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //获取侧拉栏的实例，让ActionBar的返回按钮显示出来，并为按钮换个图片
        //获取滑动菜单的实例，设置默认选中首页，并设置监听器
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_home);
        navView.setNavigationItemSelectedListener(this);

        //卡片式布局
       for(int i=0; i<funs.length; i++){
            funList.add(funs[i]);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FunAdapter(funList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){//滑动菜单的点击效果
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_call:
                if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    navView.setCheckedItem(R.id.nav_home);
                    call();
                }
                break;
            case R.id.nav_weather:
                break;
            default:
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){//定义菜栏上的点击事件
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.update:
                Toast.makeText(this, "正在上传数据...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "设置功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.connect:
                Toast.makeText(this, "正在连接设备...", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void call(){
        try{
            Intent intentCall = new Intent(Intent.ACTION_CALL);
            intentCall.setData(Uri.parse("tel:18856017129"));
            startActivity(intentCall);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                } else {
                    Toast.makeText(this, "你拒绝了拨号权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
