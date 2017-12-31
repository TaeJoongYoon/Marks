package com.yoon.memoria.Splash;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SplashContract {

    interface View{
        void toSignIn();
        void toMain();
    }

    interface Presenter{
        void check_signed(FirebaseUser user);
    }
}
