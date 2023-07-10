package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommunicationDetailActivity extends AppCompatActivity {
    TextView communication, sender_name, time;
    Button back, delete, reply;

    private static int mid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(CommunicationDetailActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(CommunicationDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("初始化", "初始化");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_detail);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        communication = (TextView) findViewById(R.id.communication);
        sender_name = (TextView)findViewById(R.id.sender_name);
        time = (TextView) findViewById(R.id.time);
        delete = (Button) findViewById(R.id.delete);
        reply = (Button) findViewById(R.id.reply);

        communication.setText(bundle.getString("message"));
        sender_name.setText(bundle.getString("sender_name"));
        time.setText(bundle.getString("time"));

        mid = bundle.getInt("mid");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        Log.d("mid的值", String.valueOf(mid));
                        String sql = "DELETE FROM `message_record` where mid = ?;";
                        try {
                            Connection connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if (ps != null) {
                                ps.setInt(1, mid);
                                int rowCount = DBUtils.Execute(ps,connection);
                                if(rowCount == 1){
                                    message.what= 2;
                                }
                            }
                            DBUtils.CloseConnection(connection);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunicationDetailActivity.this, ReplyActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putLong("from", bundle.getLong("from"));
                bundle1.putString("sender_name", bundle.getString("sender_name"));
                bundle1.putLong("account_id", bundle.getLong("account_id"));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }
}