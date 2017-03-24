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
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by ShadowAnt on 2017/3/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    private ProgressBar progressBar;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private AutoCompleteTextView accountEdit;
    private AutoCompleteTextView passwordEdit;

    private String account;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.login_progress);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rememberPass = (CheckBox) view.findViewById(R.id.remember_pass);
        accountEdit = (AutoCompleteTextView) view.findViewById(R.id.name);
        passwordEdit = (AutoCompleteTextView) view.findViewById(R.id.password);
        load_account_password();
        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login:
                progressBar.setVisibility(View.VISIBLE);

                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                save_account_password();//保存
                Toast.makeText(getActivity(), "登录成功 !", Toast.LENGTH_SHORT).show();

                //点击按钮生成一个Intent，传递信息给HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);

                //结束掉登录界面活动
                getActivity().finish();
                break;
            default:
        }
    }

    public void load_account_password(){
        boolean isRemember = pref.getBoolean("remember_password",false);
        account = pref.getString("account", "");
        accountEdit.setText(account);
        if(isRemember){
            password = pref.getString("password", "");
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
    }

    public void save_account_password(){
        editor = pref.edit();
        editor.putString("account", account);
        if(rememberPass.isChecked()){
            editor.putBoolean("remember_password", true);
            editor.putString("password", password);
        }
        editor.apply();//将数据保存，异步，比commit快
    }
}
