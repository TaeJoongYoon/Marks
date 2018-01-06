package com.yoon.memoria.User;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Yoon on 2018-01-02.
 */

public class UserContract {
    interface View{

    }

    interface Presenter{
       void onFollowed(DatabaseReference postRef, String follow);
    }
}
