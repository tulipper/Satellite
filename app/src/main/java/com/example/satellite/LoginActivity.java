package com.example.satellite;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
    private String email;
    private String password;
    private AutoCompleteTextView emailText;
    private EditText passwordText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView gotoSignup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"7932ba029f47be06be4a9ad39b356437");
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        emailText = (AutoCompleteTextView) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        gotoSignup = (TextView) findViewById(R.id.goto_signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordText.getText().toString();
                email = emailText.getText().toString();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                MyUser mu = new MyUser();
                mu.setUsername(email);
                mu.setPassword(password);
                mu.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        progressBar.setVisibility(View.GONE);
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}

