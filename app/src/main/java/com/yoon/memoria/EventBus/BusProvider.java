package com.yoon.memoria.EventBus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Yoon on 2017-12-20.
 */

public class BusProvider extends Bus{
    private static BusProvider instance;

    public static BusProvider getInstance() {
        if (instance == null) {
            synchronized (BusProvider.class) {
                if (instance == null) {
                    instance = new BusProvider();
                }
            }
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    BusProvider.super.post(event);
                }
            });
        }
    }
}
