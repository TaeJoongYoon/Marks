package com.yoon.memoria.History;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Main.Fragment.Place.PlaceRecyclerViewAdapter;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityHistoryBinding;

import java.util.ArrayList;
import java.util.List;


public class HistoryActivity extends AppCompatActivity implements ValueEventListener{
    private ActivityHistoryBinding binding;
    private DatabaseReference databaseReference;
    private UidSingleton uidSingleton = UidSingleton.getInstance();
    private HistoryRecyclerViewAdapter adapter;

    private Intent intent;
    private String date;
    private int YEAR;
    private int MONTH;
    private int DAY;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_history);
        binding.setActivity(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        init();
        initToolbar();
        setRecyclerView();
    }

    public void init(){
        intent = getIntent();
        YEAR = intent.getIntExtra("year",2000);
        MONTH = intent.getIntExtra("month",1) + 1;
        DAY = intent.getIntExtra("day",1);

        if(MONTH >=10)
            date = YEAR + "-" + MONTH + "-" + DAY;
        else
            date = YEAR + "-0" + MONTH + "-" + DAY;
        binding.historyDate.setText(YEAR + "년 " + MONTH + "월 " + DAY + "일");
    }

    public void initToolbar(){
        setSupportActionBar(binding.historyToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void setRecyclerView(){
        binding.historyToolbarRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new HistoryRecyclerViewAdapter(this);
        binding.historyToolbarRecyclerview.setAdapter(adapter);
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).addListenerForSingleValueEvent(this);
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Place> places = new ArrayList<>(0);
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Place place = snapshot.getValue(Place.class);
            places.add(place);
            count++;
        }
        if(count == 0)
            Util.makeToast(this,"방문하신 장소가 없습니다!");
        adapter.addItems(places);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
