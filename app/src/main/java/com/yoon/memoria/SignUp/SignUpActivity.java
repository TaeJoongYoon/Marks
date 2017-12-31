package com.yoon.memoria.SignUp;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivitySignUpBinding;

import java.util.ArrayList;
import java.util.List;



public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private FirebaseAuth auth;
    private SignUpPresenter presenter;
    private DatabaseReference databaseReference;
    private ActivitySignUpBinding binding;

    private String username;
    private String nickname;
    private String password1;
    private String password2;

    private List<String> nicks = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        binding.setActivity(this);
        presenter = new SignUpPresenter(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        init();
    }

    public void init(){

        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                String nick = user.getNickname();
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
        binding.signupEtPW2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.signupEtPW2.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE)
                call_Sign_up();
            return true;
        });
        binding.signupBtn.setOnClickListener(view -> {
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
        username = binding.signupEtID.getText().toString();
        nickname = binding.signupEtNAME.getText().toString();
        password1 = binding.signupEtPW.getText().toString();
        password2 = binding.signupEtPW2.getText().toString();

        if(username.length() <= 0)
            Util.makeToast(this,"이메일을 입력해주세요!");
        else if(nickname.length() < 2 || nickname.length() > 8)
            Util.makeToast(this, "별명을 2글자 이상, 8글자 이하로 입력해주세요!");
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
