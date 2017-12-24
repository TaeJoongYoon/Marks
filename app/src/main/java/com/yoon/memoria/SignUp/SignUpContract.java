package com.yoon.memoria.SignUp;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by Yoon on 2017-11-10.
 */

public class SignUpContract {

    interface View{
        void password_not_satisfied();
        void finishActivity();
    }

    interface Presenter{
        void call_sign_up(Activity activity, FirebaseAuth auth, String username, String password, String nickname);
    }
}
