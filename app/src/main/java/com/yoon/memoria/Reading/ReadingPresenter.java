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

    @Override
    public boolean inputData(DataSnapshot dataSnapshot, double latitude, double longitude) {
        Post post = dataSnapshot.getValue(Post.class);

        if(post.getLatitude() == latitude &&
                post.getLongitude() == longitude)
            return true;
        else
            return false;
    }
}
