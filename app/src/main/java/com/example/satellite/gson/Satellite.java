package com.example.satellite.gson;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Satellite extends DataSupport{
    private int id;
    private String name;
    private String url;
    private int satelliteId;

    public void setSatelliteId(int satelliteId) {
        this.satelliteId = satelliteId;
    }

    public int getSatelliteId() {
        return satelliteId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
