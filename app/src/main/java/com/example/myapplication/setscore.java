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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.CustomSpinnerAdapter;
import com.example.mySpecialConversion.CustomTextWatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*教师端设置成绩*/
public class setscore extends AppCompatActivity {
    Button back;
    Spinner testname;/*考试名称*/
    Spinner termname;//学科名称
    TextView stuname;/*学生姓名*/
    EditText mark;/*学生成绩*/
    EditText sum;//满分
    Button vertify;/*确认按钮*/
    private static long student_id = 0;//学生编号
    private static long teacher_id = 0;//老师编号
    private static CustomSpinnerAdapter adapter = null;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Spinner term_name = findViewById(R.id.termname);/*输入考试名称*/
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    term_name.setAdapter(adapter);
                    break;
                case 2:
                    String stu_name = (String)msg.obj;
                    stuname = findViewById(R.id.stuname);/*加载学生姓名*/
                    stuname.setText(stu_name);
                    break;
                case 3:
                    Toast.makeText(setscore.this, "成绩添加成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(setscore.this, "成绩添加失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(setscore.this, "请选择正确的学科以及学期", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(setscore.this, "请选择正确的分数信息", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setscore);

        Intent intent = getIntent();
        student_id = intent.getLongExtra("student_id",0);
        teacher_id = intent.getLongExtra("teacher_id",0);
        student_id = 1;
        teacher_id = 2;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        testname = findViewById(R.id.testname);/*输入考试名称*/

        termname = findViewById(R.id.termname);//获取学期信息

        stuname = findViewById(R.id.stuname);/*加载学生姓名*/

        mark = findViewById(R.id.mark);/*输入学生分数*/
        mark.addTextChangedListener(new CustomTextWatcher(mark));

        sum = findViewById(R.id.sum);//总成绩
        sum.addTextChangedListener(new CustomTextWatcher(sum));

        vertify = findViewById(R.id.vertify);/*确认按钮，上传考试分数*/
        vertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sum.getText().toString().isEmpty() && !mark.getText().toString().isEmpty()){
                    //获取成绩信息
                    int getscore = Integer.parseInt(mark.getText().toString());
                    int sumscore = Integer.parseInt(sum.getText().toString());

                    if(getscore > sumscore || getscore < 0 || sumscore <= 0){
                        Message message= new Message();
                        message.what = 6;
                        handler.sendMessage(message);
                    }else{
                        if(termname.getSelectedItem() != null && testname.getSelectedItem()!= null){
                            //获取科目
                            String subject = testname.getSelectedItem().toString();
                            String term = termname.getSelectedItem().toString();
                            final long[] term_id = {0};

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String sql = "select termid from `term` where termname = ?";
                                        Connection connection = DBUtils.getConnection();
                                        PreparedStatement ps = connection.prepareStatement(sql);
                                        ps.setString(1,term);

                                        ResultSet resultSet = DBUtils.Query(ps, connection);

                                        if(resultSet.next()){
                                            term_id[0] = resultSet.getLong("termid");
                                        }
                                        // 关闭结果集和数据库连接
                                        resultSet.close();
                                        DBUtils.CloseConnection(connection);

                                        String new_sql = "INSERT INTO `grade` VALUES(DEFAULT ,?,?,?,?,?,?,NOW())";
                                        //学期编号，学生编号，老师编号，分数，总分
                                        Connection new_connection = DBUtils.getConnection();
                                        PreparedStatement new_ps = new_connection.prepareStatement(new_sql);

                                        new_ps.setLong(1,term_id[0]);
                                        new_ps.setLong(2,student_id);
                                        new_ps.setLong(3,teacher_id);
                                        new_ps.setString(4,subject);
                                        new_ps.setInt(5,getscore);
                                        new_ps.setInt(6,sumscore);

                                        int new_resultSet = DBUtils.Execute(new_ps, new_connection);

                                        Message message = new Message();
                                        if(new_resultSet == 1){
                                            message.what = 3;
                                        }else{
                                            message.what = 4;
                                        }
                                        handler.sendMessage(message);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        Log.e("DBUtils", "异常：" + e.getMessage());
                                    }
                                }
                            }).start();
                        }
                        else{
                            Message message= new Message();
                            message.what = 5;
                            handler.sendMessage(message);
                        }
                    }
                }else{
                    Message message= new Message();
                    message.what = 6;
                    handler.sendMessage(message);
                }
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql = "select `termname` from `term`";
                    Connection connection = DBUtils.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ResultSet resultSet = DBUtils.Query(ps, connection);

                    // 处理结果集数据
                    ArrayList<String> data = new ArrayList<>();
                    while (resultSet.next()) {
                        String result = resultSet.getString("termname");
                        data.add(result);
                    }

                    // 关闭结果集和数据库连接
                    resultSet.close();
                    DBUtils.CloseConnection(connection);

                    // 将数据设置给 Spinner
                    adapter = new CustomSpinnerAdapter(setscore.this, android.R.layout.simple_spinner_item, data, 30);

                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);

                    String new_sql = "select uname from `users` where uid = ?";
                    Connection new_connection = DBUtils.getConnection();
                    PreparedStatement new_ps = new_connection.prepareStatement(new_sql);
                    new_ps.setLong(1,student_id);
                    ResultSet new_resultSet = DBUtils.Query(new_ps, new_connection);

                    if(new_resultSet.next()){
                        String stu_name = new_resultSet.getString("uname");

                        // 关闭结果集和数据库连接
                        new_resultSet.close();
                        DBUtils.CloseConnection(new_connection);

                        Message new_message = Message.obtain();
                        new_message.what = 2;
                        new_message.obj = stu_name;
                        handler.sendMessage(new_message);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("DBUtils", "异常：" + e.getMessage());
                }
            }
        }).start();
    }
}