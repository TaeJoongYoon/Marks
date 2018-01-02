package com.yoon.memoria.User;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.UiSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity implements UserContract.View {

    private ActivityUserBinding binding;
    private UserPresenter presenter;
    private DatabaseReference databaseReference;

    private Intent intent;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        binding.setActivity(this);
        presenter = new UserPresenter(this);

        init();
        dataInput();
    }

    public void init(){
        intent = getIntent();
        Uid = intent.getStringExtra("Uid");
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void dataInput(){
        databaseReference.child("users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
