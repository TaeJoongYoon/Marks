package com.yoon.memoria.Reading;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
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
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityReadingBinding;


public class ReadingActivity extends AppCompatActivity implements ReadingContract.View{
    private ActivityReadingBinding binding;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private DatabaseReference databaseReference;
    private ReadingPresenter presenter;
    private DataSnapshot currentData;

    private Post post;
    private Intent intent;
    private double latitude;
    private double longitude;

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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(presenter.inputData(snapshot,latitude,longitude)){
                        currentData = snapshot;
                        foundData();
                        binding.readBtnLike.setOnClickListener(view -> {
                            System.out.println(currentData.getKey());
                            onStarClicked(databaseReference.child("posts").child(currentData.getKey()));
                        });
                        break;
                    }
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
        getSupportActionBar().setTitle(null);
    }

    public void init(){
        intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude =intent.getDoubleExtra("longitude",0);
    }

    public void foundData(){
        post = currentData.getValue(Post.class);
        databaseReference.child("users").child(post.getUid()).child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                binding.readTvUsername.setText(username);
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

    public void UIsetting(Uri uri){
        Glide.with(this)
                .load(uri)
                .override(Util.dpToPixel(this,260), Util.dpToPixel(this,260))
                .fitCenter()
                .into(binding.readImage);

        binding.readTvLike.setText(""+post.getLikeCount());

        binding.readTvContent.setText(post.getContent());
        binding.readProgress.setVisibility(View.GONE);
    }

    private void onStarClicked(DatabaseReference postRef) {
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
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
