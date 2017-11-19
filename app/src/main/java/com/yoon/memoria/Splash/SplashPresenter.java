package com.yoon.memoria.Splash;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;

    public SplashPresenter(SplashContract.View view){
        this.view = view;
    }

    @Override
    public void checkSigned() {
        if(true){
            view.toMain();
        }
        else{
            view.toSignin();
        }
    }
}
