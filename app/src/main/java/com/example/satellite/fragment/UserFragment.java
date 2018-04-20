package com.example.satellite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satellite.ActivityCollector;
import com.example.satellite.LoginActivity;
import com.example.satellite.MainActivity;
import com.example.satellite.MyUser;
import com.example.satellite.R;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/4/19.
 */

public class UserFragment extends Fragment {
    MyUser currentUser;
    Button logoutButton;
    private TextView nickNameText;
    private TextView emailText;
    private TextView phoneText;
    private ImageView picImageView;
    private RelativeLayout nickNameItem;
    private RelativeLayout emailItem;
    private RelativeLayout phoneItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.user_fragment, container, false);
        logoutButton = (Button)view.findViewById(R.id.logout);
        nickNameText = (TextView) view.findViewById(R.id.nick_name);
        emailText = (TextView) view.findViewById(R.id.email);
        phoneText = (TextView)view.findViewById(R.id.phone);
        picImageView = (ImageView)view.findViewById(R.id.user_pic);
        nickNameItem = (RelativeLayout) view.findViewById(R.id.nick_name_item);
        emailItem = (RelativeLayout) view.findViewById(R.id.email_item);
        phoneItem = (RelativeLayout) view.findViewById(R.id.phone_item);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        showUserInfo();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    currentUser.logOut();
                    Intent intent = new Intent (getContext(), LoginActivity.class);
                    ActivityCollector.finishAll();
                    startActivity(intent);

                }
            }
        });
        nickNameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceFragmentToStack(new ModifyNicknameFragment());
            }
        });
        emailItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceFragmentToStack(new ModifyEmailFragment());
            }
        });
        phoneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceFragmentToStack(new VerifyPhoneFragment());
            }
        });
    }

    private void showUserInfo() {
        if (currentUser == null)
            return;
        nickNameText.setText(currentUser.getNickName() == null? "未设置" : currentUser.getNickName());
        emailText.setText(currentUser.getEmail() == null? "未设置" : currentUser.getEmail());
        phoneText.setText(currentUser.getMobilePhoneNumber() == null? "未绑定" : currentUser.getMobilePhoneNumber());
    }
    public void replaceFragmentToStack(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
