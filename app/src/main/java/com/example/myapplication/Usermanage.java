package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Usermanage extends AppCompatActivity {
    Button back;/*返回按键*/
    RadioGroup operate;/*操作组*/
    String stroperate;/*操作标识"insert"插入，"update"修改，"delete"删除*/
    RadioGroup role;/*角色选项*/
    String strrole;/*角色标识*/
    EditText editid;/*用户id*/
    EditText editname;/*用户名*/
    EditText editpwd;/*密码*/
    EditText classnum;/*班号*/
    Button vertify;/*确认按钮*/
    TextView txtid;/*显示id*/
    TextView txtname;/*显示用户名*/
    TextView txtclassnum;/*显示关联班级号*/
    TextView txttype;/*显示用户类型*/
    String sql;
    int signal;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(Usermanage.this,"操作失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Usermanage.this,"操作成功",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Usermanage.this,"发布失败，请检查输入班级号",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanage);
        operate = findViewById(R.id.adm_operate);
        operate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.adm_insert){editid.setEnabled(false);stroperate = "insert";}
                if(i==R.id.adm_update){editid.setEnabled(true);stroperate = "update";}
                if(i==R.id.adm_delete){editid.setEnabled(true);stroperate = "delete";}
            }
        });
        role = findViewById(R.id.adm_role);
        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.adm_leader){classnum.setEnabled(false);strrole = "leader";}
                if(i==R.id.adm_student){classnum.setEnabled(true);strrole = "student";}
                if(i==R.id.adm_parent){classnum.setEnabled(true);strrole = "parent";}
                if(i==R.id.adm_teacher){classnum.setEnabled(true);strrole = "teacher";}
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vertify = findViewById(R.id.adm_vertify);
        vertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = editname.getText().toString();
                String pwd = editpwd.getText().toString();
                String classid = classnum.getText().toString();


                if(stroperate=="insert") sql = "INSERT INTO `users` VALUES ( DEFAULT , ?, ? );";
                if(stroperate=="update") sql = "UPDATE `users` SET uname = ? password = ? WHERE uid = ?;";
                if(stroperate=="delete") sql = "DELETE FROM `users` WHERE uid = ?;";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        if(stroperate == "insert"){
                            try {
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setString(1, uname);
                                    ps.setString(2, pwd);
                                    int rowCount = DBUtils.Execute(ps, connection);
                                    if (rowCount == 1) {
                                        message.what = 2;
                                    }
                                }
                            }catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(stroperate == "update"){
                            try{
                                Integer.parseInt(classid);
                                signal = 1;
                            }catch(Exception e){
                                signal = 0;message.what = 3;
                            }
                            try{
                                if(signal !=0 ){
                                    long uid = Integer.parseInt(editid.getText().toString());
                                    Connection connection = DBUtils.getConnection();
                                    PreparedStatement ps = connection.prepareStatement(sql);
                                    if (ps != null) {
                                        ps.setString(1, uname);
                                        ps.setString(2, pwd);
                                        ps.setLong(3,uid);
                                        int rowCount = DBUtils.Execute(ps, connection);
                                        if (rowCount >= 1) {
                                            message.what = 2;
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(stroperate == "delete"){
                            try{
                                Integer.parseInt(classid);
                                signal = 1;
                            }catch(Exception e){
                                signal = 0;message.what = 3;
                            }
                            try{
                                if(signal !=0 ){
                                    long uid = Integer.parseInt(editid.getText().toString());
                                    Connection connection = DBUtils.getConnection();
                                    PreparedStatement ps = connection.prepareStatement(sql);
                                    if (ps != null) {
                                        ps.setLong(1,uid);
                                        int rowCount = DBUtils.Execute(ps, connection);
                                        if (rowCount >= 1) {
                                            message.what = 2;
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        editid = findViewById(R.id.adm_editid);
        editname = findViewById(R.id.adm_editname);
        editpwd = findViewById(R.id.adm_editpwd);
        classnum = findViewById(R.id.adm_editclass);
        txtid = findViewById(R.id.adm_uid);
        txtname = findViewById(R.id.adm_name);
        txtclassnum = findViewById(R.id.adm_classnum);
        txttype = findViewById(R.id.adm_type);
    }
}