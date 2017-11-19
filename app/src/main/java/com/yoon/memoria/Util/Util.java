package com.yoon.memoria.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Yoon on 2017-11-12.
 */

public class Util {

    public static void makeToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }
}
