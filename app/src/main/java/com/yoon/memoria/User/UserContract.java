package com.yoon.memoria.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Yoon on 2018-01-02.
 */

public class UserContract {
    interface View{
        void onCompleted(DataSnapshot dataSnapshot);
    }

    interface Presenter{
        void onFollowClicked(DatabaseReference postRef);
        void onFollowed(DatabaseReference postRef, DataSnapshot followRef, DatabaseReference databaseReference);
    }
}
