package com.yoon.memoria.Main.Fragment.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.EventBus.BusProvider;
import com.yoon.memoria.GoogleApiSingleton;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Posting.PostingActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.EventBus.ActivityResultEvent;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.FragmentMapBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MapFragment extends Fragment implements MapContract.View, OnMapReadyCallback, ValueEventListener{

    private FragmentMapBinding binding;
    private DatabaseReference databaseReference;
    private MapPresenter presenter;
    private MainActivity activity;
    private GoogleApiSingleton googleApiSingleton = GoogleApiSingleton.getInstance();

    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

    private PlaceAutocompleteFragment autocompleteFragment;

    private GoogleMap googleMap= null;
    private Marker currentMarker = null;

    private Location postLocation = null;
    private List<Marker> markers = new ArrayList<>(0);

    private String previousPlace = null;

    private CameraPosition mCameraPosition;
    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public MapFragment() {
        presenter = new MapPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusProvider.getInstance().register(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        activity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map,container,false);
        setHasOptionsMenu(true);

        binding.map.getMapAsync(this);

        databaseReference.child("posts").addValueEventListener(this);
        initGoogleSearch();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolbar();

        if (binding.map != null)
            binding.map.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.map.onStop();
    }

    @Override
    public void onDestroyView() {
        databaseReference.child("posts").removeEventListener(this);
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.map.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    @SuppressLint("MissingPermission")
    public void initToolbar() {
        binding.mapToolbar.inflateMenu(R.menu.menu_map);

        binding.mapToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_map:
                    postLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiSingleton.getGoogleApiClient());
                    if(postLocation == null)
                        Util.makeToast(getActivity(),"위치가 확인되지 않습니다");
                    else {
                        Intent intent = new Intent(getActivity(), PostingActivity.class);
                        intent.putExtra("latitude", postLocation.getLatitude());
                        intent.putExtra("longitude", postLocation.getLongitude());
                        getActivity().startActivityForResult(intent, Util.POST_CODE);
                    }
                    break;
            }
            return false;
        });
    }

    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Util.POST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Util.makeToast(getActivity(),"글쓰기 성공");
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Util.makeToast(getActivity(),"글쓰기 실패");
                }
                break;
        }
    }

    public void initGoogleSearch(){
        autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                if (currentMarker != null) currentMarker.remove();
                MarkerOptions markerOptions = presenter.setSearchLocation(location,place.getName().toString(),place.getAddress().toString());
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                currentMarker = googleMap.addMarker(markerOptions);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void toReading(Marker marker) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra("Uid",marker.getTitle());
        startActivity(intent);
    }

    @Override
    public MapContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public String getPreviousPlace() {
        return previousPlace;
    }

    @Override
    public void setPreviousPlace(String place) {
        this.previousPlace = place;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        presenter.setCurrentLocation(googleMap,DEFAULT_LOCATION, null);
        updateLocaionUI();
        getDeviceLocation();
    }


    public void updateLocaionUI(){
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMarkerClickListener(marker -> {
            if (marker.getSnippet().equals("POST"))
                toReading(marker);
            return true;
        });
        googleMap.setOnMapClickListener(latLng -> {
            if(currentMarker != null)
                currentMarker.remove();
        });
    }

    @SuppressLint("MissingPermission")
    public void getDeviceLocation(){
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiSingleton.getGoogleApiClient());
        if (mCameraPosition != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), 15));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        TedRx2Permission.with(getActivity())
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_location_message)
                .setDeniedMessage(R.string.rationale_denied_message)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .request()
                .subscribe(tedPermissionResult -> {
                    if (tedPermissionResult.isGranted()) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        activity.startLocationUpdates();
                    } else {
                        Util.makeToast(getActivity(), "권한 거부\n" + tedPermissionResult.getDeniedPermissions().toString());
                    }
                }, throwable -> {
                }, () -> {
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void setMyLocationEnabled(){
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onConnectionFailed() {
        Location location = new Location("");
        location.setLatitude(DEFAULT_LOCATION.latitude);
        location.setLongitude((DEFAULT_LOCATION.longitude));

        presenter.setCurrentLocation(googleMap,DEFAULT_LOCATION,location);
        Util.makeToast(getActivity(), "서비스와 연결이 끊겼습니다.");
    }

    @Override
    public void onLocationChanged(GoogleApiClient googleApiClient, Location location) {
        presenter.setCurrentLocation(googleMap,DEFAULT_LOCATION,location);
        if(location != null)
            presenter.searchCurrentPlaces(googleApiClient,databaseReference);
        Log.i(TAG, "onLocationChanged call..");
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        googleMap.clear();
        markers.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            presenter.markerAdd(googleMap,snapshot,markers);
        }
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}