package com.example.mylayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by ShadowAnt on 2017/3/24.
 */

public class FunAdapter extends RecyclerView.Adapter<FunAdapter.ViewHolder> {
    private Context mContext;
    private List<Fun> mFunList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView funImage;
        TextView funName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            funImage = (ImageView) view.findViewById(R.id.fun_image);
            funName = (TextView) view.findViewById(R.id.fun_name);
        }
    }

    public FunAdapter(List<Fun> funList) {
        mFunList = funList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fun_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fun fun = mFunList.get(position);
                Intent intent = new Intent(mContext, FunActivity.class);
                intent.putExtra("fun_data", fun);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fun fun = mFunList.get(position);
        holder.funName.setText(fun.getName());
        holder.cardView.setCardBackgroundColor(Color.parseColor(fun.getColorString()));
        Glide.with(mContext).load(fun.getImageId()).into(holder.funImage);
    }

    @Override
    public int getItemCount() {
        return mFunList.size();
    }

}
