package com.yoon.memoria.Main.Fragment.Place;


/**
 * Created by Yoon on 2017-11-10.
 */

public class PlacePresenter implements PlaceContract.Presenter {
    private PlaceContract.View view;

    public PlacePresenter(PlaceContract.View view){
        this.view = view;
    }
}
