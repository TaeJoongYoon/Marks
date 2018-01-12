package com.yoon.memoria;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by Yoon on 2018-01-12.
 */

public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();

        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
