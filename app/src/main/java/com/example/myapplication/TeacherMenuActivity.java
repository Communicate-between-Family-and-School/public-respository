package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*教师菜单界面*/
public class TeacherMenuActivity extends AppCompatActivity {
    Button back; /*返回按键*/
    Button sethomework;/*布置作业界面*/
    Button techcommute;/*教师交流界面*/
    Button info;/*教育资讯界面*/
    Button activity;/*学生活动界面*/
    Button score;/*成绩界面*/
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);
        txt = findViewById(R.id.textView4);
        txt.setText("家校通（教师端）");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sethomework = findViewById(R.id.homework);
        sethomework.setText("布置作业");
        sethomework.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,sethomework.class));
            }
        });
        techcommute = findViewById(R.id.commute);
        techcommute.setText("学生评价");
        techcommute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,Educate.class));
            }
        });
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this, infoActivity.class));
            }
        });
        activity = findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,StuActivity.class));
            }
        });
        score = findViewById(R.id.score);
        score.setText("发布学生成绩");
        score.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,setscore.class));
            }
        });
    }
}