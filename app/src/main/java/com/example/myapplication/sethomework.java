package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class sethomework extends AppCompatActivity {
    Button back;/*返回按钮*/
    TextView prehomework;/*以往作业文本*/
    EditText classnum;/*班级号*/
    EditText homework;/*作业内容*/
    Spinner subject;/*作业科目*/
    Button vertify;/*确认按钮*/
    String sub;/*学科信息*/
    String sql;/*sql命令*/
    EditText ddl;/*截止日期*/
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(sethomework.this,"发布失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(sethomework.this,"发布成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private final static  String[] array = {"语文","数学","英语"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sethomework);
        ddl = findViewById(R.id.ddl);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prehomework = findViewById(R.id.prehomework);/*登录时获取数据库中之前作业的信息 .setText()*/

        subject = findViewById(R.id.subject);/*输入要添加作业的学科*/
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_select,array);
        subject.setAdapter(startAdapter);
        subject.setSelection(0);
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sub = array[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sub = array[0];
            }
        });
        classnum = findViewById(R.id.classnum);/*输入班级号*/
        homework = findViewById(R.id.homework);/*输入本次这科作业的内容*/
        vertify = findViewById(R.id.vertify);/*确认按钮，点击之后将学科与作业内容提交的数据库*/
        vertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = homework.getText().toString();
                String classid = classnum.getText().toString();
                int cid = Integer.parseInt(classid);
                String deadline = ddl.getText().toString();
                if(sub=="语文") sql = "INSERT INTO `homework` VALUES ( ? , DEFAULT , ?,'','', ? , ? );";
                if(sub=="数学") sql = "INSERT INTO `homework` VALUES ( ? , DEFAULT ,'', ? ,'', ? ,? );";
                if(sub=="英语") sql = "INSERT INTO `homework` VALUES ( ? , DEFAULT ,'','', ? , ?, ? );";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if(content!=null && classid!=null){
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setInt(1, cid);
                                    ps.setString(2,content);
                                    Date date = new Date(System.currentTimeMillis());
                                    ps.setDate(3,date);
                                    ps.setString(4,deadline);
                                    int rowCount = DBUtils.Execute(ps,connection);
                                    if(rowCount == 1){
                                        message.what= 2;
                                    }
                                }
                                DBUtils.CloseConnection(connection);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}