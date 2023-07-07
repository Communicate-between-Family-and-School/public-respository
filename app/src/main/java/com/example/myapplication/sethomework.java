package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class sethomework extends AppCompatActivity {
    Button back;/*返回按钮*/
    TextView prehomework;/*以往作业文本*/
    EditText homework;/*作业内容*/
    EditText subject;/*作业科目*/
    Button vertify;/*确认按钮*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sethomework);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prehomework = findViewById(R.id.prehomework);/*登录时获取数据库中之前作业的信息 .setText()*/

        subject = findViewById(R.id.subject);/*输入要添加作业的学科*/

        homework = findViewById(R.id.homework);/*输入本次这科作业的内容*/

        vertify = findViewById(R.id.vertify);/*确认按钮，点击之后将学科与作业内容提交的数据库*/

    }
}