package com.example.satellite;

import android.content.DialogInterface;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;




import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class LiveActivity extends AppCompatActivity {
    private ImageView backImage;
    private TextView titleText;
    private TextView timeText;
    private Handler handler;
    private VideoView liveVideo;
    private RelativeLayout loadingLayout;
    private String liveUrl;
    private static final int RETRY_TIMES = 5;
    private int counter = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live);
        initView();
        initPlayer();

    }

    private void initPlayer() {
        Vitamio.isInitialized(getApplicationContext());
        liveVideo = (VideoView) findViewById(R.id.video_view);
        liveVideo.setVideoURI(Uri.parse(liveUrl));
        liveVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                liveVideo.start();
            }
        });
        liveVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (counter > RETRY_TIMES) {
                    new AlertDialog.Builder(LiveActivity.this)
                            .setMessage("播放出错")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LiveActivity.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    liveVideo.stopPlayback();
                    liveVideo.setVideoURI(Uri.parse(liveUrl));
                }
                counter ++;
                return false;
            }
        });
        liveVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        loadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        loadingLayout.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void initView() {
        liveUrl = getIntent().getStringExtra("url");
        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleText = (TextView) findViewById(R.id.panel_title_text);
        titleText.setText(getIntent().getStringExtra("name"));
        timeText = (TextView) findViewById(R.id.panel_system_time);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                        String str = sdf.format(new Date());
                        handler.sendMessage(handler.obtainMessage(100, str));
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
                timeText.setText((String) msg.obj);
            }
        };


    }



}
