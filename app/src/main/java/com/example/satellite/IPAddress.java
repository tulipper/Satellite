package com.example.satellite;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/17.
 */

public class IPAddress extends BmobObject {
    private String ipaddress;

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
}
