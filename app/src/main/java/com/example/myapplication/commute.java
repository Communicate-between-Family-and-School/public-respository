package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myComponent.MyTextView;
import com.example.mySpecialConversion.CustomTextWatcher;
import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

public class commute extends AppCompatActivity {
//    TextView attendance;/*考勤情况*/
//    Button back;/*返回按钮*/
//    Button refresh;/*刷新按钮*/
//    Button send;/*发送按钮*/
//    TextView answer;/*收到信息*/
//    TextView comments;/*教师评语*/
//    TextView record;/*奖惩记录*/
//    EditText receive;/*收件人id*/
//    EditText conversation;/*信息内容*/
//
//    LinearLayout message_all;//收到信息
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case 1:
//                    Toast.makeText(commute.this,"发送失败",Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    Toast.makeText(commute.this,"发送成功",Toast.LENGTH_SHORT).show();
//                    break;
//                case 3:
//                    LinearLayout linearLayout = findViewById(R.id.linearLayout);
//                    List<Map<String, String>> resultSet = (List<Map<String, String>>) msg.obj;
//
//                    for (Map<String, String> row : resultSet) {
//                        int mid = Integer.parseInt(row.get("mid"));
//                        int read = Integer.parseInt(row.get("read"));
//                        String sender = row.get("uname");
//                    }
//                    break;
//                case 4:
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_commute);
//
//        Intent intent = getIntent();
//        final long[] account_id = {intent.getLongExtra("account_id", 0)};
//
//        attendance = findViewById(R.id.attend);/*显示学生考勤情况  .setText()*/
//
//        back = findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        refresh = findViewById(R.id.refresh);/*点击刷新按钮再次读取数据库中信息*/
//
//        send = findViewById(R.id.send);/*点击发送按钮，读取record和conversation中的信息，将conversation的内容发送到reveive的id中*/
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //规范信件
//                String conversationText = conversation.getText().toString();
//                String indentedText = ParagraphsIndented.indentParagraphs(conversationText, 2);
//                conversation.setText(indentedText);
//
//                // 将缩进后的文本发送给收件人的ID
//                long receiverId;
//                if(receive.getText().toString().isEmpty()){
//                    receiverId = 0;
//
//                }else{
//                    receiverId = Long.parseLong(receive.getText().toString());
//                }
//
//                String sql = "INSERT INTO `message` VALUES (DEFAULT, ?, ?, NOW(), ?, DEFAULT );";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message message = new Message();
//                        message.what = 1;
//                        try {
//                            if(account_id[0] !=0 && receiverId != 0){
//                                Connection connection = DBUtils.getConnection();
//                                PreparedStatement ps = connection.prepareStatement(sql);
//                                if (ps != null) {
//                                    ps.setLong(1, account_id[0]);
//                                    ps.setLong(2,receiverId);
//                                    ps.setString(3,indentedText);
//
//                                    int rowCount = DBUtils.Execute(ps,connection);
//                                    if(rowCount == 1){
//                                        message.what= 2;
//                                    }
//                                }
//                                DBUtils.CloseConnection(connection);
//                            }
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            }
//        });
//
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        Button btn = findViewById(R.id.button20);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String sql = "SELECT * FROM `receive` r WHERE r.to = ?";
//
//                        Connection connection = null;
//                        try {
//                            // 建立数据库连接
//                            connection = DBUtils.getConnection();
//
//                            // 查询数据库获取信息数据
//                            PreparedStatement ps = connection.prepareStatement(sql);
//                            ps.setLong(1,account_id[0]);
//
//                            if(ps != null) {
//                                ResultSet new_resultSet = DBUtils.Query(ps, connection);
//                                List<Map<String, String>> rowList = new ArrayList<>();
//                                try {
//                                    ResultSetMetaData metaData = new_resultSet.getMetaData();
//                                    int columnCount = metaData.getColumnCount();
//
//                                    while (new_resultSet.next()) {
//                                        Map<String, String> rowData = new HashMap<>();
//                                        for (int i = 1; i <= columnCount; i++) {
//                                            String columnName = metaData.getColumnName(i);
//                                            String columnValue = new_resultSet.getString(i);
//                                            rowData.put(columnName, columnValue);
//                                        }
//                                        rowList.add(rowData);
//                                    }
//                                } catch (SQLException e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    new_resultSet.close();
//                                    ps.close();
//                                    DBUtils.CloseConnection(connection);
//                                }
//
//                                Message message = new Message();
//                                message.what = 3;
//                                message.obj = rowList;
//                                handler.sendMessage(message);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });
//
//
//        receive = findViewById(R.id.receive);
//        //自动去掉前缀0
//        receive.addTextChangedListener(new CustomTextWatcher(receive));
//        conversation = findViewById(R.id.conversation);
//    }
}