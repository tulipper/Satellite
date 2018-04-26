package com.example.satellite;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.example.satellite.fragment.MapFragment;
import com.example.satellite.fragment.SatelliteFragment;
import com.example.satellite.fragment.UserFragment;

import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity {
    public static String defaultHttpAddress = "http://192.168.1.115";
    Button satelliteButton;
    Button mapButton;
    Button goalButton;
    Button userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap();
        setContentView(R.layout.activity_main);
        satelliteButton = (Button)findViewById(R.id.satellite);
        mapButton = (Button) findViewById(R.id.map);
        goalButton = (Button) findViewById(R.id.goal);
        userButton = (Button) findViewById(R.id.user);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MapFragment());
            }
        });
        satelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SatelliteFragment());
            }
        });
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new UserFragment());
            }
        });
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new GoalFragment());
            }
        });

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.commit();
    }
    public void replaceFragmentToStack(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void finishFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }
    private void initMap() {
        SDKInitializer.initialize(getApplicationContext());
    }
    private void initButtonBackground() {

    }

}
