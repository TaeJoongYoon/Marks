package com.yoon.memoria.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private FirebaseAuth auth;
    private SignUpPresenter presenter;
    private DatabaseReference databaseReference;

    @BindView(R.id.request_ID) EditText input_ID;
    @BindView(R.id.request_nickname) EditText input_nickname;
    @BindView(R.id.request_PW1) EditText input_PW1;
    @BindView(R.id.request_PW2) EditText input_PW2;

    @BindView(R.id.btn_request_signup) Button btn_signup;

    private String username;
    private String nickname;
    private String password1;
    private String password2;

    private List<String> nicks = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        presenter = new SignUpPresenter(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        init();
    }

    public void init(){

        databaseReference.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                String nick = user.getNickname();
                System.out.println(nick);
                nicks.add(nick);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        input_PW2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input_PW2.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE)
                call_Sign_up();
            return true;
        });
        btn_signup.setOnClickListener(view -> {
            call_Sign_up();
        });
    }

    @Override
    public void password_not_satisfied() {
        Util.makeToast(this, "이미 가입된 계정입니다!");
    }

    @Override
    public void finishActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ID",username);
        returnIntent.putExtra("PW",password1);
        setResult(Activity.RESULT_OK,returnIntent);
        Util.makeToast(this, "회원가입이 완료되었습니다!");
        finish();
    }

    public void call_Sign_up(){
        username = input_ID.getText().toString();
        nickname = input_nickname.getText().toString();
        password1 = input_PW1.getText().toString();
        password2 = input_PW2.getText().toString();

        if(username.length() <= 0)
            Util.makeToast(this,"이메일을 입력해주세요!");
        else if(nickname.length() < 2)
            Util.makeToast(this, "별명을 2글자 이상 입력해주세요!");
        else if(nicks.contains(nickname))
            Util.makeToast(this, "중복되는 별명입니다!");
        else if(password1.length() == 0 || password2.length() == 0)
            Util.makeToast(this, "비밀번호를 입력해주세요!");
        else if(password1.length() < 8 || password2.length() < 8)
            Util.makeToast(this,"비밀번호를  8글자 이상 입력해주세요!");
        else {
            if (password1.equals(password2)) {
                presenter.call_sign_up(this,auth,username,password1, nickname);
            } else
                Util.makeToast(this, "비밀번호가 맞지 않습니다!");
        }
    }
}
