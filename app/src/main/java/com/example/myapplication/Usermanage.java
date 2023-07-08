package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

public class Usermanage extends AppCompatActivity {
    Button back;/*返回按键*/
    RadioButton insert;/*添加选项*/
    RadioButton update;/*修改选项*/
    RadioButton delete;/*删除选项*/
    RadioButton student;/*学生选项*/
    RadioButton teacher;/*教师选项*/
    RadioButton parent;/*家长选项*/
    RadioButton leader;/*领导选项*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanage);
        insert = findViewById(R.id.adm_insert);
        update = findViewById(R.id.adm_update);
        delete = findViewById(R.id.adm_delete);
        student = findViewById(R.id.adm_student);
        teacher = findViewById(R.id.adm_teacher);
        parent = findViewById(R.id.adm_parent);
        leader = findViewById(R.id.adm_leader);

        back.setEnabled(false);
    }
}