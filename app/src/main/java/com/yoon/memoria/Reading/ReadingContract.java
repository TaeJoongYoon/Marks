package com.yoon.memoria.Reading;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.yoon.memoria.Model.Post;

/**
 * Created by Yoon on 2017-11-10.
 */

public class ReadingContract {

    interface View{

    }

    interface Presenter{
        boolean inputData(DataSnapshot dataSnapshot, double latitude, double longitude);
    }
}
