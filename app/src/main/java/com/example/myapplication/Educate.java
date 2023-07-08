package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.CustomTextWatcher;
import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*教师端写学生评价*/
public class Educate extends AppCompatActivity {
    Button back;/*返回*/
    Button submit;/*提交*/
    Button vertify;/*确认按钮*/
    TextView stuname;/*显示学生姓名*/
    TextView attendance;/*显示学生出勤率*/
    EditText stuid;/*输入学生id*/
    EditText comments;/*学生评价*/

    //用于存储学期
    private static ArrayAdapter<String> adapter = null;
    private static long stu_id = 0;//学生id
    private static String student_name = "";//学生姓名
    private static String Sterm_name = "";//学期名称
    private static double percent_attendance = 0;//出勤率
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(Educate.this,"提交失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Educate.this,"提交成功",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Spinner term_name = findViewById(R.id.term);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    term_name.setAdapter(adapter);
                    break;
                case 4:
                    Toast.makeText(Educate.this,"学生编号不能为空",Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    stuname = findViewById(R.id.stu_name);
                    stuname.setText(student_name);
                    break;
                case 6:
                    Toast.makeText(Educate.this,"学生编号输入错误\n" +
                            "1.你没有权限对该学生操作\n2.你确保学生编号输入正确",Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    attendance.findViewById(R.id.attendance);

                    // 格式化 double 类型的变量，转换成百分数并保留两位小数
                    String text = String.format("%.2f%%", percent_attendance * 100);
                    attendance.setText(text);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educate);

        Intent intent = getIntent();
        final long[] account_id = {intent.getLongExtra("account_id", 0)};

        stuid = findViewById(R.id.stuid);
        stuid.addTextChangedListener(new CustomTextWatcher(stuid));

        stuname = findViewById(R.id.stu_name);

        vertify = findViewById(R.id.vertify);
        vertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stuid.getText().toString().isEmpty()){
                    stu_id = 0;
                }else{
                    stu_id = Long.parseLong(stuid.getText().toString());
                    if(stu_id != 0){
                        //获取所有班级并通过handle传递给下拉框
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 6;
                                String sql = "SELECT u.uname FROM user_class uc " +
                                        "JOIN user_role ur ON uc.uid = ur.uid " +
                                        "JOIN role r ON r.rid = ur.rid " +
                                        "JOIN users u ON uc.uid = u.uid " +
                                        "WHERE r.rname = 'student' " +
                                        "AND uc.cid IN (SELECT cid FROM user_class WHERE uid = ?)";
                                try {
                                    Connection connection = DBUtils.getConnection();
                                    PreparedStatement ps = connection.prepareStatement(sql);
                                    ps.setLong(1,stu_id);
                                    ResultSet resultSet = DBUtils.Query(ps, connection);

                                    resultSet.last();
                                    int rowCount = resultSet.getRow();
                                    resultSet.beforeFirst();

                                    if(rowCount == 1){
                                        resultSet.next();
                                        student_name = resultSet.getString("u.uname");
                                        message.what = 5;

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String new_sql = "select `termname` from `term`";
                                                try {
                                                    Connection new_connection = DBUtils.getConnection();
                                                    PreparedStatement new_ps = new_connection.prepareStatement(new_sql);
                                                    ResultSet new_resultSet = DBUtils.Query(new_ps, new_connection);

                                                    // 处理结果集数据
                                                    ArrayList<String> data = new ArrayList<>();
                                                    while (new_resultSet.next()) {
                                                        String result = new_resultSet.getString("termname");
                                                        data.add(result);
                                                    }
                                                    // 关闭结果集和数据库连接
                                                    new_resultSet.close();
                                                    DBUtils.CloseConnection(new_connection);

                                                    // 将数据设置给 Spinner
                                                    adapter = new ArrayAdapter<>(Educate.this, android.R.layout.simple_spinner_item, data);
                                                    Message new_message = Message.obtain();
                                                    new_message.what = 3;
                                                    handler.sendMessage(new_message);
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                    Log.e("DBUtils", "异常：" + e.getMessage());
                                                }
                                            }
                                        }).start();

                                    }else{
                                        student_name = "";
                                        message.what = 6;
                                    }

                                    resultSet.close();
                                    DBUtils.CloseConnection(connection);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    Log.e("DBUtils", "异常：" + e.getMessage());
                                }
                                handler.sendMessage(message);
                            }
                        }).start();
                    }else{
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                    }
                }
            }
        });

        Spinner term_name = findViewById(R.id.term);
        term_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取选中学期名称
                Sterm_name = term_name.getSelectedItem().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql = "SELECT " +
                                "(SELECT COUNT(*) FROM `attendance` WHERE attend = true AND time BETWEEN " +
                                "(SELECT start FROM `term` WHERE termname = ?) AND " +
                                "(SELECT end FROM `term` WHERE termname = ?) " +
                                ") / " +
                                "(SELECT COUNT(*) FROM `attendance` WHERE time BETWEEN " +
                                "(SELECT start FROM `term` WHERE termname = ?) AND " +
                                "(SELECT end FROM `term` WHERE termname = ?) " +
                                ") AS per_attend;";
                        try{
                            Connection connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            if(ps != null){
                                ps.setString(1,Sterm_name);
                                ps.setString(2,Sterm_name);
                                ps.setString(3,Sterm_name);
                                ps.setString(4,Sterm_name);

                                ResultSet resultSet = DBUtils.Query(ps,connection);

                                resultSet.next();
                                percent_attendance = resultSet.getDouble("per_attend");

                                Message message = new Message();
                                message.what = 7;
                                handler.sendMessage(message);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Sterm_name = "";
            }
        });

        comments = findViewById(R.id.comments);//评价控件
        submit = findViewById(R.id.submit);/*提交*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //规范信件
                String conversationText = comments.getText().toString();
                String indentedText = ParagraphsIndented.indentParagraphs(conversationText, 2);
                comments.setText(indentedText);

                String sql = "INSERT INTO `evaluate` VALUES(DEFAULT, ?, ?, (SELECT termid FROM term WHERE termname = ?),?)";//评价编号，学生编号，老师编号，学期名称，评价
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if(account_id[0] !=0 && stu_id != 0 && Sterm_name != ""){
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setLong(1,stu_id);
                                    ps.setLong(2, account_id[0]);
                                    ps.setString(3,Sterm_name);
                                    ps.setString(4,indentedText);

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

        attendance = findViewById(R.id.attendance);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}