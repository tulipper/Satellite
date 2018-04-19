package com.example.satellite;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;

import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends BaseActivity {
    private ImageView backImage;
   private Button signupButton;
    private AutoCompleteTextView mEmailView ;
    private EditText mPasswordView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.sigu_up_email);
        mPasswordView = (EditText) findViewById(R.id.sign_up_password);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        signupButton = (Button) findViewById(R.id.email_sign_in_button);
        backImage = (ImageView) findViewById(R.id.back_image);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = mPasswordView.getText().toString();
                String email = mEmailView.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isEmailValid(email) && isPasswordValid(password)){
                    mProgressBar.setVisibility(View.VISIBLE);
                   final MyUser mu = new MyUser();
                    mu.setPassword(password);
                    mu.setUsername(email);
                    mu.setEmail(email);
                    mu.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            mProgressBar.setVisibility(View.GONE);
                            if (e == null) {
                                //注册成功,自动登录
                                mu.login(new SaveListener<MyUser>() {
                                    @Override
                                    public void done(MyUser myUser, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(SignupActivity.this, "注册成功，已自动登录", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SignupActivity.this, "注册成功，请重新登录", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else  {
                    Toast.makeText(SignupActivity.this, "账号或密码不符合要求", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



}

