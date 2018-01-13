package com.yoon.memoria.SignIn;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.SignUp.SignUpActivity;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {
    private FirebaseAuth auth;
    private SignInPresenter presenter;
    private ActivitySignInBinding binding;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        binding.setActivity(this);

        presenter = new SignInPresenter(this);
        auth = FirebaseAuth.getInstance();

        init();
    }

    public void init(){
        binding.signinEtPW.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.signinEtPW.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE) {
                btn_call();
            }
            return true;
        });
        binding.signinBtn.setOnClickListener(view -> {
            btn_call();
        });

        binding.signinTvSignup.setOnClickListener(view -> startActivityForResult(new Intent(SignInActivity.this,SignUpActivity.class),Util.SIGN_UP));

        binding.personal.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://blog.naver.com/xownddl1234/221184287453"));
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.SIGN_UP) {
            if(resultCode == Activity.RESULT_OK){
                username = data.getStringExtra("ID");
                password = data.getStringExtra("PW");;
                binding.signinEtID.setText(username);
                binding.signinEtPW.setText(password);
                binding.signinProgressBar.setVisibility(View.VISIBLE);

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
        binding.signinProgressBar.setVisibility(View.GONE);
    }

    public void btn_call(){
        username = binding.signinEtID.getText().toString();
        password = binding.signinEtPW.getText().toString();
        if(username.length() <= 0)
            Util.makeToast(this, "아이디를 입력해주세요!");
        else if(password.length() == 0)
            Util.makeToast(this, "비밀번호를 입력해주세요!");
        else if(password.length() < 8)
            Util.makeToast(this,"8글자 이상 입력해주세요!");
        else {
            presenter.call_sign_in(this, auth, username, password);
            binding.signinProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
