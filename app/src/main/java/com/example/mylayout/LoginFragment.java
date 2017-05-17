package com.example.mylayout;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mylayout.json.friend;
import com.example.mylayout.json.patInfo;
import com.example.mylayout.json.responseJson;
import com.example.mylayout.util.ImageFactory;
import com.example.mylayout.util.Post;
import com.google.gson.Gson;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import space.wangjiang.toaster.Toaster;

/**
 * Created by ShadowAnt on 2017/3/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private AutoCompleteTextView accountEdit;
    private AutoCompleteTextView passwordEdit;
    private ImageView gaosiLogin;
    private ImageView iconImage;
    private Uri imageUri;
    private String account;
    private static String password;
    private ProgressDialog progressDialog;
    private Button login;
    EditText editText;
    String input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //融为一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        //登录按钮
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        rememberPass = (CheckBox) view.findViewById(R.id.remember_pass);
        accountEdit = (AutoCompleteTextView) view.findViewById(R.id.name);
        passwordEdit = (AutoCompleteTextView) view.findViewById(R.id.password);
        iconImage = (ImageView) view.findViewById(R.id.icon_image);
        gaosiLogin = (ImageView) view.findViewById(R.id.gaosi_login);
        editText = (EditText) view.findViewById(R.id.input_editText);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        load_account_password();//加载用户名，根据上次保存密码与否进行读入密码

        //设置用户头像
        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
        if (outputImage.length() != 0) {
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(getActivity(), "com.example.mylayout.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                ImageFactory imageFactory = new ImageFactory();
                bitmap = imageFactory.ratio(bitmap, 200, 200);
                iconImage.setImageBitmap(bitmap);
                Drawable drawable = new BitmapDrawable(bitmap);
                //高斯模糊
                Glide.with(getActivity())
                        .load(imageUri)
                        .crossFade(1000)
                        .placeholder(drawable)//加载过程中的资源
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//不进行缓存
                        .skipMemoryCache(true)
                        .bitmapTransform(new BlurTransformation(getActivity(), 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(gaosiLogin);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            iconImage.setImageResource(R.drawable.icon_user);
            //高斯模糊
            Glide.with(getActivity())
                    .load(R.drawable.icon_user)
                    .crossFade(1000)
                    .placeholder(R.drawable.icon_user)//加载过程中的资源
                    .bitmapTransform(new BlurTransformation(getActivity(), 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                    .into(gaosiLogin);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                showProgressDialog();
                account = accountEdit.getText().toString();
                input = editText.getText().toString();
                password = passwordEdit.getText().toString();//得到输入的用户名和密码
                String tmp = password;
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", account)
                        .add("password", tmp)
                        .build();
                String url = "http://" + input + ":8080/population/AppLogin";
                //通过服务器上网
                if (!(account.equals("123") && tmp.equals("123456"))) {
                    Post.sendOKHttpResquest(requestBody, url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toaster.error(getActivity(), "登录失败，网络问题或服务器异常", Toaster.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            if (responseText.equals("false")) {
                                //登录失败的处理
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toaster.error(getActivity(), "登录失败，请核对用户名或密码", Toaster.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                System.out.println(responseText);//登录成功的处理
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //TODO 把返回的json解析
                                        responseJson resultTemp = handle(responseText);
                                        responseJson.setFrinedList(resultTemp.frinedList);
                                        responseJson.setSelf(resultTemp.self);
                                        Toaster.success(getActivity(), "登录成功!", Toaster.LENGTH_LONG).show();
                                    }
                                });
                                next();
                            }
                        }
                    });
                } else {
                    //使用123 123456登录
                    next();
                }
                closeProgressDialog();
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
            try {
                password = AESCrypt.decrypt("password", password);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            Log.d("解密", password);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);//启动的时候把对钩打上
        }
    }

    public void save_account_password() {
        try {
            password = AESCrypt.encrypt("password", password);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Log.d("加密", password);
        editor = pref.edit();
        editor.putString("account", account);//无脑存储用户名
        editor.putString("URL", "http://" + input + ":8080/population/");
        if (rememberPass.isChecked()) {
            //根据记住密码与否存储密码
            editor.putBoolean("remember_password", true);
            editor.putString("password", password);
        }
        editor.apply();//将数据保存，异步，比commit快
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在登录...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void next() {
        String responseText = "{\n" +
                "    \"patInfo\": {\n" +
                "        \"patIdCard\": \"1111111\",\n" +
                "        \"patName\": \"病人1\",\n" +
                "        \"patAccount\": \"" +
                "P1111111\"\n" +
                "    },\n" +
                "    \"friends\": {\n" +
                "        \"0\": {\n" +
                "            \"docName\": \"林涛\",\n" +
                "            \"docId\": \"340621199604270312\",\n" +
                "            \"docAccount\": \"E41414049\"\n" +
                "        },\n" +
                "        \"1\": {\n" +
                "            \"docName\": \"赵子彰\",\n" +
                "            \"docId\": \"340621199604270313\",\n" +
                "            \"docAccount\": \"E41414047\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        responseJson resultTemp = handle(responseText);
        responseJson.setFrinedList(resultTemp.frinedList);
        responseJson.setSelf(resultTemp.self);
        save_account_password();//保存
        //点击按钮生成一个Intent，传递信息给HomeActivity
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        //结束掉登录界面活动
        getActivity().finish();
    }

    public responseJson handle(String response) {
        responseJson result = new responseJson();
        result.frinedList = new ArrayList();
        friend one = new friend("姓名", "序号", "序号");
        result.frinedList.add(one);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.length() == 2) {
                JSONObject frsObject = jsonObject.getJSONObject("friends");
                int lenth = frsObject.length();//2
                for (int i = 0; i < lenth; i++) {
                    String ans = i + "";
                    JSONObject frsObjecti = frsObject.getJSONObject(ans);
                    String frsStringi = frsObjecti.toString();
                    friend fritemp = new Gson().fromJson(frsStringi, friend.class);
                    result.frinedList.add(fritemp);
                    Log.d("add", "成功添加" + i);
                }
            }
            JSONObject patObject = jsonObject.getJSONObject("patInfo");
            String patString = patObject.toString();
            patInfo patinfo = new Gson().fromJson(patString, patInfo.class);
            result.self = patinfo;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
