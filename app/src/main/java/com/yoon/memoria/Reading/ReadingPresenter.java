package com.yoon.memoria.Reading;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.yoon.memoria.Model.Post;

/**
 * Created by Yoon on 2017-11-10.
 */

public class ReadingPresenter implements ReadingContract.Presenter {
    private ReadingContract.View view;

    public ReadingPresenter(ReadingContract.View view){
        this.view = view;
    }

}
