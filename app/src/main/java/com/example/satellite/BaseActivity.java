package com.example.satellite;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Created by Administrator on 2018/4/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
