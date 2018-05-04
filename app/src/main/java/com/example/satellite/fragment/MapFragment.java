package com.example.satellite.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.model.LatLngBounds;
//import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.satellite.MainActivity;
import com.example.satellite.MyApplication;
import com.example.satellite.R;
import com.example.satellite.Request;

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
    private View view;
    private int markerCounter = 0;
    private List<LatLng> markerList = new ArrayList<>();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationText = (TextView) view.findViewById(R.id.location_text);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mapView = (MapView) view.findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
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
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                ++markerCounter;
                markerList.add(latLng);
                markOnMap(latLng);
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap.clear();
                markerCounter = 0;
                markerList.clear();
                ((MainActivity)getActivity()).requestFromMap = null;
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });



    }

    private void markOnMap(LatLng latLng) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_36dp);

            //构建MarkerOption，用于在地图上添加Marker

        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        baiduMap.addOverlay(option);
        if (markerCounter == 2) {
            baiduMap.clear();
            markerCounter = 0;
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(markerList.get(0))
                    .include(markerList.get(1))
                    .build();

            //定义Ground显示的图片
            BitmapDescriptor bdGround = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_bound_background);

            //定义Ground覆盖物选项
            OverlayOptions ooGround = new GroundOverlayOptions()
                    .positionFromBounds(bounds)
                    .image(bdGround)
                    .transparency(0.8f);

            //在地图中添加Ground覆盖物
            baiduMap.addOverlay(ooGround);

            //添加信息窗
            final Button button = new Button(MyApplication.getContext());
            button.setBackgroundResource(R.drawable.ic_chat_bubble_outline_black_36dp);
            button.setTextColor(Color.BLACK);
            final LatLng pt = new LatLng((markerList.get(1).latitude + markerList.get(0).latitude)/2, (markerList.get(1).longitude + markerList.get(0).longitude)/2);
            //parseLocation(pt);
            final int radius = (int) DistanceUtil. getDistance(markerList.get(0), markerList.get(1))/2;

            GeoCoder mSearch = GeoCoder.newInstance();

            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

                public void onGetGeoCodeResult(GeoCodeResult result) {

                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                    }

                    //获取地理编码结果
                }

                @Override

                public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult result) {

                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(getContext(), "查询失败", Toast.LENGTH_SHORT).show();
                        //没有找到检索结果
                        button.setEnabled(false);
                        return;
                    }
                    button.setEnabled(true);
                    Toast.makeText(getContext(), "查询成功", Toast.LENGTH_SHORT).show();

                    button.setText(result.getAddress().toString());
                    Log.d(TAG, "onGetReverseGeoCodeResult: "    + result.getAddress().toString());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(getContext(), "观测中心：" + result.getAddress().toString() + "附近\n"
                                    + "范围半径：" + radius + "m", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //获取反向地理编码结果
                    ((MainActivity)getActivity()).requestFromMap = new Request();
                    ((MainActivity)getActivity()).requestFromMap.setLocation(result.getAddress().toString());
                    ((MainActivity)getActivity()).requestFromMap.setLatitude(pt.latitude);
                    ((MainActivity)getActivity()).requestFromMap.setLongitude(pt.longitude);
                    ((MainActivity)getActivity()).requestFromMap.setRadius(radius);
                }
            };
            mSearch.setOnGetGeoCodeResultListener(listener);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(pt));











          /*  if (mResult != null) {
                Log.d(TAG, "markOnMap: " + mResult.getAddress().toString());
                button.setText(mResult.getAddress().toString());
            }*/


            //定义用于显示该InfoWindow的坐标点


            //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
            InfoWindow mInfoWindow = new InfoWindow(button, pt, 0);

            //显示InfoWindow
            baiduMap.showInfoWindow(mInfoWindow);
            mSearch.destroy();
        }

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
       view = inflater.inflate(R.layout.mapfragment, container, false);

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
    private void parseLocation(LatLng pt) {



       // mSearch.destroy();

    }

    private static final String TAG = "MapFragment";

}
