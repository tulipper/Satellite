package com.example.satellite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private RelativeLayout addressItem;
    private RelativeLayout timeItem;

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
        if (MainActivity.isUseDefault()) {
            showDefaultSetting();
        } else {
            addressInput.setText(MainActivity.getDefaultHttpAddress());
            timeInput.setText(MainActivity.getDefaultTime());
        }
    }

    private void initView() {
        backImage = (ImageView) view.findViewById(R.id.back_image);
        doneImage = (ImageView) view.findViewById(R.id.done_image);
        addressInput = (EditText) view.findViewById(R.id.ip_address);
        timeInput = (EditText) view.findViewById(R.id.time);
        defaultSwitch = (Switch) view.findViewById(R.id.default_switch);
        clearButton = (Button) view.findViewById(R.id.clear);
        timeItem = (RelativeLayout) view.findViewById(R.id.modify_time_item);
        addressItem = (RelativeLayout) view.findViewById(R.id.modify_address_item);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        defaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   showDefaultSetting();
                } else {
                    //Toast.makeText(getContext(), "关闭了", Toast.LENGTH_SHORT).show();
                    addressInput.setFocusable(true);
                    addressInput.setFocusableInTouchMode(true);
                    timeInput.setFocusable(true);
                    timeInput.setFocusableInTouchMode(true);
                    //timeInput.requestFocus();
                }
            }
        });
        doneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultSwitch.isChecked()) {
                    MainActivity.setUseDefault(true);
                    MainActivity.setDefaultHttpAddress("http://192.168.1.115");
                    MainActivity.setDefaultTime("5");
                    //Toast.makeText(getContext(), "打开了", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();

                } else {
                    MainActivity.setUseDefault(false);
                    String address = addressInput.getText().toString();
                    String time = timeInput.getText().toString();
                    MainActivity.setDefaultHttpAddress(address == null ? MainActivity.getDefaultHttpAddress() : address);
                    MainActivity.setDefaultTime(time == null ? MainActivity.getDefaultTime() : time);
                   // Toast.makeText(getContext(), "关闭了", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    //Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getContext()).clearDiskCache();
                    }
                }).start();
                Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showDefaultSetting() {
        defaultSwitch.setChecked(true);
        addressInput.setText(MainActivity.getDefaultHttpAddress());
        addressInput.setFocusable(false);
        addressInput.setFocusableInTouchMode(false);
        timeInput.setText(MainActivity.getDefaultTime());
        timeInput.setFocusable(false);
        timeInput.setFocusableInTouchMode(false);
    }
}
