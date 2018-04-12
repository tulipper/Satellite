package com.example.satellite.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.satellite.MainActivity;
import com.example.satellite.gson.Satellite;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Utility {
    public static ArrayList<Satellite> handleSatelliteResponse (String response) {
        if ( !TextUtils.isEmpty(response)) {
            try {
                ArrayList<Satellite> satelliteList = new ArrayList<>();
                JSONArray allSatellite = new JSONArray(response);
                for ( int i = 0; i < allSatellite.length(); i++) {
                    JSONObject satelliteObject = allSatellite.getJSONObject(i);
                    Satellite satellite = new Satellite();
                    satellite.setId(satelliteObject.getInt("id"));
                    satellite.setName(satelliteObject.getString("name"));
                    satellite.setUrl(satelliteObject.getString("url"));
                    satelliteList.add(satellite);

                }

                return satelliteList;
            }catch (Exception e) {
                //提示解析失败
                e.printStackTrace();
            }

        }

        return null;

    }
}
