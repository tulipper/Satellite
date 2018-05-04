package com.example.satellite;

        import android.app.Application;
        import android.content.Context;

        import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2018/4/12.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }
    public static Context getContext() {
        return context;
    }
}
