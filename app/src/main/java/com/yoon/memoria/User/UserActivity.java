package com.yoon.memoria.User;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Custom.SimpleDividerItemDecoration;
import com.yoon.memoria.FollowList.FollowListActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityUserBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements UserContract.View,View.OnClickListener {

    private ActivityUserBinding binding;
    private UserPresenter presenter;
    private DatabaseReference databaseReference;

    private Intent intent;
    private String Uid;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        binding.setActivity(this);
        presenter = new UserPresenter(this);

        init();
        dataInput();
        setRecyclerView();
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
                user = dataSnapshot.getValue(User.class);
                UISetting();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void UISetting(){
        initToolbar();
        binding.userNickname.setText(user.getNickname());

        binding.userFollower.setText(""+user.getFollowerCount());
        binding.userFollowing.setText(""+user.getFollowingCount());

        Util.loadImage(binding.userProfile,user.getImgUri(), ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face_black_48dp));

        if (user.getFollower().containsKey(getUid())) {
            binding.userFollow.setBackgroundResource(R.drawable.followed);
            binding.userFollow.setText("따라가는 중");
        } else {
            binding.userFollow.setBackgroundResource(R.drawable.custom_button);
            binding.userFollow.setText("따라가기");
        }
        binding.userFollow.setOnClickListener(this);
        binding.userFollower.setOnClickListener(this);
        binding.userFollowing.setOnClickListener(this);
    }

    public void initToolbar(){
        setSupportActionBar(binding.userToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void setRecyclerView(){
        binding.userRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(this);
        binding.userRecyclerView.setAdapter(adapter);

        databaseReference.child("users").child(Uid).child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>(0);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    posts.add(post);
                }
                adapter.addItems(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onFollowClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);

                if (user == null) {

                    return Transaction.success(mutableData);
                }


                if (user.getFollower().containsKey(getUid())) {
                    user.setFollowerCount(user.getFollowerCount()-1);
                    user.getFollower().remove(getUid());

                } else {
                    user.setFollowerCount(user.getFollowerCount()+1);
                    user.getFollower().put(getUid(), true);
                }

                mutableData.child("follower").setValue(user.getFollower());
                mutableData.child("followerCount").setValue(user.getFollowerCount());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                binding.userFollower.setText(""+user.getFollowerCount());
                presenter.onFollowed(databaseReference.child("users").child(getUid()), dataSnapshot, databaseReference);

                if (user.getFollower().containsKey(getUid())) {
                    binding.userFollow.setBackgroundResource(R.drawable.followed);
                    binding.userFollow.setText("따라가는 중");
                }
                else {
                    binding.userFollow.setBackgroundResource(R.drawable.custom_button);
                    binding.userFollow.setText("따라가기");
                }
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
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_follow:
                if(getUid().equals(Uid))
                    Util.makeToast(this,"자기 자신은 함께 가는 중입니다!");
                else
                    onFollowClicked(databaseReference.child("users").child(Uid));
                break;
            case R.id.user_follower:
                Intent intent1 = new Intent(UserActivity.this,FollowListActivity.class);
                intent1.putExtra("follower","FOLLOWER");
                intent1.putExtra("Uid",Uid);
                startActivity(intent1);
                break;
            case R.id.user_following:
                Intent intent2 = new Intent(UserActivity.this,FollowListActivity.class);
                intent2.putExtra("following","FOLLOWING");
                intent2.putExtra("Uid",Uid);
                startActivity(intent2);
                break;
        }
    }
}
