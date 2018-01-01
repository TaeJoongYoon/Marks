package com.yoon.memoria.History;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityHistoryBinding;


public class HistoryActivity extends AppCompatActivity implements HistoryContract.View {
    private ActivityHistoryBinding binding;
    private HistoryPresenter presenter;

    private Intent intent;
    private int YEAR;
    private int MONTH;
    private int DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_history);
        binding.setActivity(this);
        init();
        presenter = new HistoryPresenter(this);

        Util.makeToast(this,YEAR + "년" + MONTH + "월" + DAY + "일");
    }

    public void init(){
        intent = getIntent();
        YEAR = intent.getIntExtra("year",2000);
        MONTH = intent.getIntExtra("month",1);
        DAY = intent.getIntExtra("day",1);
    }
}
