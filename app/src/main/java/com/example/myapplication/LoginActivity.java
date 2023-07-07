package com.example.myapplication;

import static com.example.myapplication.IDENTITY.IdentityType.NO_IDENTITY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity {
    EditText account,password;//账号，密码
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
                        IDENTITY login_type = DBUtils.LoginById(account_text, password_text);
                        if(login_type.getCurrentIdentity() != NO_IDENTITY){
                            Intent nextMenuIntent = null;

                            switch(login_type.getCurrentIdentity()){
                                case STUDENT:
                                    nextMenuIntent = new Intent(LoginActivity.this, StudentMenuActivity.class);
                                    break;
                                case TEACHER:
                                    nextMenuIntent = new Intent(LoginActivity.this, TeacherMenuActivity.class);
                                    break;
                                case PARENT:
                                case LEADER:
                            }
                            if(nextMenuIntent != null){
                                nextMenuIntent.putExtra("account",account_text);
                                startActivity(nextMenuIntent);

                                message.what = 1;
                                handler.sendMessage(message);

                                finish(); // 结束当前登录界面，防止返回到登录界面
                            }
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

