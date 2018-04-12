package com.example.satellite.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satellite.LiveActivity;
import com.example.satellite.MainActivity;
import com.example.satellite.MyApplication;
import com.example.satellite.R;
import com.example.satellite.gson.Satellite;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class SatelliteAdapter extends RecyclerView.Adapter<SatelliteAdapter.ViewHolder> {
    private List<Satellite> mSatelliteList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView idText;
        View itemView;
        public ViewHolder (View view) {
            super(view);
            itemView = view;
            nameText = (TextView) view.findViewById(R.id.satellite_name);
            idText = (TextView) view.findViewById(R.id.satellite_id);
        }
    }
    public SatelliteAdapter (List<Satellite> satelliteList) {
        mSatelliteList = satelliteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.satellite_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Satellite satellite = mSatelliteList.get(position);
                //测试时显示url，发布版开启活动或碎片播放视频流
                Toast.makeText(v.getContext(), satellite.getName() + "的播放地址为：" + satellite.getUrl(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, LiveActivity.class);
                intent.putExtra("url", satellite.getUrl());
                intent.putExtra("name", satellite.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Satellite satellite = mSatelliteList.get(position);
        holder.nameText.setText("卫星名字：" + satellite.getName());
        holder.idText.setText("卫星id:" + satellite.getSatelliteId());
    }

    @Override
    public int getItemCount() {
        return mSatelliteList.size();

    }
}