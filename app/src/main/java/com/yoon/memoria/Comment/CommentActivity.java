package com.yoon.memoria.Comment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Custom.SimpleDividerItemDecoration;
import com.yoon.memoria.FollowList.FollowRecyclerViewAdapter;
import com.yoon.memoria.Model.Comment;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.Quiz.QuizRecyclerViewAdapter;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.User.UserActivity;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityCommentBinding;
import com.yoon.memoria.databinding.CommentItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements CommentContract.View, ValueEventListener {

    private ActivityCommentBinding binding;
    private CommentRecyclerViewAdapter adapter;
    private DatabaseReference databaseReference;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    private Intent intent;
    private String Uid;
    private String content;
    private String postUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        binding.setActivity(this);

        init();
        setRecyclerView();
    }

    public void init(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();
        Uid = intent.getStringExtra("userUid");
        content = intent.getStringExtra("content");
        postUid = intent.getStringExtra("postUid");
        binding.commentBtn.setOnClickListener(view -> {
            String content = binding.commentText.getText().toString();
            if(content.length()>0) {
                String KEY = databaseReference.child("posts").child(postUid).child("comments").push().getKey();
                Comment comment = new Comment(uidSingleton.getUid(), KEY, content);
                databaseReference.child("posts").child(postUid).child("comments").child(KEY).setValue(comment);

                databaseReference.child("posts").child(postUid).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = dataSnapshot.getValue(Integer.class);
                        count++;
                        databaseReference.child("posts").child(postUid).child("commentCount").setValue(count);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                binding.commentText.setText("");
            }
        });
    }

    public void setRecyclerView(){
        binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.commentRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        adapter = new CommentRecyclerViewAdapter(this, this);
        binding.commentRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child("users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Util.loadImage(binding.commentProfile,user.getImgUri(),getResources().getDrawable(R.drawable.ic_face_black_48dp));
                binding.commentName.setText(user.getNickname());
                binding.commentContent.setText(content);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("posts").child(postUid).child("comments").addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Comment> commentList = new ArrayList<>(0);
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Comment comment = snapshot.getValue(Comment.class);
            commentList.add(comment);
        }
        adapter.addItems(commentList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void toUser(String Uid) {
        Log.e("Comment","click");
        Intent intent = new Intent(CommentActivity.this, UserActivity.class);
        intent.putExtra("Uid",Uid);
        startActivity(intent);
    }

    @Override
    public void delete(String Uid, String commentUid) {
        if(Uid.equals(uidSingleton.getUid()) || this.Uid.equals(uidSingleton.getUid())){
            databaseReference.child("posts").child(postUid).child("comments").child(commentUid).removeValue();
            databaseReference.child("posts").child(postUid).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = dataSnapshot.getValue(Integer.class);
                    count--;
                    databaseReference.child("posts").child(postUid).child("commentCount").setValue(count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            Util.makeToast(this,"삭제되었습니다!");
        }
    }
}
