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
    Button educate;/*教师交流界面*/
    Button commute;/*家校沟通界面*/
    Button info;/*教育资讯界面*/
    Button activity;/*学生活动界面*/
    Button score;/*成绩界面*/
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_teacher_menu);
        txt = findViewById(R.id.textView4);
        txt.setText("家校通（教师端）");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sethomework = findViewById(R.id.homework);/*布置作业*/
        sethomework.setText("布置作业");
        sethomework.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,sethomework.class));
            }
        });
        educate = findViewById(R.id.educate);/*学生评价*/
        educate.setText("学生评价");
        educate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,Educate.class));
            }
        });
        commute = findViewById(R.id.commute);/*家校沟通*/
        commute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this, communicate.class));
            }
        });
        info = findViewById(R.id.info);/*教育资讯*/
        info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this, infoActivity.class));
            }
        });
        activity = findViewById(R.id.activity);/*学生活动*/
        activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,StuActivity.class));
            }
        });
        score = findViewById(R.id.score);/*发布成绩*/
        score.setText("发布学生成绩");
        score.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherMenuActivity.this,setscore.class));
            }
        });
    }
}