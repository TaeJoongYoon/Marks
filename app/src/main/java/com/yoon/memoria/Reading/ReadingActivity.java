package com.yoon.memoria.Reading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yoon.memoria.R;

import org.w3c.dom.Text;

public class ReadingActivity extends AppCompatActivity implements ReadingContract.View{

    private ReadingPresenter presenter;

    private Intent intent;
    private TextView title;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        init();
        presenter = new ReadingPresenter(this);
    }

    public void init(){
        intent = getIntent();

        title = findViewById(R.id.title);
        title.setText(intent.getStringExtra("title"));

        location = findViewById(R.id.location);
        location.setText(intent.getStringExtra("location"));
    }
}
