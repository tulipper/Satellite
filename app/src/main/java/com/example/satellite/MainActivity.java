package com.example.satellite;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.satellite.fragment.MapFragment;
import com.example.satellite.fragment.SatelliteFragment;
import com.example.satellite.fragment.UserFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    //userSettint
    private static boolean useDefault = true;
    private static String defaultHttpAddress = "http://192.168.1.115";
    private static String defaultTime = "5"; //(minutes)
    //Buttons

    public static boolean isUseDefault() {
        return useDefault;
    }

    public static void setUseDefault(boolean useDefault) {
        MainActivity.useDefault = useDefault;
    }

    Button satelliteButton;
    Button mapButton;
    Button goalButton;
    Button userButton;

    //Fragments
    public List<Fragment> currentFragments;
    public Fragment currentFragment;
    private GoalFragment goalFragment;
    private SatelliteFragment satelliteFragment;
    private MapFragment mapFragment;
    private UserFragment userFragment;
    public Request requestFromMap;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap();
        initServerAddress();
        setContentView(R.layout.activity_main);
        currentFragments = new ArrayList<>();
        satelliteButton = (Button)findViewById(R.id.satellite);
        mapButton = (Button) findViewById(R.id.map);
        goalButton = (Button) findViewById(R.id.goal);
        userButton = (Button) findViewById(R.id.user);
        goalFragment = new GoalFragment();
        satelliteFragment = new SatelliteFragment();
        mapFragment = new MapFragment();
        userFragment = new UserFragment();
        currentFragment = userFragment;
        initShow();
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(currentFragment instanceof MapFragment || currentFragment instanceof SatelliteFragment
                        || currentFragment instanceof GoalFragment || currentFragment instanceof UserFragment))
                    onBackPressed();
                initButtonBackground();
                mapButton.setBackgroundResource(R.drawable.ic_map_black_36dp);
                switchFragment(mapFragment);
            }
        });
        satelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(currentFragment instanceof MapFragment || currentFragment instanceof SatelliteFragment
                        || currentFragment instanceof GoalFragment || currentFragment instanceof UserFragment))
                    onBackPressed();
                initButtonBackground();
                satelliteButton.setBackgroundResource(R.drawable.ic_flight_black_36dp);
                switchFragment(satelliteFragment);
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(currentFragment instanceof MapFragment || currentFragment instanceof SatelliteFragment
                        || currentFragment instanceof GoalFragment || currentFragment instanceof UserFragment))
                    onBackPressed();
                initButtonBackground();
                userButton.setBackgroundResource(R.drawable.ic_account_box_black_36dp);
                switchFragment(userFragment);

            }
        });
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(currentFragment instanceof MapFragment || currentFragment instanceof SatelliteFragment
                        || currentFragment instanceof GoalFragment || currentFragment instanceof UserFragment))
                    onBackPressed();
                initButtonBackground();
                goalButton.setBackgroundResource(R.drawable.ic_my_location_black_36dp);
                switchFragment(goalFragment);
            }
        });

    }

    private void initServerAddress() {
        BmobQuery<IPAddress> query = new BmobQuery<IPAddress>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = sdf.format(new Date());
        Log.d(TAG, "initServerAddress: todya" + today);
        //String today = "2015-05-01 00:00:00";
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date));
        query.setLimit(1);
        query.order("-updatedAt");
        query.findObjects(new FindListener<IPAddress>() {
            @Override
            public void done(List<IPAddress> object, BmobException e) {
                if(e == null){
                    IPAddress lastIp = object.get(0);
                    Log.d(TAG, "Id" + lastIp.getObjectId());
                    Log.d(TAG, "ipaddress" + lastIp.getIpaddress());
                    Log.d(TAG, "CreatedAt"+lastIp.getCreatedAt());
                    //object.size()
                    Log.d(TAG, "done: " + object.get(0).getIpaddress());
                    MainActivity.setDefaultHttpAddress("http://" + object.get(0).getIpaddress());
                    Toast.makeText(MainActivity.this, "服务器地址初始化成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            currentFragments.add(targetFragment);
            transaction
                    .hide(currentFragment)
                    .hide(satelliteFragment)
                    .add(R.id.frag_container, targetFragment)
                    .commit();
            Toast.makeText(this, "还没有添加过", Toast.LENGTH_SHORT).show();
        } else {
            transaction
                    .hide(currentFragment)
                    .hide(userFragment)
                    .hide(satelliteFragment)
                    .show(targetFragment)
                    .commit();
            //System.out.println("添加了( ⊙o⊙ )哇");
            Toast.makeText(this, "添加过了", Toast.LENGTH_SHORT).show();
        }
        currentFragment = targetFragment;
        Log.d(TAG, "currentFragment" + currentFragment.toString());
    }
    private void initShow() {
        //satelliteButton.setVisibility(View.GONE);
        currentFragments.add(currentFragment);
        initButtonBackground();
        userButton.setBackgroundResource(R.drawable.ic_account_box_black_36dp);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction
                .add(R.id.frag_container, satelliteFragment)
                .hide(satelliteFragment)
                .add(R.id.frag_container, currentFragment)
                .commit();
        Log.d(TAG, "currentFragment" + currentFragment.toString());
    }


    private void initMap() {
        SDKInitializer.initialize(getApplicationContext());
    }
    private void initButtonBackground() {
        satelliteButton.setBackgroundResource(R.drawable.ic_flight_white_36dp);
        mapButton.setBackgroundResource(R.drawable.ic_map_white_36dp);
        goalButton.setBackgroundResource(R.drawable.ic_my_location_white_36dp);
        userButton.setBackgroundResource(R.drawable.ic_account_box_white_36dp);
    }

    public static String getDefaultTime() {
        return defaultTime;
    }

    public static void setDefaultTime(String defaultTime) {
        MainActivity.defaultTime = defaultTime;
    }

    public static String getDefaultHttpAddress() {
        return defaultHttpAddress;
    }

    public static void setDefaultHttpAddress(String defaultHttpAddress) {
        MainActivity.defaultHttpAddress = defaultHttpAddress;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(TAG, "onResume: MainActivity");
    }
}
