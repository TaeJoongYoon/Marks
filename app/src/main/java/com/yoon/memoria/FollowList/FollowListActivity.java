package com.yoon.memoria.FollowList;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Custom.SimpleDividerItemDecoration;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.User.UserRecyclerViewAdapter;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityFollowListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowListActivity extends AppCompatActivity {

    private ActivityFollowListBinding binding;
    private DatabaseReference databaseReference;

    private Intent intent;
    private String follower;
    private String following;
    private String follow_tag;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_follow_list);
        binding.setActivity(this);

        init();
        initToolbar();
        setRecyclerView();
    }

    public void init(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        follower = intent.getStringExtra("follower");
        following =intent.getStringExtra("following");
        if(follower != null) {
            binding.followName.setText(follower);
            follow_tag = "follower";
        }
        if(following != null) {
            binding.followName.setText(following);
            follow_tag = "following";
        }
        Uid = intent.getStringExtra("Uid");
    }

    public void initToolbar(){
        setSupportActionBar(binding.followToolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void setRecyclerView(){
        binding.followRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.followRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        FollowRecyclerViewAdapter adapter = new FollowRecyclerViewAdapter(this);
        binding.followRecyclerView.setAdapter(adapter);

        databaseReference.child("users").child(Uid).child(follow_tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String uid = snapshot.getKey();
                    databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            users.add(0,user);
                            adapter.addItems(users);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
