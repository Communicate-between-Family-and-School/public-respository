package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myComponent.MyTextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleDisplayActivity extends AppCompatActivity {
    TextView title, content;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            content.setText((String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_display);
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("type");
        int id = bundle.getInt("id");
        title = (TextView) findViewById(R.id.title);
        content = (TextView)findViewById(R.id.content);
        title.setText(bundle.getString("title"));
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = DBUtils.getConnection();
                // 学校信息查询，type=0：通知，type=1；公告，type=2：新闻，type=3：教育资讯
                String sql = "";
                if (type == 0){
                    sql = "SELECT notice FROM school_notice where snid=?;";
                } else if (type == 1) {
                    sql = "SELECT announce FROM school_announce where said=?;";
                }else if(type == 2) {
                    sql = "SELECT news FROM school_news where snid=?;";
                }else if(type == 3){
                    sql = "SELECT mcontent FROM edu_message where mid=?;";
                }else if(type == 4){
                    sql = "SELECT satext FROM stu_activity where said=?;";
                }
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, id);
                    ResultSet rs = DBUtils.Query(ps, connection);
                    if (rs != null) {
                        if (rs.next()) {
                            String article = rs.getString(1);
                            Message message = new Message();
                            message.obj = article;
                            handler.sendMessage(message);
                        }
                    }
                    ps.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}