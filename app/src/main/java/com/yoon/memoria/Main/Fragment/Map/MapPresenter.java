package com.yoon.memoria.Main.Fragment.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
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
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View view;
    private UidSingleton uidSingleton =UidSingleton.getInstance();

    private final static int MAXENTRIES = 5;
    private String[] LikelyPlaceNames = null;
    private String[] LikelyPlaceIDs = null;
    private String[] LikelyPlaceAddresses = null;

    public MapPresenter(MapContract.View view){
        this.view = view;
    }

    @Override
    public void markerAdd(GoogleMap googleMap, DataSnapshot dataSnapshot, List<Marker> markers) {
        Post post = dataSnapshot.getValue(Post.class);

        LatLng latLng = new LatLng(post.getLatitude(),post.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(dataSnapshot.getKey());
        markerOptions.snippet("POST");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(265));
        Marker marker = googleMap.addMarker(markerOptions);
        markers.add(marker);
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
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            return;
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onConnected(){
        view.onConnected();
    }
    @Override
    public void onConnectionFailed(){
        view.onConnectionFailed();
    }

    @Override
    public void onLocationChanged(GoogleApiClient googleApiClient, Location location){
        view.onLocationChanged(googleApiClient, location);
    }

    @Override
    public void setMyLocationEnable(){
        view.setMyLocationEnabled();
    }
    public void searchCurrentPlaces(GoogleApiClient googleApiClient,DatabaseReference databaseReference) {
        @SuppressWarnings("MissingPermission")
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>(){

            @Override
            public void onResult(PlaceLikelihoodBuffer placeLikelihoods) {
                int i = 0;
                LikelyPlaceNames = new String[MAXENTRIES];
                LikelyPlaceIDs = new String[MAXENTRIES];
                LikelyPlaceAddresses = new String[MAXENTRIES];

                for(PlaceLikelihood placeLikelihood : placeLikelihoods) {
                    LikelyPlaceNames[i] = placeLikelihood.getPlace().getName().toString();
                    LikelyPlaceIDs[i] = placeLikelihood.getPlace().getId();
                    LikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress().toString();

                    i++;
                    if(i > MAXENTRIES - 1 ) {
                        break;
                    }
                }
                placeLikelihoods.release();

                if (LikelyPlaceNames[0].equals(view.getPreviousPlace())){
                    setCurrentPlace(databaseReference, LikelyPlaceNames[0], LikelyPlaceIDs[0], LikelyPlaceAddresses[0]);
                    view.setPreviousPlace(null);
                }
                else
                    view.setPreviousPlace(LikelyPlaceNames[0]);
            }
        });
    }

    public void setCurrentPlace(DatabaseReference databaseReference, String name, String ID, String address) {
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

        String date = dateFormat.format(now);
        String detail = detailFormat.format(now);

        String KEY = databaseReference.child("users").child(uidSingleton.getUid()).child("places").push().getKey();
        Place place = new Place(KEY, name, ID, date, detail, address);
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(KEY).setValue(place);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels); view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
