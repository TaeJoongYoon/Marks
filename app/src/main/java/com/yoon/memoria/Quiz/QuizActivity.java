package com.yoon.memoria.Quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.R;
import com.yoon.memoria.databinding.ActivityQuizBinding;


public class QuizActivity extends AppCompatActivity implements QuizContract.View {
    private ActivityQuizBinding binding;
    private QuizPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        binding.setActivity(this);

        presenter = new QuizPresenter(this);
    }
}
