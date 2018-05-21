package com.example.satellite;

import android.content.DialogInterface;

import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class LiveActivity extends BaseActivity {
    private ImageView backImage;
    private TextView titleText;
    private TextView timeText;
    private Handler handler;
    private VideoView liveVideo;
    private RelativeLayout loadingLayout;
    private String liveUrl;
    private RelativeLayout rootLayout;
    private LinearLayout topPanel;
    private LinearLayout bottomPanel;
    private ImageView playImage;
    private static final int RETRY_TIMES = 5;
    private static final int AUTO_HIDE_TIME = 5000;//ms
    private int counter = 0;
    private static final String TAG = "LiveActivity";

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
        playImage = (ImageView) findViewById(R.id.playImage);
        playImage.setOnClickListener(new View.OnClickListener() {
            long currentPosition = 0;
            long totalTime = 0;
            @Override
            public void onClick(View v) {

                if (liveVideo.isPlaying()) {
                    totalTime = liveVideo.getDuration();
                    currentPosition = liveVideo.getCurrentPosition();
                    Log.d(TAG, "onClick: when playing " + currentPosition);
                    if (totalTime == 0) {
                        liveVideo.stopPlayback();
                    } else {
                        liveVideo.pause();
                    }
                    //liveVideo.stopPlayback();
                    playImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    Log.d(TAG, "onClick: when other" + currentPosition);
                    playImage.setImageResource(R.drawable.ic_pause_circle_outline_white_36dp);
                    //liveVideo.setVideoURI(Uri.parse(liveUrl));
                    if (totalTime == 0) {
                        liveVideo.setVideoURI(Uri.parse(liveUrl));
                    } else {
                        liveVideo.seekTo(currentPosition);
                    }
                    liveVideo.start();
                    //liveVideo.resume();
                    /*
                    liveVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            liveVideo.start();
                        }
                    });
                    */
                }
            }
        });
        liveVideo = (VideoView) findViewById(R.id.video_view);
        liveVideo.setVideoURI(Uri.parse(liveUrl));
        //liveVideo.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
        liveVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                liveVideo.start();
                long time = mp.getDuration();
                time /= 1000;
                // Toast.makeText(LiveActivity.this, time/3600 + "h" + time%3600/60 + "S", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onPrepared: total time  " + time/3600 + "h" + time%3600/60 + "m" + time%3600%60 + "s");
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
                        break;

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
        rootLayout = (RelativeLayout) findViewById(R.id.activity_live);
        topPanel = (LinearLayout) findViewById(R.id.panel_up);
        bottomPanel = (LinearLayout) findViewById(R.id.panel_bottom);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topPanel.getVisibility() == View.VISIBLE || bottomPanel.getVisibility() == View.VISIBLE) {
                    topPanel.setVisibility(View.GONE);
                    bottomPanel.setVisibility(View.GONE);
                    return;
                }
                if (liveVideo.isPlaying())
                    playImage.setImageResource(R.drawable.ic_pause_circle_outline_white_36dp);
                else
                    playImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                topPanel.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.VISIBLE);
                 handler.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         topPanel.setVisibility(View.GONE);
                         bottomPanel.setVisibility(View.GONE);
                     }
                 }, AUTO_HIDE_TIME);
            }
        });
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

    @Override
    protected void onStop() {
        if (liveVideo != null) {
            liveVideo.stopPlayback();
        }
        counter = 0;
        super.onStop();
    }
}
