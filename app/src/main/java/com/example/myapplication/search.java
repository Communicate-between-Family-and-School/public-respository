package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class search extends AppCompatActivity {
    EditText id;/*要查询的用户id*/
    Button vertify;/*确认按键*/
    Button back;
    TextView name;/*用户名*/
    TextView type;/*用户类型*/
    TextView classnum;/*关联班级号*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        id = findViewById(R.id.search_id);
        vertify = findViewById(R.id.search_vertify);
        name = findViewById(R.id.search_name);
        type = findViewById(R.id.search_type);
        classnum = findViewById(R.id.search_classnum);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}