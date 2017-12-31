package com.yoon.memoria.Util;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by Yoon on 2017-11-12.
 */

public class Util {

    public static void makeToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }

    public static int dpToPixel(Context context, int dp){
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return  px;
    }
}
