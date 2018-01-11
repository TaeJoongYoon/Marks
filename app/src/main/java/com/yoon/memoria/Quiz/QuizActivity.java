package com.yoon.memoria.Quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

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

        initToolbar();
    }

    public void initToolbar(){
        setSupportActionBar(binding.quizToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_48dp);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
