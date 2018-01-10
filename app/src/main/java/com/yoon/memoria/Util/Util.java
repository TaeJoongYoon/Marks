package com.yoon.memoria.Util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by Yoon on 2017-11-12.
 */

public class Util {

    public static final int POST_CODE = 1111;
    public static final int GALLERY_CODE=1112;
    public static final int GPS_ENABLE_REQUEST_CODE = 2001;

    public static void makeToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }

    public static int dpToPixel(Context context, int dp){
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return  px;
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static void loadImage(ImageView imageView, String url, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(errorDrawable)
                .into(imageView);
    }
}
