package com.yoon.memoria;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Yoon on 2017-12-27.
 */

public class StorageSingleton {

    private static StorageSingleton storageSingleton;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static StorageSingleton getInstance(){
        if(storageSingleton == null)
            storageSingleton = new StorageSingleton();
        return storageSingleton;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public StorageReference getStorageReference() {
        storageReference = storage.getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images");
        return storageReference;
    }
}
