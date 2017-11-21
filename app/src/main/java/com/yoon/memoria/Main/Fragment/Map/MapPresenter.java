package com.yoon.memoria.Main.Fragment.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View view;

    public MapPresenter(MapContract.View view){
        this.view = view;
    }

    @Override
    public void markerSetting(GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);

        LatLng SEOUL2 = new LatLng(38.56, 126.97);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(SEOUL2);
        markerOptions2.title("서울2");
        markerOptions2.snippet("수도");
        googleMap.addMarker(markerOptions2);

        LatLng SEOUL3 = new LatLng(39.56, 126.97);
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(SEOUL3);
        markerOptions3.title("서울3");
        markerOptions3.snippet("수도");
        googleMap.addMarker(markerOptions3);
    }
}
