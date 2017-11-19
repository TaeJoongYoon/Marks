package com.yoon.memoria.Reading;

/**
 * Created by Yoon on 2017-11-10.
 */

public class ReadingPresenter implements ReadingContract.Presenter {
    private ReadingContract.View view;

    public ReadingPresenter(ReadingContract.View view){
        this.view = view;
    }
}
