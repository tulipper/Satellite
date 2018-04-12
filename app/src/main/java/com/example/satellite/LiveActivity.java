package com.example.satellite;

import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveActivity extends AppCompatActivity {
    private ImageView backImage;
    private TextView titleText;
    private TextView timeText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        initView();
    }

    private void initView() {
        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleText = (TextView) findViewById(R.id.panel_title_text);
        titleText.setText(getIntent().getStringExtra("name"));
        timeText=(TextView) findViewById(R.id.panel_system_time);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                        String str=sdf.format(new Date());
                        handler.sendMessage(handler.obtainMessage(100,str));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                timeText.setText((String)msg.obj);
            }
        };


    }


}
