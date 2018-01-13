package com.yoon.memoria.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Model.PhotoTask;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.MyApplication;
import com.yoon.memoria.R;

/**
 * Created by Yoon on 2017-11-12.
 */

public class Util {

    public static final int SIGN_UP = 1001;
    public static final int EDIT_OODE = 1110;
    public static final int POST_CODE = 1111;
    public static final int GALLERY_CODE=1112;
    public static final int GPS_ENABLE_REQUEST_CODE = 2001;

    public static void makeToast(Context context, String text){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
    }

    public static int dpToPixel(int dp){
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.getAppContext().getResources().getDisplayMetrics());
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

    @SuppressLint("StaticFieldLeak")
    public static void placePhotosTask(ImageView imageView, String placeId, Drawable drawable) {
        new PhotoTask(dpToPixel(120), dpToPixel(120)) {
            @Override
            protected void onPreExecute() {
                imageView.setImageDrawable(drawable);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    imageView.setImageBitmap(attributedPhoto.bitmap);
                }
            }
        }.execute(placeId);
    }

    public static void loadCommentPhoto(ImageView imageView, String Uid, Drawable errorDrawable){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                loadImage(imageView,user.getImgUri(),errorDrawable);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void loadCommentUser(TextView textView, String Uid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                textView.setText(user.getNickname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
