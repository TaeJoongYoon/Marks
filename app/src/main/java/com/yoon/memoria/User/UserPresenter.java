package com.yoon.memoria.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2018-01-02.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View view;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    public UserPresenter(UserContract.View view){
        this.view = view;
    }


    public void onFollowClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);

                if (user == null) {

                    return Transaction.success(mutableData);
                }


                if (user.getFollower().containsKey(uidSingleton.getUid())) {
                    user.setFollowerCount(user.getFollowerCount()-1);
                    user.getFollower().remove(uidSingleton.getUid());

                } else {
                    user.setFollowerCount(user.getFollowerCount()+1);
                    user.getFollower().put(uidSingleton.getUid(), true);
                }

                mutableData.child("follower").setValue(user.getFollower());
                mutableData.child("followerCount").setValue(user.getFollowerCount());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                view.onCompleted(dataSnapshot);
            }
        });
    }

    @Override
    public void onFollowed(DatabaseReference postRef, DataSnapshot followRef, DatabaseReference databaseReference) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);

                if (user == null) {
                    return Transaction.success(mutableData);
                }


                if (user.getFollowing().containsKey(followRef.getKey())) {
                    user.setFollowingCount(user.getFollowingCount()-1);
                    user.getFollowing().remove(followRef.getKey());

                } else {
                    user.setFollowingCount(user.getFollowingCount()+1);
                    user.getFollowing().put(followRef.getKey(), true);
                }


                mutableData.child("following").setValue(user.getFollowing());
                mutableData.child("followingCount").setValue(user.getFollowingCount());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
            }
        });
    }
}
