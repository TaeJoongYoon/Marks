package com.yoon.memoria.SignIn;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View view;

    public SignInPresenter(SignInContract.View view){
        this.view = view;
    }

    @Override
    public void call_sign_in(Activity activity, FirebaseAuth auth, String username, String password) {
        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if(user != null)
                                view.finishActivity();
                            else
                                view.failedActivity();
                        } else {
                            view.failedActivity();
                        }
                    }
                });
    }
}
