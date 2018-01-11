package com.yoon.memoria;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GoogleService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private IBinder binder = new MyBinder();
    private GoogleApiSingleton googleApiSingleton = GoogleApiSingleton.getInstance();

    private static final int UPDATE_INTERVAL_MS = 1000 * 60 * 20;           // 20분
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 60 * 20;   // 20분
    private LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    public class MyBinder extends Binder {
        public GoogleService getService(){
            return GoogleService.this;
        }
    }

    public GoogleService() {
    }

    public interface Callback {
        void onLocationChanged(Location location);
        void onConnected(@Nullable Bundle bundle);
        void onConnectionSuspended(int cause);
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    private Callback callback;

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        GoogleApiSingleton.setGoogleApiClient(this,this,this,this);
        googleApiSingleton.getGoogleApiClient().connect();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if ( googleApiSingleton.getGoogleApiClient().isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiSingleton.getGoogleApiClient(),this);
            googleApiSingleton.getGoogleApiClient().disconnect();
        }
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        callback.onLocationChanged(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        callback.onConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        callback.onConnectionSuspended(cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        callback.onConnectionFailed(connectionResult);
    }

    @SuppressLint("MissingPermission")
    public void request(){
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiSingleton.getGoogleApiClient(), locationRequest, this);
    }
}
