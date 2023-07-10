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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.CustomTextWatcher;
import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class stu_info extends AppCompatActivity {
    Button attend;/*考勤情况*/
    Button communication;/*师生交流*/
    Button back;/*返回按钮*/
    TextView comments;/*教师评语*/
    TextView award_publish;/*奖惩记录*/
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Bundle data = msg.getData();
                    String teacName = data.getString("teacName");
                    int termid = data.getInt("termid");
                    String evaluate = data.getString("evaluate");
                    comments.append("第"+termid+"学期\n"+"老师姓名："+teacName+"\n"+"老师评价："+evaluate+"\n\n");
                    break;
                case 2:
                    Bundle data1 = msg.getData();
                    String text = data1.getString("text");
                    int termid1 = data1.getInt("termid");
                    int ifa = data1.getInt("ifa");
                    if (ifa == 0){
                        award_publish.append("第"+termid1+"学期惩罚\n"+"惩罚内容："+text+"\n\n");
                    }else{
                        award_publish.append("第"+termid1+"学期奖励\n"+"奖励内容："+text+"\n\n");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_info);

        Intent intent = getIntent();
        long account_id = intent.getLongExtra("account_id", 0);

        attend = findViewById(R.id.attend);/*显示学生考勤情况  .setText()*/
//        communication = findViewById(R.id.communications);
        comments = findViewById(R.id.comment);
        award_publish = findViewById(R.id.award_publish);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(stu_info.this, AttendListActivity.class);
                intent.putExtra("account_id", account_id);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = DBUtils.getConnection();
                    String sql1 = "SELECT teac.uname, termid, evaluate FROM evaluate ev, users teac, parent_stu ps where ps.pid=? and ev.sid=ps.sid and teac.uid=ev.tid ORDER BY termid;";
                    PreparedStatement ps = connection.prepareStatement(sql1);
                    ps.setLong(1, account_id);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            while(rs.next()){
                                String teacName = rs.getString(1);
                                int termid = rs.getInt(2);
                                String evaluate = rs.getString(3);
                                Message message = new Message();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("teacName", teacName);
                                bundle1.putInt("termid", termid);
                                bundle1.putString("evaluate", evaluate);
                                message.what = 1; //1表示学生评价
                                message.setData(bundle1);
                                handler.sendMessage(message);
                            }
                        }
//                        ps.close();
                    }
//                     奖惩情况
                    String sql2 = "SELECT termid, ifa, message FROM `award_publish` ap, parent_stu ps where ps.pid=? and ap.sid=ps.sid ORDER BY termid;";
                    ps = connection.prepareStatement(sql2);
                    ps.setLong(1, account_id);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            while(rs.next()){
                                int termid = rs.getInt(1);
                                int ifa = rs.getInt(2);
                                String text = rs.getString(3);
                                Message message = new Message();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("text", text);
                                bundle1.putInt("termid", termid);
                                bundle1.putInt("ifa", ifa);
                                message.what = 2; //2表示学生学生奖惩
                                message.setData(bundle1);
                                handler.sendMessage(message);
                            }
                        }
                        ps.close();
                    }
                    DBUtils.CloseConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("DBUtils", "异常：" + e.getMessage());
                } finally {
                    if (connection != null)
                        DBUtils.CloseConnection(connection);
                }
            }
        }).start();
    }
}