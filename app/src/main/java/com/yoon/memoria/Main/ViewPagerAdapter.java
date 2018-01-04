package com.yoon.memoria.Main;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yoon.memoria.Main.Fragment.Map.MapFragment;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoFragment;
import com.yoon.memoria.Main.Fragment.Place.PlaceFragment;

/**
 * Created by Yoon on 2017-11-01.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    private MapFragment mapFragment;
    private PlaceFragment placeFragment;
    private MyInfoFragment myInfoFragment;

    public ViewPagerAdapter(FragmentManager fm, int tabCount,
                            MapFragment mapFragment,
                            PlaceFragment placeFragment,
                            MyInfoFragment myInfoFragment
                               ) {
        super(fm);
        this.tabCount = tabCount;
        this.mapFragment = mapFragment;
        this.placeFragment = placeFragment;
        this.myInfoFragment = myInfoFragment;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mapFragment;
            case 1:
                return placeFragment;
            case 2:
                return myInfoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public MapFragment getMapFragment(){
        return mapFragment;
    }

    public PlaceFragment getPlaceFragment(){
        return placeFragment;
    }

    public MyInfoFragment getMyInfoFragment(){
        return myInfoFragment;
    }


}
