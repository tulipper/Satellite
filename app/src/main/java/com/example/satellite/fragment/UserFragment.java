package com.example.satellite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.example.satellite.GoalFragment;
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
    Button settingButton;
    private View view;
    private TextView nickNameText;
    private TextView emailText;
    private TextView phoneText;
    private ImageView picImageView;
    private RelativeLayout nickNameItem;
    private RelativeLayout emailItem;
    private RelativeLayout phoneItem;
    private static final String TAG = "MainActivity";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        showUserInfo();

    }

    private void initView() {
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        settingButton = (Button) view.findViewById(R.id.setting);
        logoutButton = (Button)view.findViewById(R.id.logout);
        nickNameText = (TextView) view.findViewById(R.id.nick_name);
        emailText = (TextView) view.findViewById(R.id.email);
        phoneText = (TextView)view.findViewById(R.id.phone);
        picImageView = (ImageView)view.findViewById(R.id.user_pic);
        nickNameItem = (RelativeLayout) view.findViewById(R.id.nick_name_item);
        emailItem = (RelativeLayout) view.findViewById(R.id.email_item);
        phoneItem = (RelativeLayout) view.findViewById(R.id.phone_item);
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
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceFragmentToStack(new SettingFragment());
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
        ((MainActivity) getActivity()).currentFragment = fragment;
        Log.d(TAG, "replaceFragmentToStack: " + ((MainActivity) getActivity()).currentFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).currentFragment = this;
        if (!this.isAdded()) {
            ((MainActivity) getActivity()).currentFragments.add(this);
        }
        Log.d(TAG, "onResume: " + ((MainActivity) getActivity()).currentFragment.toString());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d(TAG, "onHiddenChanged: UserFragment" + " hidden");
        } else {
            //((MainActivity) getActivity()).currentFragment = this;
            Log.d(TAG, "onHiddenChanged: " + "UserFragment unHidden");
        }
    }
}
