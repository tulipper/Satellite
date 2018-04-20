package com.example.satellite.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.satellite.MyUser;
import com.example.satellite.R;

import java.sql.Time;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;



/**
 * Created by Administrator on 2018/4/20.
 */

public class VerifyPhoneFragment extends Fragment {
    private String userPhone = null;
    private boolean isSend = false;
    private View view;
    private ImageView backImage;
    private EditText inputNumber;
    private EditText inputPassword;
    private Button admitButton;
    private Button sendButton;
    TimeCount time ;
    private static final String TAG = "VerifyPhoneFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.verify_phone, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        time  = new TimeCount(60000, 1000);
        backImage = (ImageView) view.findViewById(R.id.back_image);
        inputNumber = (EditText) view.findViewById(R.id.login_input_phone_et);
        inputPassword = (EditText) view.findViewById(R.id.login_input_code_et);
        admitButton = (Button) view.findViewById(R.id.login_commit_btn);
        sendButton = (Button) view.findViewById(R.id.login_request_code_btn);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getActivity().onBackPressed();/*
                ((MainActivity)getActivity()).finishFragment(ModifyNicknameFragment.this);
                ((MainActivity)getActivity()).replaceFragment(new UserFragment());*/
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = inputNumber.getText().toString();
                if (!isPhoneValid(phone)) {
                    Toast.makeText(getContext(), "请输入正确的手机好吗", Toast.LENGTH_SHORT).show();
                    return;
                }
                userPhone = phone;
                BmobSMS.requestSMSCode(phone, "绑定验证码", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            isSend = true;
                            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            time.start();
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.getMessage().toString());
                        }
                    }
                });

            }
        });
        admitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (userPhone == null) {
                    Toast.makeText(getContext(), "请先发送验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputPassword.getText().toString();
                if (!isCodeValie(code)) {
                    Toast.makeText(getContext(), "请输入六位的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobSMS.verifySmsCode(userPhone, code, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            MyUser user =new MyUser();
                            user.setMobilePhoneNumber(userPhone);
                            user.setMobilePhoneNumberVerified(true);
                            MyUser cur = BmobUser.getCurrentUser(MyUser.class);
                            user.update(cur.getObjectId(),new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getContext(), "绑定成功，以后可以用手机号码登录", Toast.LENGTH_LONG).show();
                                        getActivity().onBackPressed();
                                    }else{
                                        Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private boolean isPhoneValid(String phone) {
        return( !TextUtils.isEmpty(phone) && (phone.length() == 11) && (phone.startsWith("1")));
    }
    private boolean isCodeValie(String code) {
        return (!TextUtils.isEmpty(code) && (code.length() == 6));
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            sendButton.setText("重新验证");
            sendButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            sendButton.setClickable(false);
            sendButton.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
