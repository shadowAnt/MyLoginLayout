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

                save_account_password();//保存
                Toast.makeText(getActivity(), "登录成功 !", Toast.LENGTH_SHORT).show();

                //点击按钮生成一个Intent，传递信息给HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("send", "登录成功!");
                startActivity(intent);

                //结束掉登录界面活动
                getActivity().finish();
                break;
            default:
        }
    }

    public void load_account_password(){
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
    }

    public void save_account_password(){
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        editor = pref.edit();
        if(rememberPass.isChecked()){
            editor.putBoolean("remember_password", true);
            editor.putString("account", account);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.apply();//将数据保存，异步，比commit快
    }
}
