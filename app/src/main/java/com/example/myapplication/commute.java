package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mySpecialConversion.CustomTextWatcher;
import com.example.mySpecialConversion.ParagraphsIndented;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class commute extends AppCompatActivity {
    TextView attendance;/*考勤情况*/
    Button back;/*返回按钮*/
    Button refresh;/*刷新按钮*/
    Button send;/*发送按钮*/
    TextView answer;/*收到信息*/
    TextView comments;/*教师评语*/
    TextView record;/*奖惩记录*/
    EditText receive;/*收件人id*/
    EditText conversation;/*信息内容*/

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(commute.this,"发送失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(commute.this,"发送成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);

        Intent intent = getIntent();
        final long[] account_id = {intent.getLongExtra("account_id", 0)};
        //account_id[0] = 1;

        attendance = findViewById(R.id.attend);/*显示学生考勤情况  .setText()*/

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh = findViewById(R.id.refresh);/*点击刷新按钮再次读取数据库中信息*/

        send = findViewById(R.id.send);/*点击发送按钮，读取record和conversation中的信息，将conversation的内容发送到reveive的id中*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //规范信件
                String conversationText = conversation.getText().toString();
                String indentedText = ParagraphsIndented.indentParagraphs(conversationText, 2);
                conversation.setText(indentedText);

                // 将缩进后的文本发送给收件人的ID
                long receiverId;
                if(receive.getText().toString().isEmpty()){
                    receiverId = 0;

                }else{
                    receiverId = Long.parseLong(receive.getText().toString());
                }

                String sql = "INSERT INTO `message` VALUES (DEFAULT, ?, ?, NOW(), ?, DEFAULT );";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        try {
                            if(account_id[0] !=0 && receiverId != 0){
                                Connection connection = DBUtils.getConnection();
                                PreparedStatement ps = connection.prepareStatement(sql);
                                if (ps != null) {
                                    ps.setLong(1, account_id[0]);
                                    ps.setLong(2,receiverId);
                                    ps.setString(3,indentedText);

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

        answer = findViewById(R.id.answer);/*显示数据库中的回复信息：answer.setText()*/

        comments = findViewById(R.id.comments);/*根据数据库内容设置评价文本：comments.setText()*/

        record = findViewById(R.id.record);/*根据数据库内容设置奖惩记录文本：record.setText()*/

        receive = findViewById(R.id.receive);
        //自动去掉前缀0
        receive.addTextChangedListener(new CustomTextWatcher(receive));

        conversation = findViewById(R.id.conversation);
    }


}