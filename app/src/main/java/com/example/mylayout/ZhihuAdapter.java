package com.example.mylayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShadowAnt on 2017/4/8.
 */

public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Zhihu> mZhihuList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView zhihuTitle;
        TextView zhihuNumber;
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            zhihuTitle = (TextView) view.findViewById(R.id.zhihu_title);
            zhihuNumber = (TextView) view.findViewById(R.id.zhihu_number);
            icon = (ImageView) view.findViewById(R.id.userIcon);
        }
    }

    public ZhihuAdapter(ArrayList<Zhihu> zhihuList) {
        mZhihuList = zhihuList;
    }

    @Override
    public ZhihuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.zhihu_item, parent, false);

        final ZhihuAdapter.ViewHolder holder = new ZhihuAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Zhihu zhihu = mZhihuList.get(position);
                Intent intent = new Intent(mContext, WebActvity.class);
                intent.putExtra("zhihu_url", zhihu.getUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ZhihuAdapter.ViewHolder holder, int position) {
        Zhihu zhihu = mZhihuList.get(position);
        holder.zhihuTitle.setText(zhihu.getTitle());
        holder.zhihuNumber.setText(zhihu.getNumber());
        Glide.with(mContext).load(zhihu.getPic()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mZhihuList.size();
    }
}

