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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class audit extends AppCompatActivity {
    TextView audit_id;/*家长编号*/
    TextView audit_pname;/*家长名*/
    TextView audit_sid;/*学生编号*/
    TextView audit_sname;/*学生名*/
    Button back;
    Button reject;/*拒绝*/
    Button pass;/*通过*/
    Button next;/*下一位*/
    private static long apid = 0;//家长申请编号（主键）
    private static long acid = 0;//班级编号

    private static long apuid = 0;//申请账号
    private static String  apname = "";//申请名称
    private static String appassword = "";//申请密码
    private static long asid = 0;//孩子编号
    private static String asname = "";//孩子名字

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    audit_id = findViewById(R.id.audit_pid);
                    audit_pname = findViewById(R.id.audit_pname);
                    audit_sid = findViewById(R.id.audit_sid);
                    audit_sname = findViewById(R.id.audit_sname);

                    List<Map<String, String>> resultSet = (List<Map<String, String>>) msg.obj;
                    for (Map<String, String> row : resultSet) {
                        apuid = Long.parseLong(row.get("apuid"));//申请账号
                        apname = row.get("apname");//申请名称
                        appassword = row.get("appassword");//申请密码
                        asid = Long.parseLong(row.get("asid"));//孩子编号
                        asname = row.get("asname");//孩子名字
                    }
                    audit_id.setText(String.valueOf(apuid));
                    audit_pname.setText(apname);
                    audit_sid.setText(String.valueOf(asid));
                    audit_sname.setText(asname);
                    break;
                case 3:
                    Toast.makeText(audit.this,"拒绝失败",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(audit.this,"拒绝成功",Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(audit.this,"通过失败",Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(audit.this,"已通过",Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(audit.this,"下一个",Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(audit.this,"这已经是最后一个了",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);

        Intent intent = getIntent();
        apid = intent.getLongExtra("apid",7);
        acid = intent.getLongExtra("acid",2021080901);

        audit_id = findViewById(R.id.audit_pid);
        audit_pname = findViewById(R.id.audit_pname);
        audit_sid = findViewById(R.id.audit_sid);
        audit_sname = findViewById(R.id.audit_sname);

        reject = findViewById(R.id.reject);//拒绝
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 3;
                        try {
                            String sql = "DELETE FROM apply_register WHERE apid = ?";
                            Connection connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setLong(1,apid);

                            int rowCount = DBUtils.Execute(ps,connection);
                            if(rowCount == 1){
                                message.what = 4;
                                handler.sendMessage(message);
//                                finish();
                            }

                            DBUtils.CloseConnection(connection);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DBUtils", "异常：" + e.getMessage());
                        }
                        if(message.what == 3)
                            handler.sendMessage(message);
                    }
                }).start();
            }
        });

        pass = findViewById(R.id.pass);//同意
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 5;
                        try {
                            String sql1 = "INSERT INTO `users` VALUES(?,?,?)";
                            String sql2 = "INSERT INTO `user_role` VALUES(DEFAULT,3,?)";
                            String sql3 = "INSERT INTO `parent_stu`  VALUES(DEFAULT ,?,?)";
                            String sql4 = "DELETE FROM apply_register WHERE apid = ?";

                            Connection connection1 = DBUtils.getConnection();
                            Connection connection2 = DBUtils.getConnection();
                            Connection connection3 = DBUtils.getConnection();
                            Connection connection4 = DBUtils.getConnection();

                            PreparedStatement ps1 = connection1.prepareStatement(sql1);
                            PreparedStatement ps2 = connection2.prepareStatement(sql2);
                            PreparedStatement ps3 = connection3.prepareStatement(sql3);
                            PreparedStatement ps4 = connection3.prepareStatement(sql4);

                            ps1.setLong(1,apuid);
                            ps1.setString(2,apname);
                            ps1.setString(3,appassword);

                            ps2.setLong(1,apuid);

                            ps3.setLong(1,apuid);
                            ps3.setLong(2,asid);

                            ps4.setLong(1,apid);

                            int rowCount1 = DBUtils.Execute(ps1,connection1);
                            int rowCount2 = DBUtils.Execute(ps2,connection2);
                            int rowCount3 = DBUtils.Execute(ps3,connection3);
                            int rowCount4 = DBUtils.Execute(ps4,connection4);

                            if(rowCount1 == 1 && rowCount2 == 1 && rowCount3 == 1 && rowCount4 == 1){
                                message.what = 6;
                                handler.sendMessage(message);
//                                finish();
                            }

                            DBUtils.CloseConnection(connection1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DBUtils", "异常：" + e.getMessage());
                        }
                        if(message.what == 5)
                            handler.sendMessage(message);
                    }
                }).start();
            }
        });

        next = findViewById(R.id.parent_next);//下一个
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 7;

                        Intent new_intent = null;

                        try {
                            String sql = "select * from `apply_register` where apid != ?";
                            Connection connection = DBUtils.getConnection();
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setLong(1,apid);

                            ResultSet resultSet = DBUtils.Query(ps,connection);

                            int rowCount = 0;
                            if(resultSet.last()){
                                rowCount = resultSet.getRow();
                            };
                            resultSet.beforeFirst();

                            if(rowCount == 0){
                                message.what = 8;
                            }else{
                                if(resultSet.next()){
                                    long next_apid = resultSet.getLong("apid");

                                    new_intent = new Intent(audit.this,audit.class);
                                    new_intent.putExtra("apid",next_apid);
                                    new_intent.putExtra("acid",acid);
                                    handler.sendMessage(message);
                                    startActivity(new_intent);
                                    finish(); // 结束当前界面
                                }
                            }
                            DBUtils.CloseConnection(connection);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DBUtils", "异常：" + e.getMessage());
                        }
                        if(message.what == 8)
                            handler.sendMessage(message);
                    }
                }).start();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String sql = "select * from `apply_register` where acid = ? and apid = ?;";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                try {
                    Connection connection = DBUtils.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    if (ps != null) {
                        ps.setLong(1,acid);
                        ps.setLong(2, apid);

                        ResultSet resultSet = DBUtils.Query(ps,connection);

                        List<Map<String, String>> rowList = new ArrayList<>();
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        if (resultSet.next()) {
                            Map<String, String> rowData = new HashMap<>();
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                String columnValue = resultSet.getString(i);
                                rowData.put(columnName, columnValue);
                            }
                            rowList.add(rowData);
                        }
                        message.what = 2;
                        message.obj = rowList;
                    }
                    DBUtils.CloseConnection(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                handler.sendMessage(message);
            }
        }).start();

    }
}