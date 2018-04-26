package com.example.satellite.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.bumptech.glide.Glide;
import com.example.satellite.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2018/4/25.
 */

public class PicFragment extends Fragment {
    private View view;
    private ImageView imageView;
    private ZoomControls zoomControls;
    private ImageView backImage;
    private TextView titleText;
    private String rootUrl;
    private String title;
    private int maxLeval;
    private int minLeval = 1;
    private int currentLevel = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pic_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.image);
        backImage = (ImageView) view.findViewById(R.id.back_image);
        zoomControls = (ZoomControls) view.findViewById(R.id.zoomcontrol);
        titleText = (TextView) view.findViewById(R.id.title_text);
        Bundle bundle = getArguments();
        rootUrl = bundle.getString("url", "");
        title = bundle.getString("city", "") + "遥感图像";
        //设置ImageView的初始显示图像
        Glide.with(this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == maxLeval) {
                    zoomControls.setIsZoomInEnabled(false);
                    Toast.makeText(getContext(), "已到最大级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                zoomControls.setIsZoomOutEnabled(true);
                currentLevel ++;
                Glide.with(PicFragment.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == minLeval) {
                    zoomControls.setIsZoomOutEnabled(false);
                    Toast.makeText(getContext(), "已是最小缩放级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                zoomControls.setIsZoomInEnabled(true);
                //Glide.with(PicFragment.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
                currentLevel --;
                Glide.with(PicFragment.this).load(rootUrl + currentLevel + ".jpg").placeholder(R.drawable.loading).into(imageView);
            }
        });
    }
}
