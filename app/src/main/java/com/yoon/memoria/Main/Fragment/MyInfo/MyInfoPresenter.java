package com.yoon.memoria.Main.Fragment.MyInfo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.History.HistoryActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoon on 2017-11-10.
 */

public class MyInfoPresenter implements MyInfoContract.Presenter {
    private MyInfoContract.View view;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    private String filename;

    public MyInfoPresenter(MyInfoContract.View view){
        this.view = view;
    }

    @Override
    public void nicknameEdit(){
        final EditText editText = new EditText(view.getAct());
        editText.setMaxLines(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getAct());
        builder.setTitle("별명 수정하기");
        builder.setMessage("수정하실 별명을 입력해주세요.");
        builder.setView(editText);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        view.nicknameEdit(editText);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @Override
    public void contentEdit() {
        final EditText editText = new EditText(view.getAct());
        editText.setMaxLines(3);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getAct());
        builder.setTitle("소개 수정하기");
        builder.setMessage("소개를 입력해주세요.");
        builder.setView(editText);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        view.contentEdit(editText);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @Override
    public void profileEdit(){
        TedRx2Permission.with(view.getAct())
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_picture_message)
                .setDeniedMessage(R.string.rationale_denied_message)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .request()
                .subscribe(tedPermissionResult -> {
                    if (tedPermissionResult.isGranted()) {
                        view.goImage();
                    } else {
                        Util.makeToast(view.getAct(), "권한 거부\n" + tedPermissionResult.getDeniedPermissions().toString());
                    }
                }, throwable -> {
                }, () -> {
                });
    }

    @Override
    public void profile_to_firebase(String imgUri, String filename) {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        firebaseDatabase.child("users").child(uidSingleton.getUid()).child("imgUri").setValue(imgUri);
        firebaseDatabase.child("users").child(uidSingleton.getUid()).child("filename").setValue(filename);
    }

    public void fileUpload(Uri filePath) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SS");

        Date now = new Date();
        filename = "profile-" + format.format(now) + ".png";

        StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images/" + filename);
        storageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        view.success(taskSnapshot.getDownloadUrl());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.failed();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });


    }

    @Override
    public String getFilename(){
        return filename;
    }
}
