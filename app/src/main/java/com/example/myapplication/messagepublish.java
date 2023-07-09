package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class messagepublish extends AppCompatActivity {
    Button back;/*返回按键*/
    Button send;/*发送按键*/
    RadioGroup type;/*信息类型*/
    String strtype;/*信息类型标识 info 教育资讯；activity 学生活动*/
    EditText title;/*标题*/
    EditText text;/*正文*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagepublish);
        send = findViewById(R.id.pub_send);
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