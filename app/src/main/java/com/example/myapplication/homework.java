package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*学生和家长端查看作业*/
public class homework extends AppCompatActivity {
    Button back;
    TextView homework;/*作业文本*/
    Button chinese;/*语文作业按钮*/
    Button math;/*数学作业按钮*/
    Button english;/*英语作业按钮*/
    String sqlselect;
    String sql;
    String s;

    long cid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(homework.this,"查询失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(homework.this,"查询成功",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(homework.this,"关联班级获取失败",Toast.LENGTH_SHORT).show();
                case 4:
                    Bundle bundle = msg.getData();
                    String homework1 = bundle.getString("result");
                    homework.setText( homework1);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        back = findViewById(R.id.back);
        homework = findViewById(R.id.homework);
        homework.setText("请选择要查看的科目");
        chinese = findViewById(R.id.chinese);
        math = findViewById(R.id.math);
        english = findViewById(R.id.english);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final long[] account_id = {intent.getLongExtra("account_id", 0)};
                sqlselect = "SELECT cid FROM `user_class` WHERE uid = ?;";
                sql = "SELECT chinese,ddl FROM `homework` WHERE cid = ? AND date = ?;";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = DBUtils.getConnection();
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if (account_id[0] != 0) {
                                PreparedStatement ps = connection.prepareStatement(sqlselect);
                                if (ps != null) {
                                    ps.setLong(1, account_id[0]);
                                }
                                ResultSet rs = DBUtils.Query(ps, connection);
                                if (rs != null) {
                                    if (rs.next()) {
                                        cid = rs.getLong("cid");
                                    }
                                }
                            }
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if (ps != null) {
                                ps.setLong(1, cid);
                                Date date = new Date(System.currentTimeMillis());
                                ps.setDate(2, date);
                            }
                            ResultSet rs = DBUtils.Query(ps, connection);
                            if (rs != null) {
                                s="";
                                while (rs.next()) {
                                    message.what = 4;
                                    Bundle bundle1 = new Bundle();
                                    String mat = rs.getString("chinese");
                                    String ddl = rs.getString("ddl");
                                    if (mat != "") {
                                        s=s+mat+"   "+"截止日期："+ddl+"\n";
                                    }
                                    bundle1.putString("result", s);
                                    message.setData(bundle1);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }

        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final long[] account_id = {intent.getLongExtra("account_id", 0)};
                sqlselect = "SELECT cid FROM `user_class` WHERE uid = ?;";
                sql = "SELECT maths,ddl FROM `homework` WHERE cid = ? AND date = ?;";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = DBUtils.getConnection();
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if (account_id[0] != 0) {
                                PreparedStatement ps = connection.prepareStatement(sqlselect);
                                if (ps != null) {
                                    ps.setLong(1, account_id[0]);
                                }
                                ResultSet rs = DBUtils.Query(ps, connection);
                                if (rs != null) {
                                    if (rs.next()) {
                                        cid = rs.getLong("cid");
                                    }
                                }
                            }
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if (ps != null) {
                                ps.setLong(1, cid);
                                Date date = new Date(System.currentTimeMillis());
                                ps.setDate(2, date);
                            }
                            ResultSet rs = DBUtils.Query(ps, connection);
                            if (rs != null) {
                                s="";
                                while (rs.next()) {
                                    message.what = 4;
                                    Bundle bundle1 = new Bundle();
                                    String mat = rs.getString("maths");
                                    String ddl = rs.getString("ddl");
                                    if (mat != "") {
                                        s=s+mat+"   "+"截止日期："+ddl+"\n";
                                    }
                                    bundle1.putString("result", s);
                                    message.setData(bundle1);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }

        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final long[] account_id = {intent.getLongExtra("account_id", 0)};
                sqlselect = "SELECT cid FROM `user_class` WHERE uid = ?;";
                sql = "SELECT english,ddl FROM `homework` WHERE cid = ? AND date = ?;";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = DBUtils.getConnection();
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if (account_id[0] != 0) {
                                PreparedStatement ps = connection.prepareStatement(sqlselect);
                                if (ps != null) {
                                    ps.setLong(1, account_id[0]);
                                }
                                ResultSet rs = DBUtils.Query(ps, connection);
                                if (rs != null) {
                                    if (rs.next()) {
                                        cid = rs.getLong("cid");
                                    }
                                }
                            }
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if (ps != null) {
                                ps.setLong(1, cid);
                                Date date = new Date(System.currentTimeMillis());
                                ps.setDate(2, date);
                            }
                            ResultSet rs = DBUtils.Query(ps, connection);
                            if (rs != null) {
                                s="";
                                while (rs.next()) {
                                    message.what = 4;
                                    Bundle bundle1 = new Bundle();
                                    String mat = rs.getString("english");
                                    String ddl = rs.getString("ddl");
                                    if (mat != "") {
                                        s=s+mat+"   "+"截止日期："+ddl+"\n";
                                    }
                                    bundle1.putString("result", s);
                                    message.setData(bundle1);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}