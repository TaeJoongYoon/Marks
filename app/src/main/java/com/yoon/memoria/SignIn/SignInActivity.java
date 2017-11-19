package com.yoon.memoria.SignIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.SignUp.SignUpActivity;
import com.yoon.memoria.Util.Util;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    private SignInPresenter presenter;

    private EditText input_ID;
    private EditText input_PW;

    private Button btn_signup;
    private Button btn_signin;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        presenter = new SignInPresenter(this);
    }

    public void init(){
        input_ID = findViewById(R.id.ID);
        input_PW = findViewById(R.id.PW);

        username = input_ID.getText().toString();
        password = input_PW.getText().toString();

        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(view -> presenter.SIGN_IN(username,password));

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(view -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
    }

    @Override
    public void finishActivity() {
        Util.makeToast(this, "로그인 성공!");
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
}
