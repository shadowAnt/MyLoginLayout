package com.example.mylayout;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import space.wangjiang.toaster.Toaster;

/**
 * Created by ShadowAnt on 2017/3/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private ProgressBar progressBar;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private AutoCompleteTextView accountEdit;
    private AutoCompleteTextView passwordEdit;
    private ImageView gaosiLogin;

    private String account;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.login_progress);
        rememberPass = (CheckBox) view.findViewById(R.id.remember_pass);
        accountEdit = (AutoCompleteTextView) view.findViewById(R.id.name);
        passwordEdit = (AutoCompleteTextView) view.findViewById(R.id.password);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        load_account_password();//加载用户名，根据上次保存密码与否进行读入密码

        //高斯模糊
        gaosiLogin = (ImageView) view.findViewById(R.id.gaosi_login);
        Glide.with(getActivity())
                .load(R.drawable.icon_user)
                .crossFade(1000)
                .placeholder(R.drawable.icon_user)
                .bitmapTransform(new BlurTransformation(getActivity(), 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(gaosiLogin);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                progressBar.setVisibility(View.VISIBLE);//让进度条显示

                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();//得到输入的用户名和密码
                save_account_password();//保存
                Toaster.success(getActivity(), "登录成功 !", Toast.LENGTH_SHORT).show();

                //点击按钮生成一个Intent，传递信息给HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("account", account);//首页会用到用户名
                startActivity(intent);

                //结束掉登录界面活动
                getActivity().finish();
                break;
            default:
        }
    }

    public void load_account_password() {
        boolean isRemember = pref.getBoolean("remember_password", false);
        //用户名加载进来
        account = pref.getString("account", "");
        accountEdit.setText(account);
        if (isRemember) {
            password = pref.getString("password", "");
            passwordEdit.setText(password);
            rememberPass.setChecked(true);//启动的时候把对钩打上
        }
    }

    public void save_account_password() {
        editor = pref.edit();
        editor.putString("account", account);//无脑存储用户名
        if (rememberPass.isChecked()) {//根据记住密码与否存储密码
            editor.putBoolean("remember_password", true);
            editor.putString("password", password);
        }
        editor.apply();//将数据保存，异步，比commit快
    }
}
