package com.example.satellite;


import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.satellite.fragment.MapFragment;
import com.example.satellite.fragment.SatelliteFragment;
import com.example.satellite.fragment.UserFragment;

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
    private Fragment currentFragment;
    private GoalFragment goalFragment;
    private SatelliteFragment satelliteFragment;
    private MapFragment mapFragment;
    private UserFragment userFragment;
    public Request requestFromMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap();
        setContentView(R.layout.activity_main);
        satelliteButton = (Button)findViewById(R.id.satellite);
        mapButton = (Button) findViewById(R.id.map);
        goalButton = (Button) findViewById(R.id.goal);
        userButton = (Button) findViewById(R.id.user);
        goalFragment = new GoalFragment();
        satelliteFragment = new SatelliteFragment();
        mapFragment = new MapFragment();
        userFragment = new UserFragment();
        currentFragment = mapFragment;
        initShow();
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initButtonBackground();
                mapButton.setBackgroundResource(R.drawable.ic_map_black_36dp);
                switchFragment(mapFragment);
            }
        });
        satelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initButtonBackground();
                satelliteButton.setBackgroundResource(R.drawable.ic_flight_black_36dp);
                switchFragment(satelliteFragment);
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initButtonBackground();
                userButton.setBackgroundResource(R.drawable.ic_account_box_black_36dp);
                switchFragment(userFragment);
            }
        });
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initButtonBackground();
                goalButton.setBackgroundResource(R.drawable.ic_my_location_black_36dp);
                switchFragment(goalFragment);
            }
        });

    }


    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frag_container, targetFragment)
                    .commit();
            Toast.makeText(this, "还没有添加过", Toast.LENGTH_SHORT).show();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
            //System.out.println("添加了( ⊙o⊙ )哇");
            Toast.makeText(this, "添加过了", Toast.LENGTH_SHORT).show();
        }
        currentFragment = targetFragment;
    }
    private void initShow() {
        satelliteButton.setVisibility(View.GONE);
        initButtonBackground();
        mapButton.setBackgroundResource(R.drawable.ic_map_black_36dp);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction
                .add(R.id.frag_container, currentFragment)
                .commit();
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
}
