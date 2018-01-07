package com.yoon.memoria.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

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

    public static void loadImage(ImageView imageView, String url, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(errorDrawable)
                .into(imageView);
    }
}
