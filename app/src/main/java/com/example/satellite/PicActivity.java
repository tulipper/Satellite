package com.example.satellite;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.bumptech.glide.Glide;
import com.example.satellite.fragment.PicFragment;

public class PicActivity extends AppCompatActivity {
    private ImageView imageView;
    private ZoomControls zoomControls;
    private ImageView backImage;
    private TextView titleText;
    private String rootUrl;
    private String title;
    private int maxLeval;
    private int minLeval = 1;
    private int currentLevel = 1;
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

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image);
        backImage = (ImageView) findViewById(R.id.back_image);
        zoomControls = (ZoomControls) findViewById(R.id.zoomcontrol);
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(title + "遥感图像");
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //设置ImageView的初始显示图像
        Glide.with(this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
               /* if (currentLevel == maxLeval) {
                    zoomControls.setIsZoomInEnabled(false);
                    Toast.makeText(PicActivity.this, "已到最大级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                zoomControls.setIsZoomOutEnabled(true);
                currentLevel ++;
                Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);*/
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
                /*if (currentLevel == minLeval) {
                    zoomControls.setIsZoomOutEnabled(false);
                    Toast.makeText(PicActivity.this, "已是最小缩放级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                zoomControls.setIsZoomInEnabled(true);
                //Glide.with(PicFragment.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
                currentLevel --;
                Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);*/
            }
        });
        imageView.setLongClickable(true);
        imageView.setOnTouchListener(new View.OnTouchListener() {
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
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
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
        Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
    }


}
