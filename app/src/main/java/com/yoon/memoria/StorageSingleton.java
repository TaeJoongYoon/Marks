package com.yoon.memoria;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Yoon on 2017-12-27.
 */

public class StorageSingleton {

    private static StorageSingleton storageSingleton;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://memoria-186507.appspot.com/").child("images");

    public static StorageSingleton getInstance(){
        if(storageSingleton == null)
            storageSingleton = new StorageSingleton();
        return storageSingleton;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
