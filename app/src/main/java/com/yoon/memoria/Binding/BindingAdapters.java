package com.yoon.memoria.Binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.yoon.memoria.Util.Util;

/**
 * Created by Yoon on 2018-01-07.
 */

public class BindingAdapters {

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView imageView, String url, Drawable errorDrawable) {
        Util.loadImage(imageView, url, errorDrawable);
    }
}
