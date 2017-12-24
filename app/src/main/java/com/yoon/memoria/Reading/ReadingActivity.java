package com.yoon.memoria.Reading;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingActivity extends AppCompatActivity implements ReadingContract.View{
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ReadingPresenter presenter;
    private DataSnapshot currentData;

    @BindView(R.id.readingToolbar) Toolbar toolbar;
    @BindView(R.id.read_username) TextView username;
    @BindView(R.id.read_image) ImageView imageView;
    @BindView(R.id.readLike) TextView like;
    @BindView(R.id.readText) TextView content;
    @BindView(R.id.read_progress) ProgressBar progressBar;

    private Post post;
    private Intent intent;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        ButterKnife.bind(this);
        presenter = new ReadingPresenter(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images");

        initToolbar();
        init();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(presenter.inputData(snapshot,latitude,longitude)){
                        currentData = snapshot;
                        foundData();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void initToolbar(){
        setSupportActionBar(toolbar);
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
        String filename = post.getFilename();
        storageReference.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                UIsetting(uri);
            }
        });
    }

    public void UIsetting(Uri uri){
        username.setText(post.getUsername());

        Glide.with(this)
                .load(uri)
                .override(dpToPixel(260), dpToPixel(260))
                .fitCenter()
                .into(imageView);

        like.setText("0");

        content.setText(post.getContent());
        progressBar.setVisibility(View.GONE);
    }

    public int dpToPixel(int dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
        return  px;
    }
}
