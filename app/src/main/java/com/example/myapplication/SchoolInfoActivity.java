package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info);

//        TextView notice_1 = (TextView)findViewById(R.id.notice_1);
//        TextView notice_2 = (TextView)findViewById(R.id.notice_2);
//        TextView notice_3 = (TextView)findViewById(R.id.notice_3);
//        TextView notice_4 = (TextView)findViewById(R.id.notice_4);
//        TextView notice_5 = (TextView)findViewById(R.id.notice_5);
        LinearLayout notice_all = (LinearLayout) findViewById(R.id.notice_all);
        LinearLayout announce_all = (LinearLayout)findViewById(R.id.announce_all);
        LinearLayout news_all = (LinearLayout) findViewById(R.id.news_all);

        // 学校通知信息页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = DBUtils.getConnection();
                    // 学校通知查询
                    String sql = "SELECT title FROM school_notice ORDER BY snid DESC LIMIT 5;";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            int i = 0;
                            while(rs.next()){
                                String title = rs.getString(1);
                                TextView notice = (TextView) notice_all.getChildAt(i++);
                                notice.setText(title);
                            }
//                            ps.close();
                        }
                    }
                    //学校公告查询
                    sql = "SELECT title FROM school_announce ORDER BY said DESC LIMIT 5;";
                    ps = connection.prepareStatement(sql);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            int i = 0;
                            while(rs.next()){
                                String title = rs.getString(1);
                                TextView notice = (TextView) announce_all.getChildAt(i++);
                                notice.setText(title);
                            }
//                            ps.close();
                        }
                    }
                    // 学校新闻查询
                    sql = "SELECT title FROM school_news ORDER BY snid DESC LIMIT 5;";
                    ps = connection.prepareStatement(sql);
                    if (ps != null) {
                        ResultSet rs = DBUtils.Query(ps,connection);
                        if (rs != null) {
                            int i = 0;
                            while(rs.next()){
                                String title = rs.getString(1);
                                TextView notice = (TextView) news_all.getChildAt(i++);
                                notice.setText(title);
                            }
                            ps.close();
                        }
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