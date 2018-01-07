package com.yoon.memoria.Main.Fragment.MyInfo;

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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.yoon.memoria.History.HistoryActivity;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Model.User;

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
    private ArrayList<CalendarDay> dates = new ArrayList<>();

    private String filename;

    public MyInfoPresenter(MyInfoContract.View view){
        this.view = view;
    }

    @Override
    public void eventSetting(DatabaseReference databaseReference, List<String> event) {
        databaseReference.child("users").child(getUid()).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String date = dataSnapshot.child("date").getValue(String.class);
                event.add(date);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Intent toHistory(CalendarDay date, Activity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        intent.putExtra("year",date.getYear());
        intent.putExtra("month",date.getMonth()+1);
        intent.putExtra("day",date.getDay());

        return intent;
    }

    @Override
    public List<CalendarDay> eventMark(List<String> events) {
        Calendar calendar = Calendar.getInstance();
        for(String event : events){
            String[] event_time = event.split(".");
            int event_year = Integer.parseInt(event_time[0]);
            int event_month = Integer.parseInt(event_time[1]);
            int event_day = Integer.parseInt(event_time[2]);

            calendar.set(event_year,event_month-1,event_day);
            CalendarDay day = CalendarDay.from(calendar);

            dates.add(day);
        }
        return  dates;
    }

    @Override
    public void show(){
        final EditText editText = new EditText(view.getAct());

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
    public void profile_to_firebase(String imgUri, String filename) {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        firebaseDatabase.child("users").child(getUid()).child("imgUri").setValue(imgUri);
        firebaseDatabase.child("users").child(getUid()).child("filename").setValue(filename);
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
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public String getFilename(){
        return filename;
    }
}
