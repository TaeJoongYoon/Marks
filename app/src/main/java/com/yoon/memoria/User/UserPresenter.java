package com.yoon.memoria.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.yoon.memoria.Model.User;

/**
 * Created by Yoon on 2018-01-02.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View view;

    public UserPresenter(UserContract.View view){
        this.view = view;
    }

    @Override
    public void onFollowed(DatabaseReference postRef, String follow) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);

                if (user == null) {
                    return Transaction.success(mutableData);
                }


                if (user.getFollowing().containsKey(follow)) {
                    user.setFollowingCount(user.getFollowingCount()-1);
                    user.getFollowing().remove(follow);

                } else {
                    user.setFollowingCount(user.getFollowingCount()+1);
                    user.getFollowing().put(follow, true);
                }

                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }

}
