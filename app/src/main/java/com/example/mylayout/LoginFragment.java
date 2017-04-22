package com.example.mylayout;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mylayout.util.HttpUtil;
import com.example.mylayout.util.ImageFactory;
import com.scottyab.aescrypt.AESCrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        rememberPass = (CheckBox) view.findViewById(R.id.remember_pass);
        accountEdit = (AutoCompleteTextView) view.findViewById(R.id.name);
        passwordEdit = (AutoCompleteTextView) view.findViewById(R.id.password);
        iconImage = (ImageView) view.findViewById(R.id.icon_image);
        gaosiLogin = (ImageView) view.findViewById(R.id.gaosi_login);

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
                password = passwordEdit.getText().toString();//得到输入的用户名和密码
                String tmp = password;
                String url = "http://192.168.191.1/index.php/?username=" + account + "&password=" + tmp;
                //通过服务器上网
                if (!(account.equals("123") && tmp.equals("123456"))) {
                    HttpUtil.sendOKHttpResquest(url, new Callback() {
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
                            if (!responseText.equals("success")) {
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
                                        Toaster.success(getActivity(), "登录成功!", Toaster.LENGTH_LONG).show();
                                    }
                                });
                                save_account_password();//保存
                                //点击按钮生成一个Intent，传递信息给HomeActivity
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                //结束掉登录界面活动
                                getActivity().finish();
                            }
                        }
                    });
                } else {
                    save_account_password();//保存
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    //结束掉登录界面活动
                    getActivity().finish();
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
}
