package com.example.myapplication;

import static com.example.myapplication.IDENTITY.IdentityType.LEADER;
import static com.example.myapplication.IDENTITY.IdentityType.NO_IDENTITY;
import static com.example.myapplication.IDENTITY.IdentityType.PARENT;
import static com.example.myapplication.IDENTITY.IdentityType.STUDENT;
import static com.example.myapplication.IDENTITY.IdentityType.TEACHER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    EditText account,password;//账号，密码
    Button login_btn,register_btn;//登录按钮，注册按钮

    private static final String Tag = "LoginActivity";
    private static String account_text = "",password_text = "";
    private static IDENTITY identity = new IDENTITY(NO_IDENTITY);

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

                        String sql = "SELECT r.rname " +
                                "FROM users u " +
                                "JOIN user_role ur ON u.uid = ur.uid " +
                                "JOIN role r ON ur.rid = r.rid " +
                                "WHERE u.uid = ? " +
                                "AND u.password = ?;";
                        Connection connection = null;
                        try {
                            connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if (ps != null) {
                                ps.setString(1, account_text);
                                ps.setString(2, password_text);
                                ResultSet rs = DBUtils.Query(ps,connection);
                                if (rs != null) {
                                    if (rs.last()) {
                                        switch(rs.getString("rname")){
                                            case "student":identity.setIdentity(STUDENT);break;
                                            case "teacher":identity.setIdentity(TEACHER);break;
                                            case "parent":identity.setIdentity(PARENT);break;
                                            case "leader":identity.setIdentity(LEADER);break;
                                        }
                                    }
                                    ps.close();
                                }
                            }
                            DBUtils.CloseConnection(connection);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DBUtils", "异常：" + e.getMessage());
                        } finally {
                            DBUtils.CloseConnection(connection);
                        }

                        Intent nextMenuIntent = null;
                        switch(identity.getCurrentIdentity()){
                            case STUDENT:
                                nextMenuIntent = new Intent(LoginActivity.this, StudentMenuActivity.class);
                                break;
                            case TEACHER:
                                nextMenuIntent = new Intent(LoginActivity.this, TeacherMenuActivity.class);
                                break;
                            case PARENT:
                                nextMenuIntent = new Intent(LoginActivity.this, ParentMenuActivity.class);
                                break;
                            case LEADER:
                                nextMenuIntent = new Intent(LoginActivity.this, AdministratorMenuActivity.class);
                                break;
                        }
                        if(nextMenuIntent != null){
                            nextMenuIntent.putExtra("account",account_text);
                            startActivity(nextMenuIntent);
                            message.what = 1;
                            handler.sendMessage(message);
//                            finish(); // 结束当前登录界面，防止返回到登录界面
                        }else{
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });

        register_btn = findViewById(R.id.login_register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Register = new Intent(LoginActivity.this,registerActivity.class);
                startActivity(Register);
            }
        });
    }
}

