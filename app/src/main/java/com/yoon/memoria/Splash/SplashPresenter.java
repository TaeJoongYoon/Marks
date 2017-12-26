package com.yoon.memoria.Splash;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.yoon.memoria.MySingleton;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;

    public SplashPresenter(SplashContract.View view){
        this.view = view;
    }

    @Override
    public void check_signed(FirebaseUser user, DatabaseReference databaseReference) {
        if(user != null) {
            view.toMain();
        }
        else
            view.toSignin();
    }
}
