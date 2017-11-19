package com.yoon.memoria.Posting;

/**
 * Created by Yoon on 2017-11-10.
 */

public class PostingPresenter implements PostingContract.Presenter {
    private PostingContract.View view;

    public PostingPresenter(PostingContract.View view){
        this.view = view;
    }
}
