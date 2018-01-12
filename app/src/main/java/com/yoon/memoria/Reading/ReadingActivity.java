package com.yoon.memoria.Reading;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.provider.Contacts;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.yoon.memoria.Comment.CommentActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.Quiz.QuizActivity;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.User.UserActivity;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityReadingBinding;

import static com.yoon.memoria.R.id.post_btn;
import static com.yoon.memoria.R.id.read_delete;
import static com.yoon.memoria.R.id.read_edit;


public class ReadingActivity extends AppCompatActivity implements ReadingContract.View, View.OnClickListener, OnMapReadyCallback{
    private ActivityReadingBinding binding;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private UidSingleton uidSingleton = UidSingleton.getInstance();
    private DatabaseReference databaseReference;
    private ReadingPresenter presenter;

    private GoogleMap googleMap;

    private Post post;
    private Intent intent;
    private String postUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reading);
        binding.setActivity(this);
        presenter = new ReadingPresenter(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initToolbar();
        init();

        binding.readProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child("posts").child(postUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);
                UISetting();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initToolbar(){
        setSupportActionBar(binding.readingToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_48dp);
        getSupportActionBar().setTitle(null);
    }

    public void init(){
        intent = getIntent();
        postUid = intent.getStringExtra("Uid");
        binding.readProfile.setOnClickListener(this);
        binding.readTvUsername.setOnClickListener(this);
        binding.readBtnEdit.setOnClickListener(this);
        binding.readBtnLike.setOnClickListener(this);
        binding.readLocation.setOnClickListener(this);
        binding.readBtnComment.setOnClickListener(this);
    }

    public void UISetting(){
        databaseReference.child("users").child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Util.loadImage(binding.readProfile,user.getImgUri(), ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face_black_48dp));
                binding.readTvUsername.setText(user.getNickname());
                binding.readProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Util.loadImage(binding.readImage,post.getImgUri(), ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_face_black_48dp));
        binding.readTvLike.setText(""+post.getLikeCount());
        binding.readTvContent.setText(post.getContent());
        binding.readLocation.setBackgroundResource(R.drawable.ic_location_on_white_48dp);
        if (post.getLikes().containsKey(uidSingleton.getUid())) {
            binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_white_48dp);
        } else {
            binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_border_black_48dp);
        }
        binding.readBtnComment.setBackgroundResource(R.drawable.ic_border_color_white_48dp);
    }

    @Override
    public void onCompleted(DataSnapshot dataSnapshot){
        Post post = dataSnapshot.getValue(Post.class);
        binding.readTvLike.setText(""+post.getLikeCount());
        binding.readTvComment.setText(""+post.getCommentCount());
        if (post.getLikes().containsKey(uidSingleton.getUid()))
            binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_white_48dp);
        else
            binding.readBtnLike.setBackgroundResource(R.drawable.ic_star_border_black_48dp);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(post.getUid().equals(uidSingleton.getUid()))
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
                break;
            case R.id.read_delete:
                deleteShow();
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
                databaseReference.child("users").child(uidSingleton.getUid()).child("posts").child(postUid).child("content").setValue(temp);
                break;
            case R.id.read_btn_like:
                presenter.onStarClicked(databaseReference.child("posts").child(postUid));
                break;
            case R.id.read_btn_comment:
                Intent intent1 = new Intent(ReadingActivity.this, CommentActivity.class);
                intent1.putExtra("userUid",post.getUid());
                intent1.putExtra("content",post.getContent());
                intent1.putExtra("postUid",postUid);
                startActivity(intent1);
                break;
            case R.id.read_location:
                final Dialog dialog = new Dialog(this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setContentView(R.layout.locaion_dialog);

                MapView mMapView = dialog.findViewById(R.id.location);
                MapsInitializer.initialize(this);
                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();
                mMapView.getMapAsync(this);

                dialog.show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng latLng = new LatLng(post.getLatitude(),post.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    public void deleteShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제");
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("posts").child(postUid).removeValue();
                        databaseReference.child("users").child(uidSingleton.getUid()).child("posts").child(postUid).removeValue();
                        storageSingleton.getStorageReference().child(post.getFilename()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
