package com.yoon.memoria.SignIn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.SignUp.SignUpActivity;
import com.yoon.memoria.Util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {
    private FirebaseAuth auth;
    private SignInPresenter presenter;

    @BindView(R.id.ID) EditText input_ID;
    @BindView(R.id.PW) EditText input_PW;

    @BindView(R.id.txt_signup) TextView txt_signup;
    @BindView(R.id.btn_signin) Button btn_signin;

    @BindView(R.id.sign_in_progress) ProgressBar progressBar;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        presenter = new SignInPresenter(this);
        auth = FirebaseAuth.getInstance();

        init();
    }

    public void init(){
        input_PW.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input_PW.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE) {
                btn_call();
            }
            return true;
        });
        btn_signin.setOnClickListener(view -> {
            btn_call();
        });

        txt_signup.setOnClickListener(view -> startActivityForResult(new Intent(SignInActivity.this,SignUpActivity.class),1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                username = data.getStringExtra("ID");
                password = data.getStringExtra("PW");;
                input_ID.setText(username);
                input_PW.setText(password);
                progressBar.setVisibility(View.VISIBLE);

                presenter.call_sign_in(this,auth,username,password);
            }
            else
                return;
        }
    }

    @Override
    public void finishActivity() {
        Util.makeToast(this, "로그인 성공!");
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void failedActivity(){
        Util.makeToast(this, "회원정보가 없습니다. 회원가입 해주세요!");
        progressBar.setVisibility(View.GONE);
    }

    public void btn_call(){
        username = input_ID.getText().toString();
        password = input_PW.getText().toString();
        if(username.length() <= 0)
            Util.makeToast(this, "아이디를 입력해주세요!");
        else if(password.length() == 0)
            Util.makeToast(this, "비밀번호를 입력해주세요!");
        else if(password.length() < 8)
            Util.makeToast(this,"8글자 이상 입력해주세요!");
        else {
            presenter.call_sign_in(this, auth, username, password);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
