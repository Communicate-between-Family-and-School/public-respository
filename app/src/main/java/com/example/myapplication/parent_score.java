package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class parent_score extends AppCompatActivity {

    Button back;
    Button search;
    EditText term;
    TextView score;
    String sql;
    long termid;
    String s;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(parent_score.this,"查询失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(parent_score.this,"查询成功",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(parent_score.this,"关联班级获取失败",Toast.LENGTH_SHORT).show();
                case 4:
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("result");
                    score.setText(result);
            }
        }
    };
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        term = findViewById(R.id.term);
        score = findViewById(R.id.score);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final long[] account_id = {intent.getLongExtra("account_id", 0)};
                sql = "SELECT subject, score FROM grade g, parent_stu p where p.pid=? and g.sid=p.sid and g.termid=?;";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = DBUtils.getConnection();
                        Message message = new Message();
                        message.what = 1;
                        termid = Integer.parseInt(term.getText().toString());
                        try {
                            if (account_id[0] != 0) {
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setLong(1, account_id[0]);
                                    ps.setLong(2, termid);
                                }
                                ResultSet rs = DBUtils.Query(ps, connection);
                                if (rs != null) {
                                    message.what = 4;
                                    s="";
                                    while (rs.next()) {
                                        Bundle bundle1 = new Bundle();
                                        s = s + "学科：";
                                        s = s + rs.getString("subject") + "   分数：";
                                        s = s + rs.getInt("score") + "\n";
                                        bundle1.putString("result",s);
                                        message.setData(bundle1);
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}
