package com.yoon.memoria.Splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.BuildConfig;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.Model.DBData;
import com.yoon.memoria.SignIn.SignInActivity;


public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        presenter = new SplashPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.child("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBData dbData = dataSnapshot.getValue(DBData.class);
                check_version(dbData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void check_version(DBData data) {
        int appVersionCode = BuildConfig.VERSION_CODE;
        int minVersionCode = Integer.parseInt(data.getMinimum_version_code());
        int latestVersionCode = Integer.parseInt(data.getLastest_version_code());

        String skipVerCode = getSharedPreferences("pref", MODE_PRIVATE).getString("skip_version", "");

        if (appVersionCode < minVersionCode){
            forceUpdatePopup(data.getForce_update_message());
        }else if(appVersionCode < latestVersionCode &&
                !skipVerCode.equals(String.valueOf(latestVersionCode))){
            optionalUpdatePopup(data.getOptional_update_message(), latestVersionCode);
        }
        else{
            presenter.check_signed(user);
        }
    }

    private void forceUpdatePopup(String msg){
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("설치하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void optionalUpdatePopup(String msg, final int versionCode){
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("설치하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("나중에", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref;
                        pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("skip_version", String.valueOf(versionCode));
                        editor.apply();
                        dialog.dismiss();
                        presenter.check_signed(user);
                    }
                })
                .show();
    }

    @Override
    public void toSignIn() {
        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        finish();
}

    @Override
    public void toMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
