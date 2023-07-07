package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/*教师端写学生评价*/
public class Educate extends AppCompatActivity {
    Button back;/*返回*/
    Button submit;/*提交*/
    Button send;/*发送*/
    Button vertify;/*确认按钮*/
    TextView stuname;/*显示学生姓名*/
    TextView attendance;/*显示学生出勤率*/
    TextView content;/*收到的文本内容*/
    EditText stuid;/*输入学生id*/
    EditText comments;/*学生评价*/
    EditText receiver;/*收信人id*/
    EditText message;/*发送文本内容*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educate);
        vertify = findViewById(R.id.vertify);

        submit = findViewById(R.id.submit);/*提交*/

        send = findViewById(R.id.send);/*发送*/

        attendance = findViewById(R.id.attendance);
        content = findViewById(R.id.content);
        receiver = findViewById(R.id.receiver);
        message = findViewById(R.id.message);
        stuid = findViewById(R.id.stuid);
        stuname = findViewById(R.id.stuname);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}