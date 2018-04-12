package com.example.satellite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satellite.MainActivity;
import com.example.satellite.R;
import com.example.satellite.gson.Satellite;
import com.example.satellite.util.HttpUtil;
import com.example.satellite.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/4/11.
 */

public class SatelliteFragment extends Fragment {
    RecyclerView satelliteRecyclerView;
    private  List<Satellite> satelliteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.satellitefragment, container, false);
        satelliteRecyclerView = (RecyclerView) view.findViewById(R.id.current_satellites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        satelliteRecyclerView.setLayoutManager(layoutManager);

        initSatelliteList();
        SatelliteAdapter adapter = new SatelliteAdapter(satelliteList);
        satelliteRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         }

    private void initSatelliteList() {
        for (int i = 0; i < 20; i++) {
            Satellite satellite = new Satellite();
            satellite.setId(1234565);
            satellite.setUrl("ccccc");
            satellite.setName("wwwww");
            satelliteList.add(satellite);
            satellite = new Satellite();
            satellite.setId(862489);
            satellite.setUrl("csdsc");
            satellite.setName("wcbazw");
            satelliteList.add(satellite);
        }
    }

}
