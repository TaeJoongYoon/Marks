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
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.FollowList.FollowListActivity;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.EventDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.OneDayDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.SaturdayDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.SundayDecorator;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.Posting.PostingActivity;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.SignIn.SignInActivity;
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

public class MyInfoFragment extends Fragment implements MyInfoContract.View,OnDateSelectedListener,View.OnClickListener{
    private FragmentMyinfoBinding binding;
    private MyInfoPresenter presenter;
    private DatabaseReference databaseReference;
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();

    private OneDayDecorator oneDayDecorator;
    private List<String> event = new ArrayList(0);
    private List<CalendarDay> calendarDays = new ArrayList(0);

    private User user;
    private String imgUri;
    private String filename;
    private Uri mImageCaptureUri;
    private final int GALLERY_CODE = 1112;
    private final int CROP_IMAGE = 1113;

    public MyInfoFragment() {
        presenter = new MyInfoPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_myinfo,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initToolbar();
        calendarinit();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.eventSetting(databaseReference, event);

        Observable<List<String>> event_observable = Observable.just(event);
        event_observable.subscribe(
                event -> calendarDays = presenter.eventMark(event),
                throwable -> {});

        binding.calendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,getActivity()));
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
                case R.id.myinfo_edit:
                    presenter.show();
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

    public void init(){

        binding.myinfoProfile.setOnClickListener(this);
        binding.myinfoFollowing.setOnClickListener(this);
        binding.myinfoFollower.setOnClickListener(this);
        databaseReference.child("users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                binding.myinfoNickname.setText(user.getNickname());
                Util.loadImage(binding.myinfoProfile,user.getImgUri(), ContextCompat.getDrawable(getContext(),R.drawable.ic_face_black_48dp));
                binding.myinfoFollower.setText(""+user.getFollowerCount());
                binding.myinfoFollowing.setText(""+user.getFollowingCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void calendarinit(){
        binding.calendarView.setOnDateChangedListener(this);
        binding.calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        binding.calendarView.setSelectedDate(instance.getTime());

        binding.calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2035, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        oneDayDecorator = new OneDayDecorator(getActivity());
        binding.calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );
    }

    @Override
    public MyInfoContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        binding.calendarView.clearSelection();
        startActivity(presenter.toHistory(date,getActivity()));
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void nicknameEdit(EditText editText){
        String temp = editText.getText().toString();
        binding.myinfoNickname.setText(temp);
        databaseReference.child("users").child(getUid()).child("nickname").setValue(temp);
    }

    @Override
    public Context getAct(){
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        else{
            switch (requestCode){
                case GALLERY_CODE:

                    mImageCaptureUri = data.getData();
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageCaptureUri, "image/*");

                    intent.putExtra("outputX", Util.dpToPixel(getActivity(),120)); // CROP한 이미지의 x축 크기
                    intent.putExtra("outputY", Util.dpToPixel(getActivity(),120)); // CROP한 이미지의 y축 크기
                    intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                    intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_IMAGE);

                    break;
                case CROP_IMAGE:

                    final Bundle extras = data.getExtras();
                    Bitmap bitmap = extras.getParcelable("data");

                    Uri uri = getImageUri(getActivity(), bitmap);
                    presenter.fileUpload(uri);

                    break;
            }
        }
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myinfo_profile:
                TedRx2Permission.with(getActivity())
                        .setRationaleTitle(R.string.rationale_title)
                        .setRationaleMessage(R.string.rationale_picture_message)
                        .setDeniedMessage(R.string.rationale_denied_message)
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request()
                        .subscribe(tedPermissionResult -> {
                            if (tedPermissionResult.isGranted()) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, GALLERY_CODE);
                            } else {
                                Util.makeToast(getActivity(), "권한 거부\n" + tedPermissionResult.getDeniedPermissions().toString());
                            }
                        }, throwable -> {
                        }, () -> {
                        });
                break;
            case R.id.myinfo_follower:
                Intent intent1 = new Intent(getActivity(),FollowListActivity.class);
                intent1.putExtra("follower","FOLLOWER");
                intent1.putExtra("Uid",getUid());
                startActivity(intent1);
                break;
            case R.id.myinfo_following:
                Intent intent2 = new Intent(getActivity(),FollowListActivity.class);
                intent2.putExtra("following","FOLLOWING");
                intent2.putExtra("Uid",getUid());
                startActivity(intent2);
                break;
        }
    }
}
