package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity {
    EditText account,password;//账号，密码
    RadioButton rdbtn1,rdbtn2,rdbtn3,rdbtn4;//连接登录身份单选按钮
    RadioGroup login_identity_group;//登录身份按钮组
    Button login_btn,register_btn;//登录按钮，注册按钮

    private static final String Tag = "LoginActivity";
    private static String account_text = "",password_text = "";
    private static IDENTITY identity = new IDENTITY(IDENTITY.IdentityType.STUDENT);

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "error:账号或者密码错误\n请检查账号、密码以及登录身份。", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, "选择了" + identity.getCurrentIdentity(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = findViewById(R.id.login_account);
        password = findViewById(R.id.login_password);

        login_identity_group = findViewById(R.id.login_identity_group);
        rdbtn1 = findViewById(R.id.login_student_rdbtn);
        rdbtn2 = findViewById(R.id.login_teacher_rdbtn);
        rdbtn3 = findViewById(R.id.login_parent_rdbtn);
        rdbtn4 = findViewById(R.id.login_leader_rdbtn);

        login_identity_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(rdbtn1.isChecked()){
                            identity.setIdentity(IDENTITY.IdentityType.STUDENT);
                        }
                        if(rdbtn2.isChecked()){
                            identity.setIdentity(IDENTITY.IdentityType.TEACHER);
                        }
                        if(rdbtn3.isChecked()){
                            identity.setIdentity(IDENTITY.IdentityType.PARENT);
                        }
                        if(rdbtn4.isChecked()){
                            identity.setIdentity(IDENTITY.IdentityType.LEADER);
                        }
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        login_btn = findViewById(R.id.login_login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        account_text = account.getText().toString();
                        password_text = password.getText().toString();
                        Message message = new Message();
                        boolean IfLogin = DBUtils.LoginById(account_text, password_text, identity);
                        if(IfLogin){
                            message.what = 1;
                            handler.sendMessage(message);
                            Intent studentMenuIntent = new Intent(LoginActivity.this, StudentMenuActivity.class);
                            startActivity(studentMenuIntent);
                            finish(); // 结束当前登录界面，防止返回到登录界面
                        }else{
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });

        register_btn = findViewById(R.id.login_register_btn);

    }
}

