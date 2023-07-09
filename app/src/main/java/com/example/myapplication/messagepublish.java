package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class messagepublish extends AppCompatActivity {
    Button back;/*返回按键*/
    Button send;/*发送按键*/
    RadioGroup type;/*信息类型*/
    String strtype;/*信息类型标识 info 教育资讯；activity 学生活动*/
    EditText title;/*标题*/
    EditText text;/*正文*/
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(messagepublish.this,"发送失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(messagepublish.this,"发送成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagepublish);
        send = findViewById(R.id.pub_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conversationText = text.getText().toString();
                String indentedText = ParagraphsIndented.indentParagraphs(conversationText, 2);
                text.setText(indentedText);
                String head = title.getText().toString();
                String sql;
                if(strtype=="info")  sql = "INSERT INTO `edu_message` VALUES ( DEFAULT , ? , ? , NOW() );";
                else sql = "INSERT INTO `stu_activity` VALUES (DEFAULT, ?, ?, NOW());";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setString(1,head);
                                    ps.setString(2,indentedText);
                                    int rowCount = DBUtils.Execute(ps,connection);
                                    if(rowCount >= 1){
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
        type = findViewById(R.id.pub_type);
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.pub_info){strtype = "info";}
                if(i==R.id.pub_activity){strtype = "activity";}
            }
        });
        title = findViewById(R.id.pub_title);
        text = findViewById(R.id.pub_text);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}