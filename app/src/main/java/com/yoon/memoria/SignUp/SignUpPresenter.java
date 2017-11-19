package com.yoon.memoria.SignUp;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;

    public SignUpPresenter(SignUpContract.View view){
        this.view = view;
    }

    public void SIGN_UP(){
        view.finishActivity();
    }
}
