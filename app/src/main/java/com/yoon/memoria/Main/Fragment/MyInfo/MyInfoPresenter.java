package com.yoon.memoria.Main.Fragment.MyInfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.yoon.memoria.History.HistoryActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MyInfoPresenter implements MyInfoContract.Presenter {
    private MyInfoContract.View view;
    private ArrayList<CalendarDay> dates = new ArrayList<>();

    public MyInfoPresenter(MyInfoContract.View view){
        this.view = view;
    }

    @Override
    public void eventSetting(DatabaseReference databaseReference, List<String> event) {
        databaseReference.child("users").child(getUid()).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String date = dataSnapshot.child("date").getValue(String.class);
                event.add(date);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Intent toHistory(CalendarDay date, Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        intent.putExtra("year",date.getYear());
        intent.putExtra("month",date.getMonth()+1);
        intent.putExtra("day",date.getDay());

        return intent;
    }

    @Override
    public List<CalendarDay> eventMark(List<String> events) {
        Calendar calendar = Calendar.getInstance();
        for(String event : events){
            String[] event_time = event.split(".");
            int event_year = Integer.parseInt(event_time[0]);
            int event_month = Integer.parseInt(event_time[1]);
            int event_day = Integer.parseInt(event_time[2]);

            calendar.set(event_year,event_month-1,event_day);
            CalendarDay day = CalendarDay.from(calendar);

            dates.add(day);
        }
        return  dates;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
