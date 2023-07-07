package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class registerActivity extends AppCompatActivity {
    private static ArrayAdapter<String> adapter = null;
    private static long register_uid = 0;//注册账号
    private static String register_account = "";//注册用户名
    private static String register_first_password = "";//注册密码
    private static String register_last_password = "";//确认密码

    private static long rchild_id = 0;//孩子编号
    private static String child_name = "";//孩子姓名
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Spinner classname = findViewById(R.id.register_classname);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    classname.setAdapter(adapter);
                    break;
                case 2:
                    RadioButton load = findViewById(R.id.id_loaded);
                    load.setChecked(true);
                    break;
                case 3:
                    load = findViewById(R.id.id_loaded);
                    load.setChecked(false);
                    break;
                case 4:
                    load = findViewById(R.id.register_pwd_match);
                    load.setChecked(true);
                    break;
                case 5:
                    load = findViewById(R.id.register_pwd_match);
                    load.setChecked(false);
                    break;
                case 6:
                    load = findViewById(R.id.register_loaded);
                    load.setChecked(true);
                    break;
                case 7:
                    load = findViewById(R.id.register_loaded);
                    load.setChecked(false);
                    break;
                case 8:
                    Toast.makeText(registerActivity.this, "注册申请提交成功\n请等待审核", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取所有班级并通过handle传递给下拉框
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "SELECT `class_name` FROM class";
                try {
                    Connection connection = DBUtils.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ResultSet resultSet = DBUtils.Query(ps, connection);

                    // 处理结果集数据
                    ArrayList<String> data = new ArrayList<>();
                    while (resultSet.next()) {
                        String result = resultSet.getString("class_name");
                        data.add(result);
                    }
                    // 关闭结果集和数据库连接
                    resultSet.close();
                    DBUtils.CloseConnection(connection);

                    // 将数据设置给 Spinner
                    adapter = new ArrayAdapter<>(registerActivity.this, android.R.layout.simple_spinner_item, data);
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("DBUtils", "异常：" + e.getMessage());
                }
            }
        }).start();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText apply_id = findViewById(R.id.register_uid);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText child_id = findViewById(R.id.register_childid);

        EditText account = findViewById(R.id.register_account);
        EditText pwd1 = findViewById(R.id.register_password1);
        EditText pwd2 = findViewById(R.id.register_password2);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText childname = findViewById(R.id.child_name);

        Spinner classname = findViewById(R.id.register_classname);

        class CustomTextWatcher implements TextWatcher {
            private EditText editText;
            public CustomTextWatcher(EditText editText) {
                this.editText = editText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 在文本变化时处理
                // 自动去除输入整数的前导零
                String inputText = s.toString();
                String trimmedText = inputText.replaceFirst("^0+(?!$)", "");
                if (!inputText.equals(trimmedText)) {
                    editText.setText(trimmedText);
                    editText.setSelection(trimmedText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
        apply_id.addTextChangedListener(new CustomTextWatcher(apply_id));
        child_id.addTextChangedListener(new CustomTextWatcher(child_id));

        //注册按钮
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button register = findViewById(R.id.register_register);

        RadioButton load1 = findViewById(R.id.id_loaded);
        RadioButton load2 = findViewById(R.id.register_loaded);
        RadioButton match = findViewById(R.id.register_pwd_match);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要先判断是否变化成空，如果没有判断，程序会崩溃
                if (apply_id.getText().toString().isEmpty()) {
                    register_uid = 0;
                    Message message = Message.obtain();
                    message.what = 3;
                    handler.sendMessage(message);
                }else{
                    register_uid = Long.parseLong(apply_id.getText().toString());
                    //判断用户编号是否允许
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String sql = "SELECT IF( " +
                                    "((SELECT COUNT(*) FROM apply_register WHERE apuid = ?) + " +
                                    "(SELECT COUNT(*) FROM users WHERE uid = ?)) > 0, 1, 0) AS result;";
                            try {
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                ps.setLong(1, register_uid);
                                ps.setLong(2,register_uid);
                                ResultSet resultSet = DBUtils.Query(ps, connection);

                                int result = -1;
                                if (resultSet.next()) {
                                    result = resultSet.getInt(1);
                                }
                                // 关闭结果集和数据库连接
                                resultSet.close();
                                DBUtils.CloseConnection(connection);

                                Message message = Message.obtain();
                                if(result == 0)message.what = 2;
                                else message.what = 3;
                                handler.sendMessage(message);

                            } catch (SQLException e) {
                                e.printStackTrace();
                                Log.e("DBUtils", "异常：" + e.getMessage());
                            }
                        }
                    }).start();
                }

                //密码匹配判断
                if(!pwd1.getText().toString().equals(null) && !pwd1.getText().toString().equals("") && pwd1.getText().toString().equals(pwd2.getText().toString())){
                    Message message = Message.obtain();
                    message.what = 4;
                    handler.sendMessage(message);
                }else{
                    Message message = Message.obtain();
                    message.what = 5;
                    handler.sendMessage(message);
                }

                if (child_id.getText().toString().isEmpty()) {
                    rchild_id = 0;
                    Message message = Message.obtain();
                    message.what = 7;
                    handler.sendMessage(message);
                }else{
                    rchild_id = Long.parseLong(child_id.getText().toString());
                    //判断用户编号是否允许
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String sql = "SELECT u.uname, c.class_name " +
                                    "FROM users u " +
                                    "LEFT JOIN user_role ur ON u.uid = ur.uid " +
                                    "LEFT JOIN role r ON r.rid = ur.rid " +
                                    "LEFT JOIN user_class uc ON uc.uid = u.uid " +
                                    "LEFT JOIN class c ON c.cid = uc.cid " +
                                    "WHERE u.uid = ? AND r.rname = 'student'";
                            try {
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                ps.setLong(1, rchild_id);
                                ResultSet resultSet = DBUtils.Query(ps, connection);

                                int rowCount = 0;
                                if (resultSet.last()) {
                                    rowCount = resultSet.getRow();
                                    resultSet.beforeFirst(); // 将游标重置到第一行之前
                                }

                                Message message = Message.obtain();
                                message.what = 7;
                                if(rowCount == 1){
                                    if (resultSet.next()) {
                                        String uname = resultSet.getString("u.uname");
                                        String class_name = resultSet.getString("c.class_name");
                                        if(classname.getSelectedItem() != null){
                                            if(uname.equals(childname.getText().toString()) && class_name.equals(classname.getSelectedItem().toString())){
                                                message.what = 6;
                                            }
                                        }
                                    }
                                }
                                handler.sendMessage(message);
                                // 关闭结果集和数据库连接
                                resultSet.close();
                                DBUtils.CloseConnection(connection);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                Log.e("DBUtils", "异常：" + e.getMessage());
                            }
                        }
                    }).start();
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (load1.isChecked() && load2.isChecked() && match.isChecked() && !account.getText().toString().isEmpty()) {
                            Message message = Message.obtain();
                            message.what = 8;
                            handler.sendMessage(message);
                        }
                    }
                }, 2000); // 延迟执行时间为2秒（2000毫秒），可根据需求调整
            }
        });
    }
}