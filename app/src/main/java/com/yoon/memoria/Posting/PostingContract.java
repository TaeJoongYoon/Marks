package com.yoon.memoria.Posting;

import android.app.ProgressDialog;
import android.net.Uri;
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
        void post_to_firebase(String uid, String date, double latitude, double longitude, String filename, String content);
        void fileUpload(Uri uri, ProgressDialog progressDialog);
    }
}
