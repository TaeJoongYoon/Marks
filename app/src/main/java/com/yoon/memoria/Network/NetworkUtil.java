package com.yoon.memoria.Network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yoon on 2017-11-19.
 */

public class NetworkUtil {
    public static String BASE_URL = "";
    public static String GOOGLE_URL = "";

    public static MemoriaService memoriaServiceRequest(){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(MemoriaService.class);
    }

    public static GoogleService googleServiceRequest(){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GOOGLE_URL)
                .build()
                .create(GoogleService.class);
    }
}
