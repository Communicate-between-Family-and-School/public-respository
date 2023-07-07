package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*学生和家长端查看作业*/
public class homework extends AppCompatActivity {
    Button back;
    TextView homework;/*作业文本*/
    Button chinese;/*语文作业按钮*/
    Button math;/*数学作业按钮*/
    Button english;/*英语作业按钮*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        back = findViewById(R.id.back);
        homework = findViewById(R.id.homework);
        chinese = findViewById(R.id.chinese);
        math = findViewById(R.id.math);
        english = findViewById(R.id.english);
        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homework.setText("无语文作业");
            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homework.setText("无数学作业");
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homework.setText("无英语作业");
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}