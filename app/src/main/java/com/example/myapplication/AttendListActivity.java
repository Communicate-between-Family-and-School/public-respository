package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myComponent.MyTextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*教师端家校沟通*/
public class AttendListActivity extends AppCompatActivity {
    Button back;
    LinearLayout attendances;
    long account_id;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            TextView textView = new TextView(AttendListActivity.this);
            Bundle bundle = msg.getData();
            int attend = bundle.getInt("attend");
            String time = bundle.getString("time");
            if (attend == 0){
                textView.setText(time+":"+"未签到");
                textView.setTextColor(R.color.red);
            }else {
                textView.setText(time+":"+"已签到");
            }
            textView.setTextSize(20);
            attendances.addView((View) textView);
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_list);

        attendances = (LinearLayout)findViewById(R.id.attendances);
        Intent intent = getIntent();
        account_id = intent.getLongExtra("account_id", 0);
        //account_id[0] = 1;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = DBUtils.getConnection();
                    String sql = "SELECT attend, att.time FROM attendance att, parent_stu ps WHERE ps.pid=? AND ps.sid=att.sid ORDER BY att.time";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setLong(1, account_id);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            while(rs.next()){
                                int attend = rs.getInt(1);
                                String time = rs.getString(2);
                                Message message = new Message();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("time", time);
                                bundle1.putInt("attend", attend);
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
        });
        thread.start();
    }
}