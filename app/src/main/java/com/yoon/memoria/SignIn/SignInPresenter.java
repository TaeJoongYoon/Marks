package com.yoon.memoria.SignIn;


/**
 * Created by Yoon on 2017-11-10.
 */

public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View view;

    public SignInPresenter(SignInContract.View view){
        this.view = view;
    }

    public void SIGN_IN(String username, String password){
        view.finishActivity();
    }
}
