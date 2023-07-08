package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
/*教师端家校沟通*/
public class communicate extends AppCompatActivity {
    Button back;
    Button send;/*发送按钮*/
    Button refresh;/*刷新按钮*/
    EditText receiveid;/*收信人id*/
    EditText content;/*信息内容*/
    TextView answer;/*收到信件*/
    String id;/*收件人id*/
    String text;/*信息文本*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        send = findViewById(R.id.send);
        refresh = findViewById(R.id.refresh);/*刷新显示信息*/


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取收件人id和信件内容
                id = receiveid.getText().toString();
                text = content.getText().toString();
                //信件标准化和信息发送
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        receiveid = findViewById(R.id.receiveid);
        content = findViewById(R.id.conversation);
        answer = findViewById(R.id.answer);
    }
}