package com.yoon.memoria;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yoon.memoria.Model.User;

/**
 * Created by Yoon on 2017-12-27.
 */

public class MySingleton {

    private static MySingleton mySingleton;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static MySingleton getInstance(){
        if(mySingleton == null)
            mySingleton = new MySingleton();
        return mySingleton;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public StorageReference getStorageReference() {
        storageReference = storage.getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images");
        return storageReference;
    }
}
