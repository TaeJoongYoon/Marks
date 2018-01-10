package com.yoon.memoria;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Yoon on 2018-01-10.
 */

public class UidSingleton {

    private static UidSingleton uidSingleton;
    private String uid = FirebaseAuth.getInstance().getUid();

    public static UidSingleton getInstance(){
        if (uidSingleton == null)
            uidSingleton = new UidSingleton();
        return uidSingleton;
    }

    public String getUid(){
        return uid;
    }
}
