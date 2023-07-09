package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myComponent.MyTextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info);

        LinearLayout notice_all = (LinearLayout) findViewById(R.id.notice_all);
        LinearLayout announce_all = (LinearLayout)findViewById(R.id.announce_all);
        LinearLayout news_all = (LinearLayout) findViewById(R.id.news_all);

        // 学校通知信息页面标题初始化
        new Thread(() -> {
            Connection connection = null;
            try {
                connection = DBUtils.getConnection();
                // 学校通知查询
                String sql = "SELECT title, snid FROM school_notice ORDER BY snid DESC LIMIT 5;";
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = DBUtils.Query(ps,connection);
                    if (rs != null) {
                        int i = 0;
                        while(rs.next()){
                            String title = rs.getString(1);
                            int snid = rs.getInt(2);
                            MyTextView notice = (MyTextView) notice_all.getChildAt(i++);
                            notice.setText(title);
                            notice.setInfoId(snid);
                        }
                    }
                }
                //学校公告查询
                sql = "SELECT title, said FROM school_announce ORDER BY said DESC LIMIT 5;";
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = DBUtils.Query(ps,connection);
                    if (rs != null) {
                        int i = 0;
                        while(rs.next()){
                            String title = rs.getString(1);
                            int said = rs.getInt(2);
                            MyTextView announce = (MyTextView) announce_all.getChildAt(i++);
                            announce.setText(title);
                            announce.setInfoId(said);
                        }
                    }
                }
                // 学校新闻查询
                sql = "SELECT title, snid FROM school_news ORDER BY snid DESC LIMIT 5;";
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = DBUtils.Query(ps,connection);
                    if (rs != null) {
                        int i = 0;
                        while(rs.next()){
                            String title = rs.getString(1);
                            int snid = rs.getInt(2);
                            MyTextView news = (MyTextView) news_all.getChildAt(i++);
                            news.setText(title);
                            news.setInfoId(snid);
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
        // 绑定标题点击事件用于打开文章，0：notice，1：announce，2：news
        for (int i=0; i<5; i++){
            // 对通知绑定点击事件, type = 0
            MyTextView notice = (MyTextView) notice_all.getChildAt(i);
            notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SchoolInfoActivity.this, ArticleDisplayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 0);
                    bundle.putString("title", (String) notice.getText());
                    bundle.putInt("id", notice.getInfoId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            // 对公告绑定点击事件, type = 1
            MyTextView announce = (MyTextView) announce_all.getChildAt(i);
            announce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SchoolInfoActivity.this, ArticleDisplayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putString("title", (String) announce.getText());
                    bundle.putInt("id", announce.getInfoId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            // 对新闻绑定点击事件, type = 2
            MyTextView news = (MyTextView) news_all.getChildAt(i);
            news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SchoolInfoActivity.this, ArticleDisplayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putString("title", (String) news.getText());
                    bundle.putInt("id", news.getInfoId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}