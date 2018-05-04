package com.example.satellite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.satellite.MainActivity;
import com.example.satellite.R;

/**
 * Created by Administrator on 2018/5/4.
 */

public class SettingFragment extends Fragment {
    private ImageView backImage;
    private ImageView doneImage;
    private EditText addressInput;
    private EditText timeInput;
    private Switch defaultSwitch;
    private Button clearButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        showCurrentSetting();
    }

    private void showCurrentSetting() {
        addressInput.setText(MainActivity.getDefaultHttpAddress());
        timeInput.setText(MainActivity.getDefaultTime());
    }

    private void initView() {
        backImage = (ImageView) view.findViewById(R.id.back_image);
        doneImage = (ImageView) view.findViewById(R.id.back_image);
        addressInput = (EditText) view.findViewById(R.id.ip_address);
        timeInput = (EditText) view.findViewById(R.id.time);
        defaultSwitch = (Switch) view.findViewById(R.id.default_switch);
        clearButton = (Button) view.findViewById(R.id.clear);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
