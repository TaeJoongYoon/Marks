package com.yoon.memoria.FollowList;

/**
 * Created by Yoon on 2018-01-07.
 */

public class FollowListPresenter implements FollowListContract.Presenter {
    private FollowListContract.View view;

    public FollowListPresenter(FollowListContract.View view){
        this.view = view;
    }
}
