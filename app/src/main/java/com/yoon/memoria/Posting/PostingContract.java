package com.yoon.memoria.Posting;

import android.app.ProgressDialog;
import android.net.Uri;

import com.yoon.memoria.Model.Like;

import java.util.List;

/**
 * Created by Yoon on 2017-11-10.
 */

public class PostingContract {

    interface View{
        void post_OK();
        void success();
        void failed();
    }

    interface Presenter{
        void post_to_firebase(String img, String content, Double latitude, Double longitude, String username, String date, List<Like> likes);
        void fileUpload(Uri uri, ProgressDialog progressDialog);
    }
}
