package com.example.satellite.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satellite.MyUser;
import com.example.satellite.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ModifyEmailFragment extends Fragment {
    private ImageView backImage;
    private ImageView doneImage;
    private EditText inputEmail;
    private View view;
    private TextView titleText;
    //private LinearLayout buttonLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_nickname, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //buttonLayout = (LinearLayout) view.findViewById(R.id.main_button_layout);
        //buttonLayout.setVisibility(View.GONE);
        backImage = (ImageView) view.findViewById(R.id.back_image);
        doneImage = (ImageView) view.findViewById(R.id.done_image);
        inputEmail = (EditText) view.findViewById(R.id.nick_name);
        titleText = (TextView) view.findViewById(R.id.title_text);
        titleText.setText("修改邮箱");
        inputEmail.setHint("请输入邮箱：");
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

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
                String email = inputEmail.getText().toString();
                if (TextUtils.isEmpty(email))
                    return;
                if (!isEmailValid(email)) {
                    Toast.makeText(getContext(), "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                if (currentUser == null) {
                    Toast.makeText(getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyUser newUser = new MyUser();
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.update(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "更新成功,请及时前往邮箱验证", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }
}
