package com.yoon.memoria.Main.Fragment.MyInfo;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MyInfoPresenter implements MyInfoContract.Presenter {
    private MyInfoContract.View view;

    public MyInfoPresenter(MyInfoContract.View view){
        this.view = view;
    }
}
