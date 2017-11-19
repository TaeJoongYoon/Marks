package com.yoon.memoria.Splash;

/**
 * Created by Yoon on 2017-11-10.
 */

public class SplashContract {

    interface View{
        void toSignin();
        void toMain();
    }

    interface Presenter{
        void checkSigned();
    }
}
