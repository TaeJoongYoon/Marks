package com.yoon.memoria.Reading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.User.UserActivity;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityReadingBinding;

import static com.yoon.memoria.R.id.read_delete;
import static com.yoon.memoria.R.id.read_edit;


public class ReadingActivity extends AppCompatActivity implements ReadingContract.View, View.OnClickListener{
    private ActivityReadingBinding binding;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private DatabaseReference databaseReference;
    private ReadingPresenter presenter;

    private Post post;
    private Intent intent;
    private String postUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reading);
        binding.setActivity(this);
        presenter = new ReadingPresenter(this);

        initDB();
        initToolbar();
        init();

        binding.readProgress.setVisibility(View.VISIBLE);
        databaseReference.child("posts").child(postUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);
                foundData();
                if (post.getLikes().containsKey(getUid())) {
                    binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_white_48dp);
                } else {
                    binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_border_black_48dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void initDB(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void initToolbar(){
        setSupportActionBar(binding.readingToolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void init(){
        intent = getIntent();
        postUid = intent.getStringExtra("Uid");
        binding.readProfile.setOnClickListener(this);
        binding.readTvUsername.setOnClickListener(this);
        binding.readBtnEdit.setOnClickListener(this);
        binding.readBtnLike.setOnClickListener(this);

    }

    public void foundData(){
        databaseReference.child("users").child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImgUri().equals("NULL"))
                    binding.readProfile.setImageResource(R.drawable.ic_face_black_48dp);
                else
                    storageSingleton.getStorageReference().child(user.getImgUri()).getDownloadUrl().addOnSuccessListener(
                            uri -> profileSetting(uri)
                    );
                binding.readTvUsername.setText(user.getNickname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String filename = post.getFilename();
        storageSingleton.getStorageReference().child(filename).getDownloadUrl().addOnSuccessListener(
                uri -> UIsetting(uri)
        );
    }

    public void profileSetting(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(binding.readProfile);
    }

    public void UIsetting(Uri uri){
        Glide.with(this)
                .load(uri)
                .into(binding.readImage);

        binding.readTvLike.setText(""+post.getLikeCount());

        binding.readTvContent.setText(post.getContent());
        binding.readProgress.setVisibility(View.GONE);
    }

    public void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post= mutableData.getValue(Post.class);

                if (post == null) {

                    return Transaction.success(mutableData);
                }

                if (post.getLikes().containsKey(getUid())) {
                    post.setLikeCount(post.getLikeCount()-1);
                    post.getLikes().remove(getUid());

                } else {
                    post.setLikeCount(post.getLikeCount()+1);
                    post.getLikes().put(getUid(), true);
                }

                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                binding.readTvLike.setText(""+post.getLikeCount());
                if (post.getLikes().containsKey(getUid()))
                    binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_white_48dp);
                else
                    binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_border_black_48dp);
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(post.getUid().equals(getUid()))
            getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.read_edit:
                String temp = binding.readTvContent.getText().toString();
                binding.readTvContent.setVisibility(View.GONE);
                binding.readEtContent.setVisibility(View.VISIBLE);
                binding.readBtnEdit.setVisibility(View.VISIBLE);
                binding.readEtContent.setText(temp);
                return true;
            case R.id.read_delete:
                databaseReference.child("posts").child(postUid).removeValue();
                databaseReference.child("users").child(getUid()).child("posts").child(postUid).removeValue();
                storageSingleton.getStorageReference().child(post.getFilename()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.read_profile:
            case R.id.read_tv_username:
                Intent intent = new Intent(ReadingActivity.this, UserActivity.class);
                intent.putExtra("Uid",post.getUid());
                startActivity(intent);
                break;
            case R.id.read_btn_edit:
                String temp = binding.readEtContent.getText().toString();
                binding.readEtContent.setVisibility(View.GONE);
                binding.readTvContent.setVisibility(View.VISIBLE);
                binding.readTvContent.setText(temp);
                binding.readBtnEdit.setVisibility(View.GONE);

                databaseReference.child("posts").child(postUid).child("content").setValue(temp);
                databaseReference.child("users").child(getUid()).child("posts").child(postUid).child("content").setValue(temp);
                break;
            case R.id.read_btn_like:
                onStarClicked(databaseReference.child("posts").child(postUid));
                break;
        }
    }
}
