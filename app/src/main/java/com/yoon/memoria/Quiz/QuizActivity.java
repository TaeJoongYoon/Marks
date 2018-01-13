package com.yoon.memoria.Quiz;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Main.Fragment.Place.PlaceRecyclerViewAdapter;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityQuizBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QuizActivity extends AppCompatActivity implements QuizContract.View,ValueEventListener {
    private ActivityQuizBinding binding;
    private DatabaseReference databaseReference;
    private UidSingleton uidSingleton = UidSingleton.getInstance();
    private QuizRecyclerViewAdapter adapter;

    private QuizPresenter presenter;

    private int ANSWER;

    public int quizCount;
    public int ansCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        binding.setActivity(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        presenter = new QuizPresenter(this);

        initToolbar();
        setRecyclerView();
    }

    public void initToolbar(){
        setSupportActionBar(binding.quizToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void setRecyclerView(){
        binding.quizRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new QuizRecyclerViewAdapter(this,this);
        binding.quizRecyclerView.setAdapter(adapter);
    }

    public void setData(List<Place> questions, int answer){
        binding.quizQuestion.setText(questions.get(answer).getDetail() + "쯤에");
        binding.quizQuestion1.setText(" 어디에 방문하셨습니까?");
        adapter.addItems(questions);
    }

    @Override
    public void onStart() {
        databaseReference.child("users").child(uidSingleton.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                quizCount = user.getQuizCount();
                ansCount = user.getAnsCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").addListenerForSingleValueEvent(this);
        super.onStart();
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
        List<String> placenames = new ArrayList<>(0);
        List<Place> questions = new ArrayList<>(0);
        int i = 0;

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Place place = snapshot.getValue(Place.class);
            places.add(place);
        }
        Collections.shuffle(places);

        do{
            if(!placenames.contains(places.get(i).getPlaceName())) {
                questions.add(places.get(i));
                placenames.add(places.get(i).getPlaceName());
            }
            i++;
        }while(!(questions.size()==4 || i==places.size()));

        if(questions.size()==4){
            ANSWER = (int)(Math.random()*4);
            setData(questions, ANSWER);
        }
        else
            Util.makeToast(this,"방문하신 장소가 부족합니다!");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void check(int i) {
        quizCount++;
        databaseReference.child("users").child(uidSingleton.getUid()).child("quizCount").setValue(quizCount);
        if(ANSWER == i) {
            ansCount++;
            databaseReference.child("users").child(uidSingleton.getUid()).child("ansCount").setValue(ansCount);

            Util.makeToast(this, "정답입니다!");
            databaseReference.child("users").child(uidSingleton.getUid()).child("places").addListenerForSingleValueEvent(this);
        }
        else
            Util.makeToast(this,"오답입니다!ㅠ");
    }
}
