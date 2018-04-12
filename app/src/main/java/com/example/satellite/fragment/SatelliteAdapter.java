package com.example.satellite.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.satellite.R;
import com.example.satellite.gson.Satellite;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class SatelliteAdapter extends RecyclerView.Adapter<SatelliteAdapter.ViewHolder> {
    private List<Satellite> mSatelliteList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView idText;
        public ViewHolder (View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.satellite_name);
            idText = (TextView) view.findViewById(R.id.satellite_id);
        }
    }
    public SatelliteAdapter (List<Satellite> satelliteList) {
        mSatelliteList = satelliteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.satellite_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
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