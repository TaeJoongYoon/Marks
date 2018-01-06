package com.yoon.memoria.Main.Fragment.MyInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MyInfoContract {

    interface View{
        Presenter getPresenter();
        Context getAct();
        void nicknameEdit(EditText editText);
        void success();
        void failed();
    }

    public interface Presenter{
        List<CalendarDay> eventMark(List<String> events);
        void eventSetting(DatabaseReference databaseReference, List<String> event);
        Intent toHistory(CalendarDay date, Activity activity);
        void show();
        void profile_to_firebase(String filename);
        void fileUpload(Uri filePath);
    }
}
