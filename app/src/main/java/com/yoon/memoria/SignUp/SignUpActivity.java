package com.yoon.memoria.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;


public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private SignUpPresenter presenter;

    private EditText input_ID;
    private EditText input_PW1;
    private EditText input_PW2;

    private Button btn_signup;

    private String username;
    private String password1;
    private String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        presenter = new SignUpPresenter(this);
    }

    public void init(){
        input_ID = findViewById(R.id.request_ID);
        input_PW1 = findViewById(R.id.request_PW1);
        input_PW2 = findViewById(R.id.request_PW2);

        username = input_ID.getText().toString();
        password1 = input_PW1.getText().toString();
        password2 = input_PW2.getText().toString();

        btn_signup = findViewById(R.id.btn_request_signup);
        btn_signup.setOnClickListener(view -> presenter.SIGN_UP());
    }

    @Override
    public void finishActivity() {
        Util.makeToast(this,"회원가입이 완료되었습니다!");
        finish();
    }
}
