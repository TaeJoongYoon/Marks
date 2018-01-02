package com.yoon.memoria.User;

/**
 * Created by Yoon on 2018-01-02.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View view;

    public UserPresenter(UserContract.View view){
        this.view = view;
    }
}
