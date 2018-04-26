package com.example.satellite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                if (currentLevel == maxLeval) {
                    zoomControls.setIsZoomInEnabled(false);
                    Toast.makeText(PicActivity.this, "已到最大级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                zoomControls.setIsZoomOutEnabled(true);
                currentLevel ++;
                Glide.with(PicActivity.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }


}
