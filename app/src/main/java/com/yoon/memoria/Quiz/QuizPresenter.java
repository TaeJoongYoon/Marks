package com.yoon.memoria.Quiz;

/**
 * Created by Yoon on 2017-11-10.
 */

public class QuizPresenter implements QuizContract.Presenter {
    private QuizContract.View view;

    public QuizPresenter(QuizContract.View view){
        this.view = view;
    }
}
