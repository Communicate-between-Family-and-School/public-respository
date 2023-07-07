package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/*教师端设置成绩*/
public class setscore extends AppCompatActivity {
    Button back;
    EditText testname;/*考试名称*/
    TextView stuname;/*学生姓名*/
    EditText mark;/*学生成绩*/
    Button vertify;/*确认按钮*/
    Button last;/*上一位*/
    Button next;/*下一位*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setscore);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        testname = findViewById(R.id.testname);/*输入考试名称*/

        stuname = findViewById(R.id.stuname);/*加载学生姓名*/

        mark = findViewById(R.id.mark);/*输入学生分数*/

        vertify = findViewById(R.id.vertify);/*确认按钮，上传考试分数*/

        last = findViewById(R.id.last);/*加载上一位学生信息，并更该stuname*/

        next = findViewById(R.id.next);/*加载下一位学生姓名信息，并更改stuname*/

        vertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}