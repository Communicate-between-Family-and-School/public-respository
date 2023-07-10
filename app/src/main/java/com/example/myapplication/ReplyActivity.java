package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReplyActivity extends AppCompatActivity {
    Button back, quit, reply;
    TextView receiver_name, communication;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(ReplyActivity.this,"回复失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ReplyActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Bundle bundle = getIntent().getExtras();

        back = findViewById(R.id.back);
        quit = findViewById(R.id.quit);
        reply = findViewById(R.id.reply);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        receiver_name = findViewById(R.id.receiver_name);
        communication = findViewById(R.id.communication);
        receiver_name.setText("发送给" + bundle.getString("sender_name") + ":");

        long receiver_id = bundle.getLong("from");
        long account_id = bundle.getLong("account_id");
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = communication.getText().toString();
                String sql = "INSERT INTO `message` VALUES(DEFAULT, ?, ?, NOW(), ?, ?)";//评价编号，学生编号，老师编号，学期名称，评价
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if(text != ""){
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setLong(1, account_id);
                                    ps.setLong(2, receiver_id);
                                    ps.setString(3, text);
                                    ps.setInt(4, 0);

                                    int rowCount = DBUtils.Execute(ps,connection);
                                    if(rowCount == 1){
                                        message.what= 2;
                                    }
                                }
                                DBUtils.CloseConnection(connection);
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