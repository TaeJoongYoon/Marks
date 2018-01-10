package com.yoon.memoria.Posting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityPostingBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostingActivity extends AppCompatActivity implements PostingContract.View {

    private ActivityPostingBinding binding;
    private PostingPresenter presenter;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    private ProgressDialog progressDialog;

    private Intent intent;

    private String uid;
    private String imgUri;
    private String filename;
    private String content;
    private double latitude;
    private double longitude;

    private Uri uri;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_posting);
        binding.setActivity(this);
        presenter = new PostingPresenter(this);

        initToolbar();
        init();
    }

    public void init(){
        binding.postBtn.setOnClickListener(view -> {
            if(filePath.length() <= 0)
                Util.makeToast(this, "사진이 없습니다!");
            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();
                presenter.fileUpload(uri, progressDialog);
            }
        });
        intent = getIntent();

        uid = uidSingleton.getUid();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
    }

    public void initToolbar(){
        setSupportActionBar(binding.postingToolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_48dp);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_post:
                    TedRx2Permission.with(this)
                            .setRationaleTitle(R.string.rationale_title)
                            .setRationaleMessage(R.string.rationale_picture_message)
                            .setDeniedMessage(R.string.rationale_denied_message)
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .request()
                            .subscribe(tedPermissionResult -> {
                                if (tedPermissionResult.isGranted()) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, Util.GALLERY_CODE);
                                } else {
                                    Util.makeToast(this, "권한 거부\n" + tedPermissionResult.getDeniedPermissions().toString());
                                }
                            }, throwable -> {
                            }, () -> {
                            });

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void post_OK(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void success(Uri uri) {
        imgUri = uri.toString();
        filename = presenter.getFilename();
        content = binding.postEdit.getText().toString();
        progressDialog.dismiss();
        presenter.post_to_firebase(uid,latitude,longitude, imgUri, filename,content);
    }

    @Override
    public void failed() {
        Util.makeToast(this,"업로드 실패!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Util.GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            filePath = Util.getRealPathFromURIPath(uri, PostingActivity.this);
            File file = new File(filePath);
            if(file.length()>5*2E20){
                Util.makeToast(this,"이미지 크기는 최대 5MB 입니다");
            }else {
                Glide.with(this)
                        .load(file)
                        .into(binding.postImage);
            }
        }
    }
}
