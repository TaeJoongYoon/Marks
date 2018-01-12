package com.yoon.memoria.Main.Fragment.Map;

import android.app.Activity;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapContract {

    interface View{
        Presenter getPresenter();
        String getPreviousPlace();
        void setPreviousPlace(String place);
        void setMyLocationEnabled();
        void onConnected();
        void onConnectionFailed();
        void onLocationChanged(GoogleApiClient googleApiClient, Location location);
    }

    public interface Presenter{
        void markerAdd(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers);
        MarkerOptions setSearchLocation(Location location, String markerTitle, String markerSnippet);
        void setCurrentLocation(GoogleMap googleMap, LatLng DEFAULT_LOCATION, Location location);
        void searchCurrentPlaces(GoogleApiClient googleApiClient, DatabaseReference databaseReference);
        void setMyLocationEnable();
        void onConnected();
        void onConnectionFailed();
        void onLocationChanged(GoogleApiClient googleApiClient, Location location);
    }
}
