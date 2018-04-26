package com.example.satellite.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import com.example.satellite.R;

import java.util.ArrayList;

import java.util.List;


/**
 * Created by Administrator on 2018/4/10.
 */

public class MapFragment extends Fragment {
    private BaiduMap baiduMap;
    public LocationClient mLocationClient;
    private TextView locationText;
    private MapView mapView;
    private FloatingActionButton fab;
    private double latitude;
    private double longitude;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //SDKInitializer.initialize(getContext());
        //runtime permission
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
        baiduMap.setMyLocationEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移动到我的位置
                LatLng ll = new LatLng(latitude, longitude);
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(update);
                //让我显示在地图上
                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(latitude);
                locationBuilder.longitude(longitude);
                MyLocationData locationData = locationBuilder.build();
                baiduMap.setMyLocationData(locationData);
            }
        });
    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1 :
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getContext(), "未获得授权，不能打开地图", Toast.LENGTH_SHORT).show();
                            //关闭地图碎片
                            return;

                        }
                        requestLocation();
                    }
                } else {
                    Toast.makeText(getContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    //关闭地图碎片
                }
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.mapfragment, container, false);
        locationText = (TextView) view.findViewById(R.id.location_text);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mapView = (MapView) view.findViewById(R.id.map_view);
        baiduMap = mapView.getMap();

        return view;
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                fab.setVisibility(View.VISIBLE);
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("；\n");
                currentPosition.append("经度：").append(bdLocation.getLongitude()).append("；");
                locationText.setText(currentPosition.toString());
            } else {
                //fab.setVisibility(View.GONE);
            }

        }
    }

}
