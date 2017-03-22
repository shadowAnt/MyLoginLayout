package com.example.mylayout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import space.wangjiang.toaster.Toaster;

/**
 * Created by ShadowAnt on 2017/3/21.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.login_progress);

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login:
                progressBar.setVisibility(View.VISIBLE);
                Toaster.success(getActivity(), "登录成功 !", Toaster.LENGTH_SHORT).show();

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
}
