package com.example.satellite;

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

public class MainActivity extends AppCompatActivity {
    public static String defaultHttpAddress = "http://192.168.1.103";
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

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.commit();
    }
    private void initMap() {
        SDKInitializer.initialize(getApplicationContext());
    }
}
