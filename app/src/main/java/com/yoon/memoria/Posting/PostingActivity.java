package com.yoon.memoria.Posting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.R;

public class PostingActivity extends AppCompatActivity implements PostingContract.View {

    private PostingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        presenter = new PostingPresenter(this);
    }
}
