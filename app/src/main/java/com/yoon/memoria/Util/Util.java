package com.yoon.memoria.Util;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

import com.yoon.memoria.MyApplication;

/**
 * Created by Yoon on 2017-11-12.
 */

public class Util {

    public static void makeToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }

    public static int dp_to_px(float dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, MyApplication.getAppContext().getResources().getDisplayMetrics());
    }
}
