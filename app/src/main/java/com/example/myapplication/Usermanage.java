package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Usermanage extends AppCompatActivity {
    Button back;/*返回按键*/
    RadioButton insert;/*添加选项*/
    RadioButton update;/*修改选项*/
    RadioButton delete;/*删除选项*/
    RadioButton student;/*学生选项*/
    RadioButton teacher;/*教师选项*/
    RadioButton parent;/*家长选项*/
    RadioButton leader;/*领导选项*/
    EditText editid;/*用户id*/
    EditText editname;/*用户名*/
    EditText editpwd;/*密码*/
    EditText classnum;/*班号*/
    Button vertify;/*确认按钮*/
    TextView txtid;/*显示id*/
    TextView txtname;/*显示用户名*/
    TextView txtclassnum;/*显示关联班级号*/
    TextView txttype;/*显示用户类型*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanage);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        insert = findViewById(R.id.adm_insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editid.setEnabled(false);
            }
        });
        update = findViewById(R.id.adm_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editid.setEnabled(true);
            }
        });
        delete = findViewById(R.id.adm_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editid.setEnabled(true);
            }
        });
        student = findViewById(R.id.adm_student);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classnum.setEnabled(true);
            }
        });
        teacher = findViewById(R.id.adm_teacher);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classnum.setEnabled(true);
            }
        });
        parent = findViewById(R.id.adm_parent);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classnum.setEnabled(true);
            }
        });
        leader = findViewById(R.id.adm_leader);
        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classnum.setEnabled(false);
            }
        });
        editid = findViewById(R.id.adm_editid);
        editname = findViewById(R.id.adm_editname);
        editpwd = findViewById(R.id.adm_editpwd);
        classnum = findViewById(R.id.adm_editclass);
        vertify = findViewById(R.id.adm_vertify);
        txtid = findViewById(R.id.adm_uid);
        txtname = findViewById(R.id.adm_name);
        txtclassnum = findViewById(R.id.adm_classnum);
        txttype = findViewById(R.id.adm_type);

    }
}