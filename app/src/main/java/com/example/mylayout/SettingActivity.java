package com.example.mylayout;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import space.wangjiang.toaster.Toaster;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    AutoCompleteTextView callNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        callNumberText = (AutoCompleteTextView) findViewById(R.id.call_number_text);
        Button save = (Button) findViewById(R.id.save_button);
        Button cancle = (Button) findViewById(R.id.cancel_button);

        save.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cancel_button:
                finish();
                break;
            case R.id.save_button:
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("callNumber", callNumberText.getText().toString());
                editor.apply();
                Toaster.success(this, "保存成功", Toaster.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
