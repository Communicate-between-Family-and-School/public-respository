package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*教师菜单界面*/
public class TeacherMenuActivity extends AppCompatActivity {
    Button back; /*返回按键*/
    Button sethomework;/*布置作业界面*/
    Button educate;/*教师交流界面*/
    Button commute;/*家校沟通界面*/
    Button info;/*教育资讯界面*/
    Button activity;/*学生活动界面*/
    Button score;/*成绩界面*/
    Button audit;/*审核家长申请界面*/

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Toast.makeText(TeacherMenuActivity.this,"当前没有申请需要审核",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_teacher_menu);

        //获取参数值,登录id
        Intent intent = getIntent();
        long account_id = intent.getLongExtra("account",0);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sethomework = findViewById(R.id.homework);/*布置作业*/
        sethomework.setText("布置作业");
        sethomework.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, sethomework.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        educate = findViewById(R.id.educate);/*学生评价*/
        educate.setText("学生评价");
        educate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, Educate.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        commute = findViewById(R.id.commute);/*家校沟通*/
        commute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, CommunicationListActivity.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        info = findViewById(R.id.info);/*教育资讯*/
        info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, MoreInfoActivity.class);
                intent.putExtra("account_id", account_id);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
        activity = findViewById(R.id.activity);/*学生活动*/
        activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, MoreInfoActivity.class);
                intent.putExtra("account_id", account_id);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        });
        score = findViewById(R.id.score);/*发布成绩*/
        score.setText("发布学生成绩");
        score.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherMenuActivity.this, setscore.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });
        audit = findViewById(R.id.audit);
        audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;

                        Intent new_intent = null;

                        try {
                            String sql = "SELECT apid ,acid FROM `apply_register` ar " +
                                    "LEFT JOIN `user_class` uc ON ar.acid = uc.cid AND uid = ?";
                            Connection connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setLong(1,account_id);

                            ResultSet resultSet = DBUtils.Query(ps,connection);

                            int rowCount = 0;
                            if(resultSet.last()){
                                rowCount = resultSet.getRow();
                            };
                            resultSet.beforeFirst();

                            if(rowCount == 0){
                                message.what = 2;
                            }else{
                                if(resultSet.next()){
                                    long apid = resultSet.getLong("apid");
                                    long acid = resultSet.getLong("acid");


                                    new_intent = new Intent(TeacherMenuActivity.this,audit.class);
                                    new_intent.putExtra("apid",apid);
                                    new_intent.putExtra("acid",acid);
                                    handler.sendMessage(message);
                                    startActivity(new_intent);
                                }
                            }
                            DBUtils.CloseConnection(connection);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DBUtils", "异常：" + e.getMessage());
                        }
                        if(message.what == 2)
                            handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}