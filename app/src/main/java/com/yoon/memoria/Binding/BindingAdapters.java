package com.yoon.memoria.Binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.provider.Contacts;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoon.memoria.Util.Util;

/**
 * Created by Yoon on 2018-01-07.
 */

public class BindingAdapters {

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView imageView, String url, Drawable errorDrawable) {
        Util.loadImage(imageView, url, errorDrawable);
    }

    @BindingAdapter({"placeId", "noPic"})
    public static void placePhotosTask(ImageView imageView, String placeId,Drawable drawable){
        Util.placePhotosTask(imageView,placeId,drawable);
    }

    @BindingAdapter({"photoUid","noPhoto"})
    public static void loadCommentPhoto(ImageView imageView, String Uid, Drawable errorDrawable){
        Util.loadCommentPhoto(imageView,Uid,errorDrawable);
    }

    @BindingAdapter({"userUid"})
    public static void loadCommentUser(TextView textView, String Uid){
        Util.loadCommentUser(textView,Uid);
    }
}
