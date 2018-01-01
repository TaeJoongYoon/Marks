package com.yoon.memoria.Main.Fragment.Map;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View view;

    public MapPresenter(MapContract.View view){
        this.view = view;
    }

    @Override
    public void markerAdd(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers) {
        Post post = dataSnapshot.getValue(Post.class);

        LatLng latLng = new LatLng(post.getLatitude(),post.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(post.getUid());
        Marker marker = googleMap.addMarker(markerOptions);
        markers.add(marker);
    }

    @Override
    public void markerRemove(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers) {
        Post post = dataSnapshot.getValue(Post.class);

        for (Marker marker : markers){
            if(marker.getTitle().equals(post.getUid()))
                marker.remove();
        }
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
    public void setCurrentLocation(GoogleMap googleMap, LatLng DEFAULT_LOCATION, Location location) {
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

    @Override
    public void setCurrentPlace(DatabaseReference databaseReference, String name, String ID) {
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm");

        String date = dateFormat.format(now);
        String detail = detailFormat.format(now);

        Place place = new Place(date, name, ID, detail);
        databaseReference.child("users").child(getUid()).child("places").push().setValue(place);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
