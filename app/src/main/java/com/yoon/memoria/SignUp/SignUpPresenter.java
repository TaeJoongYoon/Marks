package com.yoon.memoria.SignUp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.StorageSingleton;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;

    private DatabaseReference databaseReference;
    private User userModel;

    public SignUpPresenter(SignUpContract.View view){
        this.view = view;
    }


    @Override
    public void call_sign_up(Activity activity, FirebaseAuth auth, String username, String password, String nickname) {
        auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            userModel = new User(user.getUid(),nickname,"NULL");

                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("users").child(user.getUid()).setValue(userModel);

                            if(user != null)
                                view.finishActivity();
                            else
                                view.password_not_satisfied();
                        } else {
                            view.password_not_satisfied();
                        }
                    }
                });
    }
}
