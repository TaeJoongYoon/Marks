package com.yoon.memoria.Main.Fragment.Map;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View view;
    private List<MarkerOptions> markerOptions = new ArrayList<>(0);

    public MapPresenter(MapContract.View view){
        this.view = view;
    }

    @Override
    public void markerSetting(GoogleMap googleMap, DataSnapshot dataSnapshot) {
        Post post = dataSnapshot.getValue(Post.class);

        LatLng latLng = new LatLng(post.getLatitude(),post.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
    }

    @Override
    public MarkerOptions setSearchLocation(Location location, String markerTitle, String markerSnippet) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocation);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        return  markerOptions;
    }

    @Override
    public void setCurrentLocation(GoogleMap googleMap, LatLng DEFAULT_LOCATION, Location location, String markerTitle, String markerSnippet) {
        if (location != null) {
            //현재위치의 위도 경도 가져옴
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            return;
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
