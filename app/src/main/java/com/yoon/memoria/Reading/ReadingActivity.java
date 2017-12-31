package com.yoon.memoria.Reading;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingActivity extends AppCompatActivity implements ReadingContract.View{
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private StorageReference storageReference;
    private ReadingPresenter presenter;
    private DataSnapshot currentData;

    @BindView(R.id.readingToolbar) Toolbar toolbar;
    @BindView(R.id.read_username) TextView username;
    @BindView(R.id.read_image) ImageView imageView;
    @BindView(R.id.readLike) TextView like;
    @BindView(R.id.readText) TextView content;
    @BindView(R.id.read_progress) ProgressBar progressBar;

    @BindView(R.id.read_like) Button button;

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

        initDB();
        initToolbar();
        init();

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("11");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println("22");
                    if(presenter.inputData(snapshot,latitude,longitude)){
                        currentData = snapshot;
                        foundData();
                        button.setOnClickListener(view -> {
                            System.out.println(currentData.getKey());
                            onStarClicked(databaseReference.child("post").child(currentData.getKey()));
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = storageSingleton.getStorageReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        Util.makeToast(this, ""+databaseReference);
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
        username.setText(post.getNickname());

        Glide.with(this)
                .load(uri)
                .override(dpToPixel(260), dpToPixel(260))
                .fitCenter()
                .into(imageView);

        like.setText(""+post.getLikeCount());

        content.setText(post.getContent());
        progressBar.setVisibility(View.GONE);
    }

    public int dpToPixel(int dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
        return  px;
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post= mutableData.getValue(Post.class);

                if (post == null) {

                    return Transaction.success(mutableData);
                }

                if (post.getLikes().containsKey(user.getUid())) {
                    post.setLikeCount(post.getLikeCount()-1);
                    post.getLikes().remove(user.getUid());
                } else {
                    post.setLikeCount(post.getLikeCount()+1);
                    post.getLikes().put(user.getUid(), true);
                }

                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                like.setText(""+post.getLikeCount());
            }
        });
    }
}
