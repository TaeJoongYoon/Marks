package com.yoon.memoria.Main.Fragment.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoon.memoria.Posting.PostingActivity;
import com.yoon.memoria.R;


public class MapFragment extends Fragment implements MapContract.View, OnMapReadyCallback {

    private MapPresenter presenter;

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

        //initMap();
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
        if(mapView != null)
            mapView.onCreate(savedInstanceState);

    }

    public void initToolbar(){
        toolbar = getView().findViewById(R.id.mapToolbar);
        toolbar.inflateMenu(R.menu.menu_map);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_map:
                    getActivity().startActivityForResult(new Intent(getActivity(), PostingActivity.class),1);
                    break;
            }
            return false;
        });
    }

    public void initMap(View v){
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.getMapAsync(this);
    }

    @Override
    public MapContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}