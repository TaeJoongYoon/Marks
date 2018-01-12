package com.yoon.memoria.Reading;

import android.provider.Contacts;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.UidSingleton;

/**
 * Created by Yoon on 2017-11-10.
 */

public class ReadingPresenter implements ReadingContract.Presenter {
    private ReadingContract.View view;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    public ReadingPresenter(ReadingContract.View view){
        this.view = view;
    }

    @Override
    public void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post= mutableData.getValue(Post.class);

                if (post == null) {

                    return Transaction.success(mutableData);
                }

                if (post.getLikes().containsKey(uidSingleton.getUid())) {
                    post.setLikeCount(post.getLikeCount()-1);
                    post.getLikes().remove(uidSingleton.getUid());

                } else {
                    post.setLikeCount(post.getLikeCount()+1);
                    post.getLikes().put(uidSingleton.getUid(), true);
                }

                mutableData.child("likes").setValue(post.getLikes());
                mutableData.child("likeCount").setValue(post.getLikeCount());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                view.onCompleted(dataSnapshot);
            }
        });
    }
}
