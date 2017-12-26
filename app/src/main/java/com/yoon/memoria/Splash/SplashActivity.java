package com.yoon.memoria.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.SignIn.SignInActivity;


public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println("BBBB");
        System.out.println(databaseReference);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        System.out.println("BBBB");
        System.out.println(databaseReference);
        presenter.check_signed(user, databaseReference);
    }

    @Override
    public void toSignin() {
        startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void toMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
