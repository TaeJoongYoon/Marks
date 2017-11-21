package com.yoon.memoria.Main.Fragment.Map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoon.memoria.Posting.PostingActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.Util.Util;


public class MapFragment extends Fragment implements MapContract.View, OnMapReadyCallback {

    private MapPresenter presenter;
    public final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    public final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 2;
    private Toolbar toolbar;
    private MapView mapView = null;

    public MapFragment() {
        presenter = new MapPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        initMap(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolbar();
        if (mapView != null)
            mapView.onCreate(savedInstanceState);

    }

    public void initToolbar() {
        toolbar = getView().findViewById(R.id.mapToolbar);
        toolbar.inflateMenu(R.menu.menu_map);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_map:
                    getActivity().startActivityForResult(new Intent(getActivity(), PostingActivity.class), 1);
                    break;
            }
            return false;
        });
    }

    public void initMap(View v) {
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.getMapAsync(this);
    }

    @Override
    public void toReading(Marker marker) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra("title",marker.getTitle());
        intent.putExtra("location",marker.getPosition());
        startActivity(intent);
    }

    @Override
    public MapContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapSetting_runtimeAccess(googleMap);

        presenter.markerSetting(googleMap);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        googleMap.setOnMarkerClickListener(marker -> {
            toReading(marker);
            return true;
        });
    }

    public void mapSetting_runtimeAccess(GoogleMap googleMap){
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        //런타임 Access
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            }
        }
        //런타임 Access

        googleMap.setMyLocationEnabled(true);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Util.makeToast(getActivity(),"현재 위치 권한을 승인했습니다.");

                } else {
                    Util.makeToast(getActivity(),"현재 위치 권한을 거부했습니다.");
                }

                return;
            }
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Util.makeToast(getActivity(),"주변 위치 권한을 승인했습니다.");

                } else {
                    Util.makeToast(getActivity(),"주변 위치 권한을 거부했습니다.");
                }

                return;
            }

        }
    }
}