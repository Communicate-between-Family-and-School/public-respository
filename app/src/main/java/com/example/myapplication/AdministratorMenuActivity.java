package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdministratorMenuActivity extends AppCompatActivity {
    Button back;/*返回按键*/
    Button manage;/*用户管理界面*/
    Button communicate;/*家校沟通*/
    Button message;/*信息发布界面*/
    Button search;/*用户信息查询*/
    Button info;/*教育资讯界面*/
    Button activity;/*学生活动界面*/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_menu);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        communicate = findViewById(R.id.communicate);
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorMenuActivity.this,MoreInfoActivity.class));
            }
        });
        activity = findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorMenuActivity.this, MoreInfoActivity.class);
                startActivity(intent);
            }
        });
        communicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorMenuActivity.this, CommunicationListActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorMenuActivity.this,search.class));
            }
        });

        manage = findViewById(R.id.manage);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorMenuActivity.this, Usermanage.class));
            }
        });
        message = findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorMenuActivity.this, messagepublish.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}