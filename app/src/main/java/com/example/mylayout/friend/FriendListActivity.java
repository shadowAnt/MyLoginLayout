package com.example.mylayout.friend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.mylayout.R;
import com.example.mylayout.json.responseJson;


public class FriendListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        listView = (ListView) findViewById(R.id.list_view);

        if (responseJson.frinedList!=null) {
            Log.d("patInfo", responseJson.frinedList.get(0).docName);
        } else {
            Log.e("null", "null");
        }
        Log.e("lenth", responseJson.frinedList.size() + "");
        FriendAdapter friendAdapter = new FriendAdapter(this, R.layout.friend_item, responseJson.frinedList);
        listView.setAdapter(friendAdapter);
    }
}
