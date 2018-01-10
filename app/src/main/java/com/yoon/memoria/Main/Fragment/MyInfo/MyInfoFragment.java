package com.yoon.memoria.Main.Fragment.MyInfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.otto.Subscribe;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.EventBus.ActivityResultEvent;
import com.yoon.memoria.FollowList.FollowListActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.Posting.PostingActivity;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.SignIn.SignInActivity;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.User.UserRecyclerViewAdapter;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.FragmentMyinfoBinding;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public class MyInfoFragment extends Fragment implements MyInfoContract.View,View.OnClickListener{
    private FragmentMyinfoBinding binding;
    private MyInfoPresenter presenter;
    private DatabaseReference databaseReference;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    private User user;
    private String imgUri;
    private String filename;

    public MyInfoFragment() {
        presenter = new MyInfoPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_myinfo,container,false);
        initToolbar();
        setRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        init();
        super.onStart();
    }
    public void initToolbar() {
        binding.myinfoToolbar.inflateMenu(R.menu.menu_myinfo);

        binding.myinfoToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.myinfo_profile_edit:
                    presenter.profileEdit();
                    break;
                case R.id.myinfo_edit:
                    presenter.nicknameEdit();
                    break;
                case R.id.myinfo_content_edit:
                    presenter.contentEdit();
                    break;
                case R.id.myinfo_logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                    Util.makeToast(getActivity(),"로그아웃 되었습니다!");
                    getActivity().finish();
                    break;
            }
            return false;
        });
    }


    public void setRecyclerView(){
        binding.myinfoRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),3));
        MyInfoRecyclerViewAdapter adapter = new MyInfoRecyclerViewAdapter(getActivity());
        binding.myinfoRecyclerview.setAdapter(adapter);

        databaseReference.child("users").child(uidSingleton.getUid()).child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>(0);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    posts.add(0, post);
                }
                adapter.addItems(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void init(){
        binding.myinfoProfile.setOnClickListener(this);
        binding.myinfoFollowing.setOnClickListener(this);
        binding.myinfoFollower.setOnClickListener(this);
        databaseReference.child("users").child(uidSingleton.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                binding.myinfoNickname.setText(user.getNickname());
                binding.myinfoProfileText.setText(user.getProfile());
                Util.loadImage(binding.myinfoProfile,user.getImgUri(), ContextCompat.getDrawable(getContext(),R.drawable.ic_face_black_48dp));
                binding.myinfoFollower.setText("팔로워 "+user.getFollowerCount());
                binding.myinfoFollowing.setText("팔로잉 "+user.getFollowingCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public MyInfoContract.Presenter getPresenter() {
        return presenter;
    }

    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        else{
            switch (requestCode){
                case Util.GALLERY_CODE:

                    Uri uri = data.getData();
                    String filePath = Util.getRealPathFromURIPath(uri, getActivity());
                    File file = new File(filePath);
                    if(file.length()>5*2E20){
                        Util.makeToast(getActivity(),"이미지 크기는 최대 5MB 입니다");
                    }else {
                        presenter.fileUpload(uri);
                    }
                    break;
            }
        }
    }

    @Override
    public void nicknameEdit(EditText editText){
        String temp = editText.getText().toString();
        binding.myinfoNickname.setText(temp);
        databaseReference.child("users").child(uidSingleton.getUid()).child("nickname").setValue(temp);
    }

    @Override
    public void contentEdit(EditText editText){
        String temp = editText.getText().toString();
        binding.myinfoProfileText.setText(temp);
        databaseReference.child("users").child(uidSingleton.getUid()).child("profile").setValue(temp);
    }

    @Override
    public Context getAct(){
        return getActivity();
    }

    @Override
    public void goImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Util.GALLERY_CODE);
    }

    @Override
    public void success(Uri uri) {
        imgUri = uri.toString();
        filename = presenter.getFilename();
        Util.loadImage(binding.myinfoProfile,imgUri,ContextCompat.getDrawable(getContext(),R.drawable.ic_face_black_48dp));
        if (!(user.getImgUri().equals("NULL")))
        storageSingleton.getStorageReference().child(user.getFilename()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        presenter.profile_to_firebase(imgUri,filename);
    }

    @Override
    public void failed() {
        Util.makeToast(getActivity(),"사진수정 실패!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myinfo_profile:
               presenter.profileEdit();
                break;
            case R.id.myinfo_follower:
                Intent intent1 = new Intent(getActivity(),FollowListActivity.class);
                intent1.putExtra("follower","FOLLOWER");
                intent1.putExtra("Uid",uidSingleton.getUid());
                startActivity(intent1);
                break;
            case R.id.myinfo_following:
                Intent intent2 = new Intent(getActivity(),FollowListActivity.class);
                intent2.putExtra("following","FOLLOWING");
                intent2.putExtra("Uid",uidSingleton.getUid());
                startActivity(intent2);
                break;
        }
    }
}
