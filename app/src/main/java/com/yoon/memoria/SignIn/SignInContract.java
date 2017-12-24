package com.yoon.memoria.SignIn;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SignInContract {

    interface View{
        void finishActivity();
        void failedActivity();
    }

    interface Presenter{
        void call_sign_in(Activity activity, FirebaseAuth auth, String username, String password);
    }
}
