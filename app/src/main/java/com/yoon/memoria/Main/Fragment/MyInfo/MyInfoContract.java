package com.yoon.memoria.Main.Fragment.MyInfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MyInfoContract {

    interface View{
        Presenter getPresenter();
    }

    public interface Presenter{
        List<CalendarDay> eventMark(List<String> events);
        void eventSetting(List<String> event);
        Intent toHistory(CalendarDay date, Activity activity);
    }
}
