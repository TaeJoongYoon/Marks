package com.yoon.memoria.User;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityUserBinding;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity implements UserContract.View {

    private ActivityUserBinding binding;
    private UserPresenter presenter;
    private DatabaseReference databaseReference;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();

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

        if (user.getImgUri().equals("NULL"))
            binding.userProfile.setImageResource(R.drawable.ic_face_black_48dp);
        else
            storageSingleton.getStorageReference().child(user.getImgUri()).getDownloadUrl().addOnSuccessListener(
                    uri -> Glide.with(this)
                                .load(uri)
                                .centerCrop()
                                .into(binding.userProfile)
                    );

        if (user.getFollower().containsKey(getUid())) {
            binding.userFollow.setBackgroundResource(R.drawable.followed);
            binding.userFollow.setText("따라가는 중");
        } else {
            binding.userFollow.setBackgroundResource(R.drawable.custom_button);
            binding.userFollow.setText("따라가기");
        }
        binding.userFollow.setOnClickListener(view -> {
            if(getUid().equals(Uid))
                Util.makeToast(this,"자기 자신은 함께 가는 중입니다!");
            else
                onFollowClicked(databaseReference.child("users").child(Uid));
        });
    }

    public void initToolbar(){
        setSupportActionBar(binding.userToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_48dp);
        getSupportActionBar().setTitle(null);
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

                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                binding.userFollower.setText(""+user.getFollowerCount());
                presenter.onFollowed(databaseReference.child("users").child(getUid()), dataSnapshot.getKey());

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
}
