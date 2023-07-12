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
public class CommunicationListActivity extends AppCompatActivity {
    Button back;
    Button refresh;/*刷新按钮*/
    LinearLayout communications;
    long account_id;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(CommunicationListActivity.this).inflate(R.layout.communication_board, null, false);
            MyTextView text = (MyTextView) layout.getChildAt(0);
            LinearLayout layout0 = (LinearLayout) layout.getChildAt(1);
            LinearLayout layout1 = (LinearLayout) layout0.getChildAt(0);
            LinearLayout layout2 = (LinearLayout) layout0.getChildAt(1);
            LinearLayout layout3 = (LinearLayout) layout.getChildAt(2);
            TextView sender_name = (TextView) layout1.getChildAt(0);
            TextView time = (TextView) layout2.getChildAt(0);
            Button detail_btn = (Button) layout3.getChildAt(0);
            detail_btn.setTextSize(10);
            Bundle bundle = msg.getData();
            long from = bundle.getLong("from");
            text.setInfoId((int) from);
            String message = bundle.getString("message");
            text.setText(message);
            String name = bundle.getString("sender_name");
            sender_name.setText(name);
            String date = bundle.getString("time");
            time.setText(date);
            int mid = bundle.getInt("mid");
            detail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CommunicationListActivity.this, CommunicationDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("from", from);
                    bundle.putString("message", message);
                    bundle.putString("sender_name", name);
                    bundle.putString("time", date);
                    bundle.putInt("mid", mid);
                    bundle.putLong("account_id", account_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            communications.addView(layout);
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_list);

        communications = (LinearLayout)findViewById(R.id.communications);
        Intent intent = getIntent();
        account_id = intent.getLongExtra("account_id", -1);
        //account_id[0] = 1;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh = findViewById(R.id.refresh);/*点击刷新按钮再次读取数据库中信息*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = DBUtils.getConnection();
                    String sql = "SELECT message, uname, time, m.from, mid FROM `message_record` m, users u where m.to=? and m.from=uid ORDER BY time DESC";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setLong(1, account_id);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            while(rs.next()){
                                String text = rs.getString(1);
                                String sender_name = rs.getString(2);
                                String time = rs.getString(3);
                                long from = rs.getLong(4);
                                int mid = rs.getInt(5);
                                Message message = new Message();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("message", text);
                                bundle1.putString("sender_name", sender_name);
                                bundle1.putString("time", time);
                                bundle1.putLong("from", from);
                                bundle1.putInt("mid", mid);
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
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communications.removeAllViews();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = null;
                        try {
                            connection = DBUtils.getConnection();
                            String sql = "SELECT message, uname, time, m.from, mid FROM `message_record` m, users u where m.to=? and m.from=uid ORDER BY mid DESC";
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setLong(1, account_id);
                            if (ps != null) {
                                ResultSet rs = DBUtils.Query(ps,connection);
                                if (rs != null) {
                                    while(rs.next()){
                                        String text = rs.getString(1);
                                        String sender_name = rs.getString(2);
                                        String time = rs.getString(3);
                                        long from = rs.getLong(4);
                                        int mid = rs.getInt(5);
                                        Message message = new Message();
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("message", text);
                                        bundle1.putString("sender_name", sender_name);
                                        bundle1.putString("time", time);
                                        bundle1.putLong("from", from);
                                        bundle1.putInt("mid", mid);
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
        });
    }
}