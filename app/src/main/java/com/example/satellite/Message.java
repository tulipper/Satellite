package com.example.satellite;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/4/23.
 */

public class Message extends BmobObject {
    private String location;
    private String picAddr;
    private String videoAddr;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicAddr() {
        return picAddr;
    }

    public void setPicAddr(String picAddr) {
        this.picAddr = picAddr;
    }

    public String getVideoAddr() {
        return videoAddr;
    }

    public void setVideoAddr(String videoAddr) {
        this.videoAddr = videoAddr;
    }
}
