package com.yoon.memoria;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Yoon on 2018-01-10.
 */

public class UidSingleton {

    private static UidSingleton uidSingleton;

    public static UidSingleton getInstance(){
        if (uidSingleton == null)
            uidSingleton = new UidSingleton();
        return uidSingleton;
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    public FirebaseUser getUser(){ return FirebaseAuth.getInstance().getCurrentUser();}
}
