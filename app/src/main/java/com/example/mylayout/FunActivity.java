package com.example.mylayout;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);

        Fun fun = (Fun) getIntent().getSerializableExtra("fun_data");
        String funName = fun.getName();
        int funImageId = fun.getImageId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView funImageView = (ImageView) findViewById(R.id.fun_image_view);
        TextView funContentText = (TextView) findViewById(R.id.fun_content_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(funName);
        Glide.with(this).load(funImageId).into(funImageView);
        String funContent = initFunContent(funName);
        funContentText.setText(funContent);
    }

    private String initFunContent(String funName){
        StringBuilder funContent = new StringBuilder();
        funContent.append(funName);
        return funContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
