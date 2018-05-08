package com.example.satellite;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.bumptech.glide.Glide;
import com.example.satellite.fragment.PicFragment;

public class PicActivity extends AppCompatActivity {
    private ImageView imageCurrent;
    private ImageView imageHistory;
    private ZoomControls zoomControls;
    private FrameLayout currentLayout;
    private FrameLayout historyLayout;
    private String rootUrl;
    private String title;
    private int maxLeval;
    private int minLeval = 1;
    private int currentLevel = 1;
    private Toolbar toolbar;
    private  int WIDTH;
    private  int HEIGHT;
    private static final String TAG = "PicActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        Intent intent = getIntent();
        rootUrl = intent.getStringExtra("url");
        title = intent.getStringExtra("city");
        maxLeval = intent.getIntExtra("max_leval", 9);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.current:
                showCurrent();
                Toast.makeText(this, "current", Toast.LENGTH_SHORT).show();
                break;
            case R.id.history:
                showHistory();
                Toast.makeText(this, "history", Toast.LENGTH_SHORT).show();
                break;
            case R.id.compare:
                showCompare();
                Toast.makeText(this, "lalala", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    private void showHistory() {
        historyLayout.setVisibility(View.VISIBLE);
        currentLayout.setVisibility(View.GONE);
        ViewGroup.LayoutParams lps = historyLayout.getLayoutParams();
        lps.width = WIDTH;
        historyLayout.setLayoutParams(lps);
    }
    private void showCurrent() {
        currentLayout.setVisibility(View.VISIBLE);
        historyLayout.setVisibility(View.GONE);
        ViewGroup.LayoutParams lps = currentLayout.getLayoutParams();
        lps.width = WIDTH;
        currentLayout.setLayoutParams(lps);
    }

    private void showCompare() {
        historyLayout.setVisibility(View.VISIBLE);
        currentLayout.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lps = currentLayout.getLayoutParams();
        lps.width = WIDTH/2;
        currentLayout.setLayoutParams(lps);
        historyLayout.setLayoutParams(lps);
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageCurrent = (ImageView) findViewById(R.id.image_current);
        //backImage = (ImageView) findViewById(R.id.back_image);
        imageHistory = (ImageView) findViewById(R.id.image_history);
        currentLayout = (FrameLayout) findViewById(R.id.current_layout);
        historyLayout = (FrameLayout) findViewById(R.id.history_layout);
        imageCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PicActivity.this, "current", Toast.LENGTH_SHORT).show();
            }
        });
        imageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PicActivity.this, "history", Toast.LENGTH_SHORT).show();
            }
        });
        zoomControls = (ZoomControls) findViewById(R.id.zoomcontrol);


        //设置ImageView的初始显示图像
        Glide.with(this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageCurrent);
        Glide.with(this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageHistory);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
              }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
                   }
        });
        currentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrent();
            }
        });
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });
        imageCurrent.setLongClickable(true);
        imageCurrent.setOnTouchListener(new View.OnTouchListener() {
            private double currentdis;
            private double startdis = -1;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount() >= 2) {


                            currentdis = getDistance(event);
                            Log.d(TAG, "onTouch: ACTION_MOVE currentdis:" + currentdis);
                            if (currentdis - startdis > 500) {
                                int scale = (int) (currentdis - startdis)/500;
                                Log.d(TAG, "onTouch: 放大" + scale + "个级别");
                                //确定缩放等级，执行缩放操作
                                zoomIn();
                                startdis = currentdis;
                                return false;
                            } else if (startdis - currentdis > 500) {
                                int scale = (int) (startdis - currentdis)/500;
                                Log.d(TAG, "onTouch: 缩小" + scale + "个级别");
                                //确定缩放等级，执行缩放操作
                                zoomOut();
                                startdis = currentdis;
                                return false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_DOWN :
                        /*startPoint.set(event.getX(), event.getY());*/
                        Log.d(TAG, "onTouch: ACTION DOWN");
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN :
                        /*secondPoint.set(event.getX(), event.getY());*/
                        startdis = getDistance(event);
                        Log.d(TAG, "onTouch: ACTION_POINT_DOWN startDistance:" + startdis);


                        break;
                    default:
                        break;
                }
                return true;
            }
            private float getDistance (MotionEvent event) {
                float x = event.getX(1) - event.getX(0);
                float y = event.getY(1) - event.getY(0);
                float distance = (float) Math.sqrt(x * x + y * y);
                return distance;
            }
        });
    }
    private void zoomIn() {
        if (currentLevel == maxLeval) {
            zoomControls.setIsZoomInEnabled(false);
            Toast.makeText(PicActivity.this, "已到最大级别", Toast.LENGTH_SHORT).show();
            return;
        }
        zoomControls.setIsZoomOutEnabled(true);
        currentLevel ++;
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageCurrent);
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageHistory);
    }
    private void zoomOut() {
        if (currentLevel == minLeval) {
            zoomControls.setIsZoomOutEnabled(false);
            Toast.makeText(PicActivity.this, "已是最小缩放级别", Toast.LENGTH_SHORT).show();
            return;
        }
        zoomControls.setIsZoomInEnabled(true);
        //Glide.with(PicFragment.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
        currentLevel --;
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageCurrent);
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageHistory);
    }


}
