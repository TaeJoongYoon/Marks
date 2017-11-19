package com.yoon.memoria.Reading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yoon.memoria.R;

public class ReadingActivity extends AppCompatActivity implements ReadingContract.View{

    private ReadingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        presenter = new ReadingPresenter(this);
    }
}
