package com.example.satellite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2018/4/23.
 */

public class GoalFragment extends Fragment {
    private EditText cityEdit;
    private EditText timeEdit;
    private Button admitButton;
    private LinearLayout inputLayout;
    private View view;
    private MyUser currentUser;
    private ScrollView progressLayout;
    private TextView progressText;
    private String city;
    private String time;
    private String requestId;
    private Message message;
    private RelativeLayout buttonLayout;
    private Button seePicButton;
    private Button seeVideoButton;
    private Request mapRequest;
    private static final String TAG = "GoalFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.goal_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        cityEdit = (EditText) view.findViewById(R.id.input_city);
        timeEdit = (EditText) view.findViewById(R.id.input_time);
        admitButton = (Button) view.findViewById(R.id.admit);
        inputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        progressLayout = (ScrollView) view.findViewById(R.id.progress_layout);
        progressText = (TextView) view.findViewById(R.id.progress_text);
        buttonLayout = (RelativeLayout) view.findViewById(R.id.button_layout);
        seePicButton = (Button) view.findViewById(R.id.see_pic);
        seeVideoButton = (Button) view.findViewById(R.id.see_video);
        mapRequest = ((MainActivity)getActivity()).requestFromMap;
        if (mapRequest != null)
            cityEdit.setText(mapRequest.getLocation());
        seeVideoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (message != null) {
                    String url = message.getVideoAddr().startsWith("/") ?
                            MainActivity.getDefaultHttpAddress()+message.getVideoAddr() :
                            message.getVideoAddr();
                    String city = message.getLocation();
                    Intent intent = new Intent(getContext(), LiveActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("name", city);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "未获取到信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message != null) {
                    String url = MainActivity.getDefaultHttpAddress() + message.getPicAddr();
                    String city = message.getLocation();
                    int maxLeval = 9;
                    Intent intent  = new Intent (getActivity(), PicActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("city", city);
                    intent.putExtra("max_leval",9);
                    startActivity(intent);
                }
            }
        });
        admitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser == null) {
                    Toast.makeText(MyApplication.getContext(), "当前未登录，请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                city =  cityEdit.getText().toString();
                time = timeEdit.getText().toString();
                time = TextUtils.isEmpty(time)? MainActivity.getDefaultTime()  : time;
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(MyApplication.getContext(), "请输入观测城市", Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.VISIBLE);

                progressText.setText("正在检察观测区域的信息...\n");
                BmobQuery<Message> query = new BmobQuery<>();
                query.addWhereEqualTo("location", city);
                if (mapRequest != null)
                    query.addWhereEqualTo("location", "其他");
                query.setLimit(1);
                query.findObjects(new FindListener<Message>() {
                    @Override
                    public void done(List<Message> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0) {
                                progressText.append("目标区域不在观测区域内\n");
                                return;
                            }
                            progressText.append("目标区域在观测区域内\n");
                            message = list.get(0);
                            final Request request = new Request();
                            request.setMessage(message);
                            Toast.makeText(MyApplication.getContext(), "success!", Toast.LENGTH_SHORT).show();
                            request.setUser(currentUser);
                            request.setTime(time);
                            if (mapRequest != null) {
                                request.setLocation(mapRequest.getLocation());
                                request.setLatitude(mapRequest.getLatitude());
                                request.setLongitude(mapRequest.getLongitude());
                                request.setRadius(mapRequest.getRadius());
                            }
                            progressText.append("正在向服务器发送请求信息...\n");
                            request.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    //进度条隐藏
                                    if (e == null) {
                                        //Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
                                        //showProgress(s);
                                        progressText.append("发送成功! \n正在获取目标区域的信息...\n");
                                        requestId = s;
                                        progressText.append("准备就绪");
                                        buttonLayout.setVisibility(View.VISIBLE);

                                    } else {
                                        //Toast.makeText(MyApplication.getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        progressText.append("发送失败，请检测本地网络连接或稍后重试\n");
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(MyApplication.getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                //
                /*
                if (message != null) {
                    progressText.append("准备就绪");
                    buttonLayout.setVisibility(View.VISIBLE);

                }
                */


            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d(TAG, "onHiddenChanged: "  +"  GoalFragment Hide" );
        } else {
            //((MainActivity)getActivity()).onGoalButtonPressed();
            mapRequest = ((MainActivity)getActivity()).requestFromMap;
            if (cityEdit != null)
                cityEdit.setText(mapRequest == null ? "" : mapRequest.getLocation());
        }
        super.onHiddenChanged(hidden);
    }
}
