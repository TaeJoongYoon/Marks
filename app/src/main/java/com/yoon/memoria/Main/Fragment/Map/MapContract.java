package com.yoon.memoria.Main.Fragment.Map;

import android.location.Location;

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
    }

    public interface Presenter{
        void markerAdd(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers);
        void markerRemove(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers);
        MarkerOptions setSearchLocation(Location location, String markerTitle, String markerSnippet);
        void setCurrentLocation(GoogleMap googleMap, LatLng DEFAULT_LOCATION, Location location);
    }
}
