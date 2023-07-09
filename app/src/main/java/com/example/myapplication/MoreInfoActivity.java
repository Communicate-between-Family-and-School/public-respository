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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myComponent.MyTextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoreInfoActivity extends AppCompatActivity {
    TextView infoType;
    LinearLayout titles;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyTextView myView = (MyTextView) LayoutInflater.from(MoreInfoActivity.this).inflate(R.layout.title, null, false);
            Bundle bundle = msg.getData();
            myView.setInfoId(bundle.getInt("id"));
            myView.setText(bundle.getString("title"));
            int type = bundle.getInt("type");
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MoreInfoActivity.this, ArticleDisplayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    bundle.putString("title", (String) myView.getText());
                    bundle.putInt("id", myView.getInfoId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            titles.addView(myView);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Bundle bundle = getIntent().getExtras();
        infoType = (TextView) findViewById(R.id.infoType);
        titles = (LinearLayout) findViewById(R.id.titles);
        int type = bundle.getInt("type");
        // type=0：更多通知，type=1：更多公告，type=2：更多新闻
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = DBUtils.getConnection();
                String sql = "";
                if(type == 0){
                    sql = "SELECT title, snid FROM school_notice ORDER BY snid DESC";
                    infoType.setText("通知");
                }else if (type == 1){
                    sql = "SELECT title, said FROM school_announce ORDER BY said DESC";
                    infoType.setText("公告");
                }else {
                    sql = "SELECT title, snid FROM school_news ORDER BY snid DESC";
                    infoType.setText("新闻");
                }
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = DBUtils.Query(ps,connection);
                    if (rs != null) {
                        while(rs.next()){
                            String title = rs.getString(1);
                            int id = rs.getInt(2);
                            Message message = new Message();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("title", title);
                            bundle1.putInt("id", id);
                            bundle1.putInt("type", type);
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
        }).start();

    }
}