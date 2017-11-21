package com.yoon.memoria.Main.Fragment.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MapContract {

    interface View{
        void toReading(Marker marker);
        Presenter getPresenter();
    }

    public interface Presenter{
        void markerSetting(GoogleMap googleMap);
    }
}
