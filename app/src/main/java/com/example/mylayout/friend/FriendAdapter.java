package com.example.mylayout.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylayout.R;
import com.example.mylayout.json.friend;

import java.util.List;

/**
 * Created by ShadowAnt on 2017/5/13.
 */

public class FriendAdapter extends ArrayAdapter<friend> {
    private int resourceId;

    public FriendAdapter(Context context, int textViewResourceId, List<friend> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        friend fri = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView friendName = (TextView) view.findViewById(R.id.friend_name);
        TextView accountView = (TextView) view.findViewById(R.id.account);
        friendName.setText(fri.getDocName());
        accountView.setText(fri.getDocAccount());
        return view;
    }
}
