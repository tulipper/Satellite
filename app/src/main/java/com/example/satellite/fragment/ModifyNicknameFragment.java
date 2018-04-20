package com.example.satellite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.satellite.MainActivity;
import com.example.satellite.MyUser;
import com.example.satellite.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2018/4/19.
 */

public class ModifyNicknameFragment extends Fragment {
    private ImageView backImage;
    private ImageView doneImage;
    private EditText inputNickName;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_nickname, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backImage = (ImageView) view.findViewById(R.id.back_image);
        doneImage = (ImageView) view.findViewById(R.id.done_image);
        inputNickName = (EditText) view.findViewById(R.id.nick_name);
        inputNickName.setHint("输入昵称");
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getActivity().onBackPressed();/*
                ((MainActivity)getActivity()).finishFragment(ModifyNicknameFragment.this);
                ((MainActivity)getActivity()).replaceFragment(new UserFragment());*/
            }
        });
        doneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = inputNickName.getText().toString();
                if (TextUtils.isEmpty(nickName))
                    return;

                MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyUser newUser = new MyUser();
                newUser.setNickName(nickName);
                newUser.update(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
