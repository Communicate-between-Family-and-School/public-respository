package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/*学生控制界面*/
public class StudentMenuActivity extends AppCompatActivity {
    Button back; /*返回按键*/
    Button homework;/*作业界面*/
    Button commute;/*交流界面*/
    Button info;/*教育资讯界面*/
    Button activity;/*学生活动界面*/
    Button score;/*成绩界面*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        //获取参数值,登录id
        Intent intent = getIntent();
        long account_id = intent.getLongExtra("account",0);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        homework = findViewById(R.id.homework);
        homework.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMenuActivity.this, homework.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        commute = findViewById(R.id.commute);
        commute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMenuActivity.this, stu_commute.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMenuActivity.this, MoreInfoActivity.class);
                intent.putExtra("account_id", account_id);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
        activity = findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMenuActivity.this, MoreInfoActivity.class);
                intent.putExtra("account_id", account_id);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        });
        score = findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMenuActivity.this, score.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
    }
}