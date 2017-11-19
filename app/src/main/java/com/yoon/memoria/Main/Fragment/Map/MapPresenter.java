package com.yoon.memoria.Main.Fragment.Map;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapPresenter implements MapContract.Presenter {
    private MapContract.View view;

    public MapPresenter(MapContract.View view){
        this.view = view;
    }
}
