package com.yoon.memoria;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import javax.sql.ConnectionEventListener;

/**
 * Created by Yoon on 2018-01-10.
 */

public class GoogleApiSingleton {
    private static GoogleApiSingleton googleApiSingleton;
    private static GoogleApiClient googleApiClient;

    public static GoogleApiSingleton getInstance(){
        if (googleApiSingleton == null)
            googleApiSingleton = new GoogleApiSingleton();
        return googleApiSingleton;
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    public static void setGoogleApiClient(Context context,
                                   GoogleApiClient.ConnectionCallbacks connectionCallbacks,
                                   GoogleApiClient.OnConnectionFailedListener connectionFailedListener,
                                   LocationListener locationListener){
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }
}
