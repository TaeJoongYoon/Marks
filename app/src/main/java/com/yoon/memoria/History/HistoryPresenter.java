package com.yoon.memoria.History;

/**
 * Created by Yoon on 2017-11-10.
 */

public class HistoryPresenter implements HistoryContract.Presenter {
    private HistoryContract.View view;

    public HistoryPresenter(HistoryContract.View view){
        this.view = view;
    }
}
