package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class commute extends AppCompatActivity {
    TextView attendance;/*考勤情况*/
    Button back;/*返回按钮*/
    Button refresh;/*刷新按钮*/
    Button send;/*发送按钮*/
    TextView answer;/*收到信息*/
    TextView comments;/*教师评语*/
    TextView record;/*奖惩记录*/
    EditText receive;/*收件人id*/
    EditText conversation;/*信息内容*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);
        attendance = findViewById(R.id.attendance);/*显示学生考勤情况  .setText()*/

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh = findViewById(R.id.refresh);/*点击刷新按钮再次读取数据库中信息*/

        send = findViewById(R.id.send);/*点击发送按钮，读取record和conversation中的信息，将conversation的内容发送到reveive的id中*/

        answer = findViewById(R.id.answer);/*显示数据库中的回复信息：answer.setText()*/

        comments = findViewById(R.id.comments);/*根据数据库内容设置评价文本：comments.setText()*/

        record = findViewById(R.id.record);/*根据数据库内容设置奖惩记录文本：record.setText()*/

        receive = findViewById(R.id.receive);
        conversation = findViewById(R.id.conversation);
    }
}