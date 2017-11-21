package com.yoon.memoria.History;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.R;


public class HistoryActivity extends AppCompatActivity implements HistoryContract.View {

    private HistoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        presenter = new HistoryPresenter(this);
    }
}
