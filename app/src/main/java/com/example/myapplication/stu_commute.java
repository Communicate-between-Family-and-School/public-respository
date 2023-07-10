package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.CustomTextWatcher;
import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class stu_commute extends AppCompatActivity {
    TextView attendance;/*考勤情况*/
    Button back;/*返回按钮*/
    Button stu_attend;/*打卡按键*/
    Button communications;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(stu_commute.this,"签到失败",Toast.LENGTH_SHORT).show();
                    attendance.setText("未考勤");
                    break;
                case 2:
                    Toast.makeText(stu_commute.this,"签到成功",Toast.LENGTH_SHORT).show();
                    stu_attend.setEnabled(false);
                    attendance.setText("已签到");
                    break;
                case 3:
                    attendance.setText("未考勤");
                    break;
                case 4:
                    stu_attend.setEnabled(false);
                    attendance.setText("已签到");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_commute);
        Intent intent = getIntent();
        long account_id = intent.getLongExtra("account_id", 0);
        //account_id[0] = 1;

        attendance = findViewById(R.id.attend);/*显示学生考勤情况  .setText()*/
        stu_attend = findViewById(R.id.stu_attend);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message message = new Message();
                message.what = 3;
                try {
                    if(account_id !=0){
                        String sql = "SELECT attend from attendance att where sid=? and TIMESTAMPDIFF(DAY,att.time,NOW())=0;";
                        Connection connection = DBUtils.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                        if (ps != null) {
                            ps.setLong(1, account_id);
                            ResultSet rs = DBUtils.Query(ps,connection);
                            if (rs.next()){
                                if (rs.getInt(1) == 1){
                                    message.what = 4;
                                }
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

        stu_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if(account_id !=0){
                                String sql = "INSERT INTO `attendance` VALUES ( DEFAULT , ? , 1 , NOW() );";
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setLong(1, account_id);
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
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        communications = findViewById(R.id.communications);
        communications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stu_commute.this, CommunicationListActivity.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });

    }
}