package com.yoon.memoria.Posting;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.StorageSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2017-11-10.
 */

public class PostingPresenter implements PostingContract.Presenter {
    private PostingContract.View view;

    private DatabaseReference firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private Post post;
    private String filename;

    public PostingPresenter(PostingContract.View view){
        this.view = view;
    }


    @Override
    public void post_to_firebase(String uid, String date, double latitude, double longitude,String imgUri, String filename, String content) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        String KEY = firebaseDatabase.child("posts").push().getKey();
        post = new Post(uid,KEY,date,latitude,longitude,imgUri, filename,content);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + KEY, postValues);
        childUpdates.put("/users/" + getUid() + "/" + "/posts/" + KEY, postValues);

        firebaseDatabase.updateChildren(childUpdates);
        view.post_OK();
    }

    @Override
    public void fileUpload(Uri filePath, ProgressDialog progressDialog) {
        firebaseStorage = FirebaseStorage.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SS");

        Date now = new Date();
        filename = format.format(now) + ".png";

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
                        @SuppressWarnings("VisibleForTests")
                                double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                });


    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getFilename(){
        return filename;
    }
}
