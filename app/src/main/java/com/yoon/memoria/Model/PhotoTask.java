package com.yoon.memoria.Model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.yoon.memoria.GoogleApiSingleton;

/**
 * Created by Yoon on 2018-01-11.
 */

public class PhotoTask extends AsyncTask<String, Void, PhotoTask.AttributedPhoto> {

    private int mHeight;
    private int mWidth;
    private GoogleApiSingleton googleApiSingleton = GoogleApiSingleton.getInstance();
    public PhotoTask(int width, int height) {
        mHeight = height;
        mWidth = width;
    }

    @Override
    public AttributedPhoto doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }
        final String placeId = params[0];
        AttributedPhoto attributedPhoto = null;

        PlacePhotoMetadataResult result = Places.GeoDataApi
                .getPlacePhotos(googleApiSingleton.getGoogleApiClient(), placeId).await();

        if (result.getStatus().isSuccess()) {
            PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
            if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                // Get the first bitmap and its attributions.
                PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                CharSequence attribution = photo.getAttributions();
                // Load a scaled bitmap for this photo.
                Bitmap image = photo.getScaledPhoto(googleApiSingleton.getGoogleApiClient(), mWidth, mHeight).await()
                        .getBitmap();
                attributedPhoto = new AttributedPhoto(attribution, image);
            }
            // Release the PlacePhotoMetadataBuffer.
            photoMetadataBuffer.release();
        }
        return attributedPhoto;
    }

    public class AttributedPhoto {

        public final CharSequence attribution;

        public final Bitmap bitmap;

        public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
            this.attribution = attribution;
            this.bitmap = bitmap;
        }
    }
}