package com.example.mylayout;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.mylayout.util.Post;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import space.wangjiang.toaster.Toaster;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView beginTime;
    TextView endTime;
    String type = "jianshen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Button chooseBegin = (Button) findViewById(R.id.choose_begin);
        chooseBegin.setOnClickListener(this);
        Button chooseEnd = (Button) findViewById(R.id.choose_end);
        chooseEnd.setOnClickListener(this);
        beginTime = (TextView) findViewById(R.id.beginTimeText);
        endTime = (TextView) findViewById(R.id.endTimeText);

        //TODO 得到url
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("URL", "");

        Button queryJianshenButton = (Button) findViewById(R.id.queryJianshen);
        Button queryXuetaButton = (Button) findViewById(R.id.queryXuetang);
        Button queryXinlvButton = (Button) findViewById(R.id.queryXinlv);
        Button queryXueyaButton = (Button) findViewById(R.id.queryXueya);
        queryJianshenButton.setOnClickListener(this);
        queryXuetaButton.setOnClickListener(this);
        queryXinlvButton.setOnClickListener(this);
        queryXueyaButton.setOnClickListener(this);
    }

    public void go() {
        String start = beginTime.getText().toString();
        String end = endTime.getText().toString();
        String patID = "1111111";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("URL", "");
        url += "queryPatInfo";
        RequestBody requestBody = new FormBody.Builder()
                .add("patID", "1111111")
                .add("start", start)
                .add("end", end)
                .build();
        Post.sendOKHttpResquest(requestBody, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.error(QueryActivity.this, "发送失败!", Toaster.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                System.out.println(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.success(QueryActivity.this, "发送成功!", Toaster.LENGTH_SHORT).show();
                        //TODO 根据type来解析json
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONObject typeObject = jsonObject.getJSONObject(type);
                            JSONObject timeObject = jsonObject.getJSONObject("time");
                            int len = typeObject.length();
                            float[] ans = new float[len];
                            String[] timeString = new String[len];
                            for (int i = 0; i < len; i++) {
                                ans[i] = (float) typeObject.getDouble(i + "");
                                timeString[i] = timeObject.getString(i + "");
                            }
                            SuitLines suitLines = (SuitLines) findViewById(R.id.QuerySuitlines);
                            List<Unit> lines = new ArrayList<>();
                            //TODO 把funContent 里面的内容按\n 分割，放到数组中
                            for (int i = 0; i < len; i++) {
                                lines.add(new Unit(ans[i], i + ""));
                            }
                            suitLines.feedWithAnim(lines);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.queryJianshen:
                type = "jianshen";
                go();
                break;
            case R.id.queryXuetang:
                type = "xuetang";
                go();
                break;
            case R.id.queryXinlv:
                type = "xueya";
                go();
                break;
            case R.id.queryXueya:
                type = "xinlv";
                go();
                break;
            case R.id.choose_begin:
                //时间选择器
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = formatter.format(date);
                        beginTime.setText(str);
                    }
                })
                        .setType(TimePickerView.Type.ALL)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(18)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("选择起始时间")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(false)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.choose_end:
                //时间选择器
                TimePickerView edTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = formatter.format(date);
                        endTime.setText(str);
                    }
                })
                        .setType(TimePickerView.Type.ALL)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(18)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("选择终止时间")//标题文字
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(false)//是否显示为对话框样式
                        .build();
                edTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                edTime.show();
                break;
        }
    }

}
