package com.yoon.memoria.Quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.R;


public class QuizActivity extends AppCompatActivity implements QuizContract.View {

    private QuizPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        presenter = new QuizPresenter(this);
    }
}
