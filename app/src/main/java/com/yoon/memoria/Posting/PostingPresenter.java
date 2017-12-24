package com.yoon.memoria.Posting;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yoon.memoria.Model.Like;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.Util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void post_to_firebase(String filename, String content, Double latitude, Double longitude, String username, String date, List<Like> likes) {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        post = new Post(filename,content,latitude, longitude, username, date, likes);
        firebaseDatabase.child("post").push().setValue(post);
        view.post_OK();
    }

    @Override
    public void fileUpload(Uri filePath, ProgressDialog progressDialog) {
        firebaseStorage = FirebaseStorage.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMHH_mmss");

        Date now = new Date();
        filename = format.format(now) + ".png";

        StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images/" + filename);
        storageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        view.success();
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

    public String getFilename(){
        return filename;
    }
}
